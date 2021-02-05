package com.simple;

import com.mysql.jdbc.jdbc2.optional.MysqlXAConnection;
import com.mysql.jdbc.jdbc2.optional.MysqlXid;

import javax.sql.XAConnection;
import javax.transaction.xa.XAException;
import javax.transaction.xa.XAResource;
import javax.transaction.xa.Xid;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.UUID;

/***
 * @Description mysql分布式事务XAConnection模拟
 * @Author: zhangshaolong001
 * @Date: 2021/2/5 10:31 上午
 */
public class MysqlXaConnectionTest {

    public static void main(String[] args) throws SQLException {
        //true表示打印XA语句,，用于调试
        boolean logXaCommands = true;
        // 获得资源管理器操作接口实例 RM1
        Connection conn1 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test2", "root", "123456a?");
        XAConnection xaConn1 = new MysqlXAConnection((com.mysql.jdbc.Connection) conn1, logXaCommands);
        XAResource rm1 = xaConn1.getXAResource();

        // 获得资源管理器操作接口实例 RM2
        Connection conn2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test3", "root", "123456a?");
        XAConnection xaConn2 = new MysqlXAConnection((com.mysql.jdbc.Connection) conn2, logXaCommands);
        XAResource rm2 = xaConn2.getXAResource();
        // AP请求TM执行一个分布式事务，TM生成全局事务id
        byte[] gtrid = UUID.randomUUID().toString().getBytes();
        int formatId = 1;

        // TM生成rm1上的事务分支id
        byte[] bqual1 = UUID.randomUUID().toString().getBytes();
        Xid xid1 = new MysqlXid(gtrid, bqual1, formatId);

        // TM生成rm2上的事务分支id
        byte[] bqual2 = UUID.randomUUID().toString().getBytes();
        Xid xid2 = new MysqlXid(gtrid, bqual2, formatId);

        try {
            // ==============分别执行RM1和RM2上的事务分支====================

            // 执行rm1上的事务分支 One of TMNOFLAGS, TMJOIN, or TMRESUME.
            rm1.start(xid1, XAResource.TMNOFLAGS);
            // 业务1：插入user表
            PreparedStatement ps1 = conn1.prepareStatement("update user set amount = amount - 1 where user_id=1");
            ps1.execute();
            rm1.end(xid1, XAResource.TMSUCCESS);


            // 执行rm2上的事务分支
            rm2.start(xid2, XAResource.TMNOFLAGS);
            // 业务2：插入user_msg表
            PreparedStatement ps2 = conn2.prepareStatement("update user set amount = amount + 1 where user_id=2");
            ps2.execute();
            rm2.end(xid2, XAResource.TMSUCCESS);

            // ===================两阶段提交================================
            // phase1：询问所有的RM 准备提交事务分支
            int rm1Prepare = rm1.prepare(xid1);
            int rm2Prepare = rm2.prepare(xid2);
            // phase2：提交所有事务分支
            boolean onePhase = false;
            //TM判断有2个事务分支，所以不能优化为一阶段提交
            if (rm1Prepare == XAResource.XA_OK && rm2Prepare == XAResource.XA_OK) {
                //所有事务分支都prepare成功，提交所有事务分支
                rm1.commit(xid1, onePhase);

                /**
                 * 如果第一个事务提交了，第二个事务未执行提交，在mysql5.7.7之后那么test2库里一直有个锁未解，哪怕客户端断开连接也无用
                 * 需要应用程序去做补偿机制，直接将所有分支事务再提交一遍，已提交的不影响，未提交的被提交，需要自己实现代码
                 */
//                if (true){
//                    throw new RuntimeException();
//                }
                rm2.commit(xid2, onePhase);
            }else {
                throw new RuntimeException();
            }
        } catch (Exception e) {
            //如果有事务分支没有成功，则回滚
            try {
                rm1.rollback(xid1);
                rm1.rollback(xid2);
            } catch (XAException xaException) {
                xaException.printStackTrace();
            }

        }
    }
}
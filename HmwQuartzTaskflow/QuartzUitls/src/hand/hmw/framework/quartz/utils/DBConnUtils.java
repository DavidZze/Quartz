package hand.hmw.framework.quartz.utils;

import java.io.Serializable;

import java.sql.Connection;
import java.sql.DriverManager;

import java.sql.PreparedStatement;

import java.sql.ResultSet;

import java.sql.SQLException;

import java.util.HashMap;

import javax.naming.InitialContext;

import javax.sql.DataSource;

/**
 * @(#)DBConnUtils.java
 *
 * HAND Enterprise Solution Company.
 *                          All rights reserved
 *
 *  获取数据库连接工具类
 *  @version 1.0.0.0
 */
public class DBConnUtils implements Serializable{
    public DBConnUtils() {
        super();
    }

    //  static {
    //    dataSourceName = getDataSource("DataSource");
    //  }

    //  private static String getDataSource(String dataSource) {
    //    Properties props = new Properties();
    //
    //    ClassLoader cl = Thread.currentThread().getContextClassLoader();
    //    InputStream is =
    //      cl.getResourceAsStream("com/yuchaihi/utility/db/utils/config.properties");
    //    try {
    //      props.load(is);
    //      return props.getProperty(dataSource);
    //    } catch (Exception e) {
    //      e.printStackTrace();
    //      throw new RuntimeException("error wile getDeta");
    //    } finally {
    //      try {
    //        if (is != null)
    //          is.close();
    //      } catch (Exception e) {
    //      }
    //    }
    //    return null;
    //  }

    public static Connection getJDBCConnection() {
        try {
            Connection conn = null;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            conn =
DriverManager.getConnection("jdbc:oracle:thin:@10.211.55.4:1521:XE", "glodon",
                            "welcome1");
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    /**
     * create by David.ze 2015/07/11
     * Class.forName("oracle.jdbc.driver.OracleDriver");
     * @param hostname
     * @param port
     * @param sid
     * @param username
     * @param pwd
     * @return
     */
    public static Connection getJDBCConn(String hostname , String port , String sid ,
                                         String username , String pwd ) throws ClassNotFoundException,
                                                            SQLException {
        
            Connection conn = null;
            Class.forName("oracle.jdbc.driver.OracleDriver");
            
            String thinLink = "jdbc:oracle:thin:@" + hostname + ":" + port + ":" + sid;   
            
            conn = DriverManager.getConnection(thinLink, username,pwd);
            return conn;
        
        }     
        
    
    /**
   * GetConnection with jdbc/xxxDS
   * @param dataSource
   * @return
   */
    public static Connection getJNDIConnectionByContainer(String dataSource) {

        try {
            Connection conn = null;
            InitialContext ctx = new InitialContext();
            if (ctx != null) {
                Object o = ctx.lookup(dataSource);
                DataSource ds = (DataSource)o;
                conn = ds.getConnection();
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    
    
    
    
    
    /**
   * GetConn with comp/env/jdbc/xxxDS
   * @param dataSource
   * @return
   */
    public static Connection getJNDIConnection(String dataSource) {

        try {
            Connection conn = null;
            InitialContext ctx = new InitialContext();
            if (ctx != null) {
                Object o = ctx.lookup("java:comp/env/" + dataSource);
                DataSource ds = (DataSource)o;
                conn = ds.getConnection();
            }
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection conn, PreparedStatement stat,
                             ResultSet rs) {

        try {
            if (conn != null) {
                conn.close();
            }
            if (stat != null) {
                stat.close();
            }
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        //        System.out.println(dataSourceName);
        //        System.out.println(getDataSource("SOADataSource"));
        String sqlStatement =
            "select property_id, property_key, property_value from fnd_properties";
        Connection connection = DBConnUtils.getJDBCConnection();
        //        Connection connection = DBConnUtils.getConnection();
        PreparedStatement pStatement;
        ResultSet result;

        try {
            pStatement = connection.prepareStatement(sqlStatement);
            result = pStatement.executeQuery();
            HashMap<String, String> cmsProperties =
                new HashMap<String, String>();
            while (result.next()) {
                System.out.println(result.getString(2) + ":  " +
                                   result.getString(3));
                //            cmsProperties.put(result.getString(2), result.getString(3));
            }
            connection.close();
            result.close();
            //      result.getRow()
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


}


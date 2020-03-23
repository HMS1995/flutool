package com.xhrmyy.histool.repository;

import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.sql.*;
import java.util.Properties;

/**
 * @Description:
 * @Author HuangShiming
 * @Date 2019-4-22
 */
@Component
public class JdbcUtil {

    private static Properties p = new Properties();

    static{
        try {
            InputStream is = Thread.currentThread().
                    getContextClassLoader().getResourceAsStream("datasource.properties");
            p.load(is);
            Class.forName(p.getProperty("jdbc.driverClassName"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection(){
        try {
            return DriverManager.getConnection(p.getProperty("jdbc.url"),
                    p.getProperty("jdbc.username"),p.getProperty("jdbc.password"));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void close(Connection conn , PreparedStatement ps , ResultSet rs ) {
        if (conn != null) {
            try {
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                if (ps != null) {
                    try {
                        ps.close();
                    } catch (SQLException e) {
                        e.printStackTrace();
                    } finally {
                        if (rs != null) {
                            try {
                                rs.close();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}

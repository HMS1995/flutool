package com.xhrmyy.histool.repository;

import com.xhrmyy.histool.entity.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private static DataSource dataSource;


    public static Connection getConnection(){
        try {
            Class.forName(dataSource.getDriver_class_name());
            return DriverManager.getConnection(dataSource.getUrl(),
                   dataSource.getUsername(),dataSource.getPassword());
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

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import com.microsoft.sqlserver.jdbc.SQLServerDataSource;
import java.io.IOException;
import java.io.InputStream;
import javax.sql.DataSource;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 *
 * @author Ante Prskalo
 */
public class DataSourceSingleton {

    private static final String PATH = "/config/dbCoolMovie.properties";

    private static final String SERVER_NAME = "SERVER_NAME";
    private static final String DATABASE_NAME = "DATABASE_NAME";
    private static final String USER = "USER";
    private static final String PASSWORD = "PASSWORD";

    private static final Properties properties = new Properties();
    
    static {
        try(InputStream is = DataSourceSingleton.class.getResourceAsStream(PATH)) {
            properties.load(is);
            
        } catch (IOException ex) {
            Logger.getLogger(DataSourceSingleton.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private DataSourceSingleton() {
    }

    private static DataSource instance;

    public static DataSource getInstance() {
        if (instance == null) {
            instance = createInstance();
        }
        return instance;
    }

    private static DataSource createInstance() {
        SQLServerDataSource dataSource = new SQLServerDataSource();
        dataSource.setServerName(properties.getProperty(SERVER_NAME));
        dataSource.setDatabaseName(properties.getProperty(DATABASE_NAME));
        dataSource.setUser(properties.getProperty(USER));
        dataSource.setPassword(properties.getProperty(PASSWORD));
        return dataSource;
    }
}

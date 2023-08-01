/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Ante Prskalo
 */
public class AdminControlsRepo implements hr.algebra.dal.IAdminControlsRepo {

    private static final String DELETE_ALL_DATA = "{ CALL deleteAllData }";

    @Override
    public void clearAllData() throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ALL_DATA)) {
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

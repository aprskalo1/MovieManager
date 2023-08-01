/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import org.mindrot.jbcrypt.BCrypt;
import hr.algebra.model.User;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Types;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import javax.sql.DataSource;

public class UserRepo implements hr.algebra.dal.IUserRepo {
    String salt = BCrypt.gensalt();

    private static final String ID_USER = "ID";
    private static final String USERNAME = "Username";
    private static final String PASSWORD = "UserPassword";
    private static final String ROLE = "UserRole";

    private static final String FIND_USER = "{ CALL findUser (?) }";
    private static final String SELECT_USERS = "{ CALL selectUsers }";
    private static final String SELECT_USER = "{ CALL selectUser (?) }";
    private static final String CREATE_USER = "{ CALL createUser (?,?,?) }";

    @Override
    public Optional<User> findUser(String username, char[] password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(FIND_USER)) {
            stmt.setString(USERNAME, username);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String hashedPassword = rs.getString("UserPassword");
                    boolean passwordMatches = BCrypt.checkpw(new String(password), hashedPassword);

                    if (passwordMatches) {
                        return Optional.of(new User(
                                rs.getInt("ID"),
                                rs.getString("Username"),
                                rs.getString("UserRole")
                        ));
                    }
                }
            }
        } finally {
            Arrays.fill(password, ' ');
        }
        return Optional.empty();
    }

    @Override
    public List<User> selectUsers() throws Exception {
        List<User> users = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USERS)) {
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    users.add(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(ROLE)
                    ));
                }
            }
        }
        return users;
    }

    @Override
    public int createUser(String username, char[] password) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_USER)) {
            stmt.setString(USERNAME, username);

            String hashedPassword = hashPassword(new String(password), salt);
            stmt.setString(PASSWORD, hashedPassword);

            stmt.registerOutParameter(ID_USER, Types.INTEGER);
            stmt.executeUpdate();
            return stmt.getInt(ID_USER);
        }
    }

    @Override
    public Optional<User> selectUser(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_USER)) {

            stmt.setInt(ID_USER, id);
            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return Optional.of(new User(
                            rs.getInt(ID_USER),
                            rs.getString(USERNAME),
                            rs.getString(ROLE)
                    ));
                }
            }
        }
        return Optional.empty();
    }

    private String hashPassword(String password, String salt) {
        String hashedPassword = BCrypt.hashpw(password, salt);
        return hashedPassword;
    }
}

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.User;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ante Prskalo
 */
public interface IUserRepo {
    int createUser(String username, char[] password) throws Exception;
    Optional<User> selectUser(int id) throws Exception;
    List<User> selectUsers() throws Exception;
    Optional<User> findUser(String username, char[] password) throws Exception;

}

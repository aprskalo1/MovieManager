/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Ante Prskalo
 */
public class UserRepoFactory {

    private static final Properties properties = new Properties();
    private static final String PATH = "/config/UserRepository.properties";
    private static final String CLASS_NAME = "CLASS_NAME";

    private static IUserRepo repository;

    static {
        try (InputStream is = UserRepoFactory.class.getResourceAsStream(PATH)) {
            properties.load(is);
            repository = (IUserRepo) Class
                    .forName(properties.getProperty(CLASS_NAME))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception ex) {
            Logger.getLogger(UserRepoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static IUserRepo getRepository() {
        return repository;
    }
}

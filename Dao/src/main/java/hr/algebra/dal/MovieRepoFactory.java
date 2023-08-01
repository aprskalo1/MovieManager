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
public class MovieRepoFactory {

    private static final Properties properties = new Properties();
    private static final String PATH = "/config/MovieRepository.properties";
    private static final String CLASS_NAME = "CLASS_NAME";

    private static IMovieRepo repository;

    static {
        try (InputStream is = MovieRepoFactory.class.getResourceAsStream(PATH)) {
            properties.load(is);
            repository = (IMovieRepo) Class
                    .forName(properties.getProperty(CLASS_NAME))
                    .getDeclaredConstructor()
                    .newInstance();
        } catch (Exception ex) {
            Logger.getLogger(MovieRepoFactory.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static IMovieRepo getRepository() {
        return repository;
    }
}

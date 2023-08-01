/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import hr.algebra.model.User;
import java.util.Optional;

/**
 *
 * @author Ante Prskalo
 */
public class SessionManager {
    private static Optional<User> loggedUser = Optional.empty();
    private static Movie selectedMovie;

    public static void setSelectedMovie(Movie selectedMovie) {
        SessionManager.selectedMovie = selectedMovie;
    }

    public static void setLoggedUser(Optional<User> user) {
        loggedUser = user;
    }

    public static Optional<User> getLoggedUser() {
        return loggedUser;
    }

    public static void clearSession() {
        loggedUser = Optional.empty();
    }
}

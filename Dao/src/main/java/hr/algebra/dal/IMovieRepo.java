/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Movie;
import java.util.List;
import java.util.Optional;

/**
 *
 * @author Ante Prskalo
 */
public interface IMovieRepo {

    int createMovie(Movie movie) throws Exception;

    List<Movie> selectMovies() throws Exception;

    void deleteMovie(int id) throws Exception;

    void updateMovie(int id, Movie movie) throws Exception;

    public Optional<Movie> selectMovie(int selectedMovieId) throws Exception;
}

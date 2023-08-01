/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package hr.algebra.dal;

import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import java.util.List;

/**
 *
 * @author Ante Prskalo
 */
public interface IWholeMovieRepo {

    int createActor(Actor actor) throws Exception;

    int createDirector(Director director) throws Exception;

    public void updateActor(Actor actor) throws Exception;

    public void updateDirector(Director director) throws Exception;

    public void deleteActor(int actorId) throws Exception;

    public void deleteDirector(int directorId) throws Exception;

    List<Actor> selectActors() throws Exception;

    List<Actor> selectSpecificActors(Movie selectedMovie) throws Exception;

    int assignActor(int actorId, int wholeMovieId) throws Exception;

    List<Director> selectDirectors() throws Exception;

    int assignDirector(int directorId, int wholeMovieId) throws Exception;

    List<Director> selectSpecificDirectors(Movie selectedMovie) throws Exception;

}

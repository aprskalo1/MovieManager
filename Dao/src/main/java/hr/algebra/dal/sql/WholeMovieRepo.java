/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Ante Prskalo
 */
public class WholeMovieRepo implements hr.algebra.dal.IWholeMovieRepo {

    private static final String ID_ACTOR_DIRECTOR = "ID";
    private static final String FULL_NAME = "FullName";
    private static final String MOVIE_ID = "MovieId";

    private static final String WHOLE_MOVIE_ID = "WholeMovieId";
    private static final String ACTOR_ID = "ActorId";
    private static final String DIRECTOR_ID = "DirectorId";

    private static final String CREATE_ACTOR = "{ CALL createActor (?,?,?) }";
    private static final String CREATE_DIRECTOR = "{ CALL createDirector (?,?,?) }";
    private static final String UPDATE_ACTOR = "{ CALL updateActor (?,?,?) }";
    private static final String UPDATE_DIRECTOR = "{ CALL updateDirector (?,?,?) }";
    private static final String DELETE_ACTOR = "{ CALL deleteActor (?) }";
    private static final String DELETE_DIRECTOR = "{ CALL deleteDirector (?) }";
    private static final String SELECT_ACTORS = "{ CALL selectActors }";
    private static final String SELECT_SPECIFIC_ACTORS = "{ CALL selectSpecificActors (?) }";
    private static final String ASSIGN_ACTOR = "{ CALL assignActor (?,?) }";

    private static final String SELECT_DIRECTORS = "{ CALL selectDirectors }";
    private static final String ASSIGN_DIRECTOR = "{ CALL assignDirector (?,?) }";
    private static final String SELECT_SPECIFIC_DIRECTORS = "{ CALL selectSpecificDirectors (?) }";

    @Override
    public int createActor(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_ACTOR)) {

            stmt.setString(FULL_NAME, actor.getFullName());
            stmt.setInt(MOVIE_ID, actor.getMovieId());
            stmt.registerOutParameter(ID_ACTOR_DIRECTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_ACTOR_DIRECTOR);
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    public void updateActor(Actor actor) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_ACTOR)) {
            stmt.setInt(ID_ACTOR_DIRECTOR, actor.getId());
            stmt.setString(FULL_NAME, actor.getFullName());
            stmt.setInt(MOVIE_ID, actor.getMovieId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deleteActor(int actorId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_ACTOR)) {
            stmt.setInt(ID_ACTOR_DIRECTOR, actorId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public int assignActor(int actorId, int wholeMovieId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(ASSIGN_ACTOR)) {

            stmt.setInt(WHOLE_MOVIE_ID, wholeMovieId);
            stmt.setInt(ACTOR_ID, actorId);

            stmt.executeUpdate();
        }
        return 0;
    }

    @Override
    public List<Actor> selectActors() throws Exception {
        List<Actor> actors = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_ACTORS)) {
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    actors.add(new Actor(
                            rs.getInt(ID_ACTOR_DIRECTOR),
                            rs.getString(FULL_NAME),
                            rs.getInt(MOVIE_ID)
                    ));
                }
            }
        }
        return actors;
    }

    @Override
    public List<Actor> selectSpecificActors(Movie selectedMovie) throws Exception {
        List<Actor> actors = new ArrayList<>();
        int wholeMovieId = selectedMovie.getId();

        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_SPECIFIC_ACTORS)) {

            stmt.setInt(WHOLE_MOVIE_ID, wholeMovieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                actors.add(new Actor(
                        rs.getInt(ID_ACTOR_DIRECTOR),
                        rs.getString(FULL_NAME),
                        rs.getInt(MOVIE_ID)
                ));
            }
        }
        return actors;
    }

    @Override
    public List<Director> selectDirectors() throws Exception {
        List<Director> directors = new ArrayList<>();
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_DIRECTORS)) {
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    directors.add(new Director(
                            rs.getInt(ID_ACTOR_DIRECTOR),
                            rs.getString(FULL_NAME),
                            rs.getInt(MOVIE_ID)
                    ));
                }
            }
        }
        return directors;
    }

    @Override
    public int assignDirector(int directorId, int wholeMovieId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(ASSIGN_DIRECTOR)) {

            stmt.setInt(WHOLE_MOVIE_ID, wholeMovieId);
            stmt.setInt(DIRECTOR_ID, directorId);

            stmt.executeUpdate();
        }
        return 0;
    }

    @Override
    public List<Director> selectSpecificDirectors(Movie selectedMovie) throws Exception {
        List<Director> directors = new ArrayList<>();
        int wholeMovieId = selectedMovie.getId();

        DataSource dataSource = DataSourceSingleton.getInstance();

        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_SPECIFIC_DIRECTORS)) {

            stmt.setInt(WHOLE_MOVIE_ID, wholeMovieId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                directors.add(new Director(
                        rs.getInt(ID_ACTOR_DIRECTOR),
                        rs.getString(FULL_NAME),
                        rs.getInt(MOVIE_ID)
                ));
            }
        }
        return directors;
    }

    @Override
    public int createDirector(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_DIRECTOR)) {

            stmt.setString(FULL_NAME, director.getFullName());
            stmt.setInt(MOVIE_ID, director.getMovieId());
            stmt.registerOutParameter(ID_ACTOR_DIRECTOR, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_ACTOR_DIRECTOR);
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return 0;
    }

    @Override
    public void deleteDirector(int directorId) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_DIRECTOR)) {
            stmt.setInt(ID_ACTOR_DIRECTOR, directorId);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void updateDirector(Director director) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_DIRECTOR)) {
            stmt.setInt(ID_ACTOR_DIRECTOR, director.getId());
            stmt.setString(FULL_NAME, director.getFullName());
            stmt.setInt(MOVIE_ID, director.getMovieId());

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

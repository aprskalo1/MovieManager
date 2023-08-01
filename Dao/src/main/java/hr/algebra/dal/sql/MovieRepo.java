/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.dal.sql;

import hr.algebra.model.Movie;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;

/**
 *
 * @author Ante Prskalo
 */
public class MovieRepo implements hr.algebra.dal.IMovieRepo {

    private static final String ID_MOVIE = "Id";
    private static final String TITLE = "Title";
    private static final String PUBLISHED_DATE = "PublishedTime";
    private static final String DESCRIPTION = "MovieDescription";
    private static final String ORIGINAL_TITLE = "OriginalTitle";
    private static final String LENGTH = "MovieLength";
    private static final String YEAR = "YearMade";
    private static final String GENRE = "Genre";
    private static final String PICTURE_PATH = "PicturePath";

    private static final String CREATE_MOVIE = "{ CALL createMovie (?,?,?,?,?,?,?,?,?) }";
    private static final String DELETE_MOVIE = "{ CALL deleteMovie (?) }";
    private static final String UPDATE_MOVIE = "{ CALL updateMovie (?,?,?,?,?,?,?,?,?) }";
    private static final String SELECT_MOVIES = "{ CALL selectMovies }";
    private static final String SELECT_MOVIE = "{ CALL selectMovie (?) }";

    public int createMovie(Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(CREATE_MOVIE)) {

            stmt.setString(TITLE, movie.getMovieTitle());
            stmt.setString(PUBLISHED_DATE, movie.getDatePublished()
                    .format(Movie.DATE_FORMATTER));
            stmt.setString(DESCRIPTION, movie.getMovieDescription());
            stmt.setString(ORIGINAL_TITLE, movie.getOriginalTitle());
            stmt.setInt(LENGTH, movie.getMovieLength());
            stmt.setInt(YEAR, movie.getYearMade());
            stmt.setString(GENRE, movie.getMovieGenre());
            stmt.setString(PICTURE_PATH, movie.getPicturePath());
            stmt.registerOutParameter(ID_MOVIE, Types.INTEGER);

            stmt.executeUpdate();
            return stmt.getInt(ID_MOVIE);
        }
    }

    public List<Movie> selectMovies() throws Exception {
        List<Movie> movies = new ArrayList<>();

        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIES)) {
            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    movies.add(new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(ORIGINAL_TITLE),
                            LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                            rs.getString(DESCRIPTION),
                            Integer.parseInt(rs.getString(LENGTH)),
                            Integer.parseInt(rs.getString(YEAR)),
                            rs.getString(GENRE),
                            rs.getString(PICTURE_PATH)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return movies;
    }

    public void updateMovie(int id, Movie movie) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(UPDATE_MOVIE)) {

            stmt.setString(TITLE, movie.getMovieTitle().trim());
            stmt.setString(PUBLISHED_DATE, movie.getDatePublished().format(Movie.DATE_FORMATTER));
            stmt.setString(DESCRIPTION, movie.getMovieDescription().trim());
            stmt.setString(ORIGINAL_TITLE, movie.getOriginalTitle().trim());
            stmt.setInt(LENGTH, movie.getMovieLength());
            stmt.setInt(YEAR, movie.getYearMade());
            stmt.setString(GENRE, movie.getMovieGenre());
            stmt.setString(PICTURE_PATH, movie.getPicturePath());

            stmt.setInt(ID_MOVIE, id);

            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Optional<Movie> selectMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(SELECT_MOVIE)) {
            stmt.setInt(ID_MOVIE, id);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Movie(
                            rs.getInt(ID_MOVIE),
                            rs.getString(TITLE),
                            rs.getString(ORIGINAL_TITLE),
                            LocalDateTime.parse(rs.getString(PUBLISHED_DATE), Movie.DATE_FORMATTER),
                            rs.getString(DESCRIPTION),
                            Integer.parseInt(rs.getString(LENGTH)),
                            Integer.parseInt(rs.getString(YEAR)),
                            rs.getString(GENRE),
                            rs.getString(PICTURE_PATH)));
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
        return Optional.empty();
    }

    @Override
    public void deleteMovie(int id) throws Exception {
        DataSource dataSource = DataSourceSingleton.getInstance();
        try (Connection con = dataSource.getConnection(); CallableStatement stmt = con.prepareCall(DELETE_MOVIE)) {
            stmt.setInt(ID_MOVIE, id);
            stmt.executeUpdate();
        } catch (SQLException ex) {
            Logger.getLogger(MovieRepo.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}

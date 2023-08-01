/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.parser.rss;

import hr.algebra.dal.IMovieRepo;
import hr.algebra.dal.IWholeMovieRepo;
import hr.algebra.dal.MovieRepoFactory;
import hr.algebra.dal.WholeMovieRepoFactory;
import hr.algebra.factory.ParserFactory;
import hr.algebra.factory.UrlConnectionFactory;
import hr.algebra.model.Actor;
import hr.algebra.model.Director;
import hr.algebra.model.Movie;
import hr.algebra.utilities.FileUtils;
import hr.algebra.utilities.MessageUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.stream.XMLEventReader;
import javax.xml.stream.XMLStreamConstants;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.events.StartElement;
import javax.xml.stream.events.XMLEvent;
import org.jsoup.Jsoup;

/**
 *
 * @author Ante Prskalo
 */
public class MovieParser {

    private static final String RSS_URL = "https://www.blitz-cinestar-bh.ba/rss.aspx?id=2682";
    private static final String ATTRIBUTE_URL = "url";
    private static final String EXT = ".jpg";
    private static final String DIR = "assets";

    private static IMovieRepo movieRepo;
    private static IWholeMovieRepo wholeMovieRepo;

    public static void parse() throws IOException, XMLStreamException, Exception {
        int currentMovieID = 1;

        movieRepo = MovieRepoFactory.getRepository();
        wholeMovieRepo = WholeMovieRepoFactory.getRepository();

        List<Movie> movies = new ArrayList<>();
        List<Actor> actors = new ArrayList<>();
        List<Director> directors = new ArrayList<>();

        HttpURLConnection con = UrlConnectionFactory.getHttpUrlConnection(RSS_URL);

        try (InputStream is = con.getInputStream();) {
            XMLEventReader reader = ParserFactory.createStaxParser(is);

            Movie movie = null;
            Actor actor = null;
            Director director = null;
            StartElement startElement = null;

            Optional<Tags> tags = Optional.empty();

            while (reader.hasNext()) {
                XMLEvent event = reader.nextEvent();

                switch (event.getEventType()) {
                    case XMLStreamConstants.START_ELEMENT -> {
                        startElement = event.asStartElement();
                        String qualifiedName = startElement.getName().getLocalPart();
                        tags = Tags.from(qualifiedName);
                        if (tags.isPresent() && tags.get().equals(Tags.ITEM)) {
                            actor = new Actor();
                            director = new Director();

                            movie = new Movie();
                            movies.add(movie);

                            movie.setId(currentMovieID);
                            currentMovieID++;
                        }
                    }

                    case XMLStreamConstants.CHARACTERS -> {
                        if (tags.isPresent() && movie != null) {
                            String data = event.asCharacters().getData().trim();
                            switch (tags.get()) {
                                case TITLE:
                                    if (!data.isEmpty()) {
                                        movie.setMovieTitle(data);
                                    }
                                    break;
                                case DATE_PUBLISHED:
                                    if (!data.isEmpty()) {
                                        LocalDateTime datePublished = LocalDateTime.parse(
                                                data,
                                                DateTimeFormatter.RFC_1123_DATE_TIME);
                                        movie.setDatePublished(datePublished);
                                    }
                                    break;
                                case DESCRIPTION:
                                    if (!data.isEmpty()) {
                                        org.jsoup.nodes.Document doc = Jsoup.parse(data);
                                        org.jsoup.nodes.Element descriptionElement = doc.selectFirst("div");
                                        if (descriptionElement != null) {
                                            String description = descriptionElement.ownText();
                                            movie.setMovieDescription(description);
                                        }
                                    }
                                    break;
                                case ORIGINAL_TITLE:
                                    if (!data.isEmpty()) {
                                        movie.setOriginalTitle(data);
                                    }
                                    break;
                                case DIRECTOR:
                                    if (!data.isEmpty()) {
                                        try {
                                            String content = data.replace("<![CDATA[", "").replace("]]>", "");
                                            String[] directorNames = content.split(",");

                                            for (String name : directorNames) {
                                                director = new Director(name, movie.getId());
                                                directors.add(director);
                                            }

                                        } catch (Exception e) {
                                            MessageUtils.showErrorMessage("Error", e.getMessage());
                                            break;
                                        }
                                    }
                                    break;
                                case ACTORS:
                                    if (!data.isEmpty()) {
                                        try {
                                            String content = data.replace("<![CDATA[", "").replace("]]>", "");
                                            String[] actorNames = content.split(",");

                                            for (String name : actorNames) {
                                                actor = new Actor(name, movie.getId());
                                                actors.add(actor);
                                            }

                                        } catch (Exception e) {
                                            MessageUtils.showErrorMessage("Error", e.getMessage());
                                            break;
                                        }
                                    }
                                    break;
                                case LENGTH:
                                    if (!data.isEmpty()) {
                                        movie.setMovieLength(Integer.parseInt(data));
                                    }
                                    break;
                                case YEAR_MADE:
                                    if (!data.isEmpty()) {
                                        movie.setYearMade(Integer.parseInt(data));
                                    }
                                    break;
                                case GENRE:
                                    if (!data.isEmpty()) {
                                        movie.setMovieGenre(data);
                                    }
                                    break;
                                case IMAGE:
                                    if (!data.isEmpty()) {
                                        handlePicture(movie, data);
                                    }
                                    break;
                                default:
                                    throw new AssertionError(tags.get().name());
                            }
                        }
                    }
                }
            }
        }

        movies.forEach(m -> {
            try {
                movieRepo.createMovie(m);
            } catch (Exception ex) {
                Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        actors.forEach(a -> {
            try {
                wholeMovieRepo.createActor(a);
            } catch (Exception ex) {
                Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        directors.forEach(d -> {
            try {
                wholeMovieRepo.createDirector(d);
            } catch (Exception ex) {
                Logger.getLogger(MovieParser.class.getName()).log(Level.SEVERE, null, ex);
            }
        });

        List<Movie> moviesInDB = movieRepo.selectMovies();
        List<Actor> actorsInDB = wholeMovieRepo.selectActors();
        List<Director> directorsInDB = wholeMovieRepo.selectDirectors();

        for (Movie movieInDB : moviesInDB) {
            int movieId = movieInDB.getId();
            for (Actor actorInDB : actorsInDB) {
                if (actorInDB.getMovieId() == movieId) {
                    wholeMovieRepo.assignActor(actorInDB.getId(), movieId);
                }
            }
        }

        for (Movie movieInDB : moviesInDB) {
            int movieId = movieInDB.getId();
            for (Director directorInDB : directorsInDB) {
                if (directorInDB.getMovieId() == movieId) {
                    wholeMovieRepo.assignDirector(directorInDB.getId(), movieId);
                }
            }
        }
    }

    private MovieParser() {
    }

    private static void handlePicture(Movie movie, String url) throws IOException {
        String ext = url.substring(url.lastIndexOf("."));
        if (ext.length() > 4) {
            ext = EXT;
        }
        String pictureName = UUID.randomUUID() + ext;
        String localPath = DIR + File.separator + pictureName;

        FileUtils.copyFromUrl(url, localPath);
        movie.setPicturePath(localPath);

    }

    private enum Tags {
        ITEM("item"),
        TITLE("title"),
        DATE_PUBLISHED("pubDate"),
        DESCRIPTION("description"),
        ORIGINAL_TITLE("orignaziv"),
        DIRECTOR("redatelj"),
        ACTORS("glumci"),
        LENGTH("trajanje"),
        YEAR_MADE("godina"),
        GENRE("zanr"),
        IMAGE("plakat");

        private final String name;

        private Tags(String name) {
            this.name = name;
        }

        private static Optional<Tags> from(String name) {

            for (Tags value : values()) {
                if (value.name.equals(name)) {
                    return Optional.of(value);
                }
            }
            return Optional.empty();
        }
    }
}

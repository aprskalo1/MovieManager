/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;

/**
 *
 * @author Ante Prskalo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Movie {

    public static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @XmlElementWrapper
    @XmlElement(name = "actor")
    private List<Actor> movieactors;

    @XmlElementWrapper
    @XmlElement(name = "director")
    private List<Director> moviedirectors;

    private int id;

    @XmlElement(name = "movietitle")
    private String movieTitle;

    @XmlElement(name = "originaltitle")
    private String originalTitle;

    @XmlElement(name = "datepublished")
    @XmlJavaTypeAdapter(LocalDateTimeAdapter.class)
    private LocalDateTime datePublished;

    @XmlElement(name = "moviedescription")
    private String movieDescription;

    @XmlElement(name = "movielength")
    private int movieLength;

    @XmlElement(name = "yearmade")
    private int yearMade;

    @XmlElement(name = "moviegenre")
    private String movieGenre;

    @XmlElement(name = "picturepath")
    private String picturePath;

    public Movie(List<Actor> movieactors, List<Director> moviedirectors, int id, String movieTitle, String originalTitle, LocalDateTime datePublished, String movieDescription, int movieLength, int yearMade, String movieGenre, String picturePath) {
        this.movieactors = movieactors;
        this.moviedirectors = moviedirectors;
        this.id = id;
        this.movieTitle = movieTitle;
        this.originalTitle = originalTitle;
        this.datePublished = datePublished;
        this.movieDescription = movieDescription;
        this.movieLength = movieLength;
        this.yearMade = yearMade;
        this.movieGenre = movieGenre;
        this.picturePath = picturePath;
    }

    public Movie(int id, String movieTitle, String originalTitle, LocalDateTime datePublished, String movieDescription, int movieLength, int yearMade, String movieGenre, String picturePath) {
        this.id = id;
        this.movieTitle = movieTitle;
        this.originalTitle = originalTitle;
        this.datePublished = datePublished;
        this.movieDescription = movieDescription;
        this.movieLength = movieLength;
        this.yearMade = yearMade;
        this.movieGenre = movieGenre;
        this.picturePath = picturePath;
    }

    public Movie(String movieTitle, String originalTitle, LocalDateTime datePublished, String movieDescription, int movieLength, int yearMade, String movieGenre, String picturePath) {
        this.movieTitle = movieTitle;
        this.originalTitle = originalTitle;
        this.datePublished = datePublished;
        this.movieDescription = movieDescription;
        this.movieLength = movieLength;
        this.yearMade = yearMade;
        this.movieGenre = movieGenre;
        this.picturePath = picturePath;
    }

    public Movie() {
    }

    public static DateTimeFormatter getDATE_TIME_FORMATTER() {
        return DATE_FORMATTER;
    }

    public int getId() {
        return id;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public LocalDateTime getDatePublished() {
        return datePublished;
    }

    public String getMovieDescription() {
        return movieDescription;
    }

    public int getMovieLength() {
        return movieLength;
    }

    public int getYearMade() {
        return yearMade;
    }

    public String getMovieGenre() {
        return movieGenre;
    }

    public String getPicturePath() {
        return picturePath;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMovieTitle(String movieTitle) {
        this.movieTitle = movieTitle;
    }

    public void setOriginalTitle(String originalTitle) {
        this.originalTitle = originalTitle;
    }

    public void setDatePublished(LocalDateTime datePublished) {
        this.datePublished = datePublished;
    }

    public void setMovieDescription(String movieDescription) {
        this.movieDescription = movieDescription;
    }

    public void setMovieLength(int movieLength) {
        this.movieLength = movieLength;
    }

    public void setYearMade(int yearMade) {
        this.yearMade = yearMade;
    }

    public void setMovieGenre(String movieGenre) {
        this.movieGenre = movieGenre;
    }

    public void setPicturePath(String picturePath) {
        this.picturePath = picturePath;
    }

}

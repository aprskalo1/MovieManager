/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

/**
 *
 * @author Ante Prskalo
 */
@XmlAccessorType(XmlAccessType.FIELD)
public class Director {

    private int id;
    
    @XmlElement(name = "fullname")
    private String fullName;
    
    @XmlElement(name = "movieid")
    private int movieId;

    public Director(int id, String fullName, int movieId) {
        this.id = id;
        this.fullName = fullName;
        this.movieId = movieId;
    }

    public Director(String fullName, int movieId) {
        this.fullName = fullName;
        this.movieId = movieId;
    }

    public Director() {
    }

    public int getId() {
        return id;
    }

    public String getFullName() {
        return fullName;
    }

    public int getMovieId() {
        return movieId;
    }
}

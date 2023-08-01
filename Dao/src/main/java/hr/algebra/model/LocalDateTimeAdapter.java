/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package hr.algebra.model;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 *
 * @author Ante Prskalo
 */
public class LocalDateTimeAdapter extends XmlAdapter<String, LocalDateTime> {

    @Override
    public LocalDateTime unmarshal(String vt) throws Exception {
        return LocalDateTime.parse(vt, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    @Override
    public String marshal(LocalDateTime bt) throws Exception {
        return bt.format(DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }
}

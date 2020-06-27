package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class RegisterAll {
    private int userIdx;
    private int registerIdx;
    private String variety;
    private int gender;
    private int transGender;
    private int weight;
    private int age;
    private String protectPlace;
    private String lostPlace;
    private Date lostDate;
    private String findPlace;
    private String findDate;
    private String feature;
    private String tel;
    private String email;
    private String memo;
}

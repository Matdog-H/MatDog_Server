package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Register_spot {
    private int userIdx;
    private int registerIdx;
    private String variety;
    private int gender;
    private int weight;
    private int age;
    private String protectPlace;
    private String findPlace;
    private Date findDate;
    private String feature;
    private String tel;
    private String email;
    private String memo;
}

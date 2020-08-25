package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Register_lost {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private String variety;
    private int gender;
    private int weight;
    private int age;
    private String protectPlace;
    private Date lostDate;
    private String lostPlace;
    private String feature;
    private String tel;
    private String email;
    private String dm;
}

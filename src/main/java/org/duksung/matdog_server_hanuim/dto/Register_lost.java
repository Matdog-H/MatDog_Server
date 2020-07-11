package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.util.Date;

@Data
public class Register_lost {
    private int userIdx;
    private int registerIdx;
    private int status;
    private String variety;
    private int gender;
    private int weight;
    private int age;
    private String protectPlace;
    private String lostPlace;
    private Date lostDate;
    private String feature;
    private String tel;
    private String email;
    private String memo;
}

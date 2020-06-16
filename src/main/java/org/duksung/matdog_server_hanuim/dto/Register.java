package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.sql.Date;

@Data
//분양공고 등록하기
public class Register {
    private int userIdx;
    private int registerIdx;
    private int status;
    private int gender;
    private int transGender;
    private int weight;
    private int age;
    private String protectPlace;
    private String lostDate;
    private String lostPlace;
    private String findDate;
    private String findPlace;
    private String feature;
    private String tel;
    private String email;
    private String memo;
    private boolean heart;
}

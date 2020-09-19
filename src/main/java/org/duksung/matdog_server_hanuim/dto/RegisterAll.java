package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.sql.Date;

@Data
public class RegisterAll {
    private int userIdx;
    private int registerIdx;
    private String kindCd;
    private String sexCd;
    private String neuterYn;
    private String weight;
    private String age;
    private String orgNm;
    private String careAddr;
    private String lostPlace;
    private Date lostDate;
    private Date happenDt;
    private String findPlace;
    private String findDate;
    private String specialMark;
    private String careTel;
    private String email;
    private String dm;
}

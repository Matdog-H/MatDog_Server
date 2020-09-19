package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

import java.sql.Date;

@Data
public class viewAllRes {
    private int registerIdx;
    private int registerStatus;
    private String kindCd;
    private String sexCd;
    private String neuterYn;
    private String weight;
    private String age;
    private String careAddr;
    private Date happenDt;
    private String specialMark;
    private String careTel;
    private String email;
    private String memo;

    private String[] dogUrl;
}

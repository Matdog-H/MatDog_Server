package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

import java.sql.Date;

@Data
public class RegisterRes {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private String kindCd;
    private String sexCd;
    private String age;
    private Date happenDt;
    private String filename;
}

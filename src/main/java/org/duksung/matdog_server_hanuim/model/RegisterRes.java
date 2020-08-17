package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

import java.sql.Date;

@Data
public class RegisterRes {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private String variety;
    private int gender;
    private int age;
    private String protectPlace;
    private Date endDate;
}

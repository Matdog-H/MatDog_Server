package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

import java.sql.Date;

@Data
public class viewAllRes {
    private int registerIdx;
    private int registerStatus;
    private String variety;
    private int gender;
    private int transGender;
    private int weight;
    private int age;
    private String protectPlace;
    private Date endDate;
    private String feature;
    private String tel;
    private String email;
    private String memo;

    private String[] dogUrl;
}

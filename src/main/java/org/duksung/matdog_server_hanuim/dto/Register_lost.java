package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

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
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date registeDate;
    private String lostPlace;
    private String feature;
    private String tel;
    private String email;
    private String dm;
    private String dogUrl;
}

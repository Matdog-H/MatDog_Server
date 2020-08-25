package org.duksung.matdog_server_hanuim.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
public class RegisterReq {
    private int userIdx;
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
    private String dm;

    private int registerIdx;
    private String dogUrl;

    private MultipartFile dogImg;
}

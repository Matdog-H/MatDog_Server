package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
//분양공고 등록하기
public class Register {
    private int userIdx;
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

    private MultipartFile[] dogimg;
    private String dogUrl;
}

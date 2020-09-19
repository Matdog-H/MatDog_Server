package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

import java.sql.Date;

@Data
//분양공고 등록하기
public class Register {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private String kindCd;
    private String sexCd;
    private String neuterYn;
    private String weight;
    private String age;
    private String orgNm;
    private String careAddr;
    private Date happenDt;
    private String specialMark;
    private String careTel;
    private String email;
    private String dm;

    //private MultipartFile[] dogimg;
    private String filename;
}

package org.duksung.matdog_server_hanuim.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;

@Data
public class RegisterReq {
    private int userIdx;
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
    private String dm;

    private int registerIdx;
    private String popfile;

    private MultipartFile dogImg;
}

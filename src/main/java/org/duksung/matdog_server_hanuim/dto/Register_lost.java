package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.sql.Date;

@Data
public class Register_lost {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private String kindCd;
    private String sexCd;
    private String weight;
    private String age;
    private Date happenDt;
    private Date lostDate;
    private String lostPlace;
    private String specialMark;
    private String careTel;
    private String email;
    private String dm;
    private String filename;
}

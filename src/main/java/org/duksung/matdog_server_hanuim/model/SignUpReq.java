package org.duksung.matdog_server_hanuim.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignUpReq {
    private String id;
    private String pw;
    private String name;
    private String addr;
    private String birth;
    private String tel;
    private int telcheck;
    private String email;
    private int emailcheck;
    private String dm;
    private int dmcheck;


//    private MultipartFile profile;
//    private String profileUrl;
}

package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

@Data
public class LoginReq {
    private String id;
    //로그인 이메일
    private String pw;
    //로그인 패스워드
}

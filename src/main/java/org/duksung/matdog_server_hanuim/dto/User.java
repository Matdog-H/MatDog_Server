package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

@Data
public class User {
    private int userIdx; // ID number
    private String id; // ID
    private String pw; // Password
    private String name; // Name
    private String addr; // Address
    private String birth; // 생일
    private String tel; // 휴대폰 번호
    private String email; // Email
    private String memo; // Memo
    private String profileUrl; // 프로필이미지
}

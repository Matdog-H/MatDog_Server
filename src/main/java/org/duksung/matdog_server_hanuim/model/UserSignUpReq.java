package org.duksung.matdog_server_hanuim.model;
import lombok.Data;

@Data
public class UserSignUpReq {
    //회원 가입시 입력

    private String email;
    private String pw;
    private String tel;
    private String addr;
    private String name;

    public boolean checkQualification() {
        return (email != null && pw != null && tel != null && name != null);
    }
}
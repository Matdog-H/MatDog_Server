package org.duksung.matdog_server_hanuim.model;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class SignUpReq {
    private String name;
    private String pw;

    private MultipartFile profile;
    private String profileUrl;
}

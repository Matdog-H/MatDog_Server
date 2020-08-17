package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class DogImgUrl {
    private int registerIdx;
    private String dogUrl;
    private int registerStatus;

    private MultipartFile dogImg;
}

package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;

@Data
public class Register_like {
    private int userIdx;
    private int registerIdx;
    private int registerStatus;
    private int likeStatus;
}

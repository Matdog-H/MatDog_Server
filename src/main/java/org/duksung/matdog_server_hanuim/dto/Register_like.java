package org.duksung.matdog_server_hanuim.dto;

import lombok.Data;
import org.springframework.web.bind.annotation.DeleteMapping;

@Data
public class Register_like {
    private int userIdx;
    private int registerIdx;
    private boolean likeStatus;
    private int status;
}

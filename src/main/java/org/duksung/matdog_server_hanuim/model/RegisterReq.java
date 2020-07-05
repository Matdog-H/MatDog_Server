package org.duksung.matdog_server_hanuim.model;

import lombok.Data;

@Data
public class RegisterReq {
    private int registerIdx;
    private int userIdx;
    private String body;

    public boolean checkProperties() {return (body != null);}
}

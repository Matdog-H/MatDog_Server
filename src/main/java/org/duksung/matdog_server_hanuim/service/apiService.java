package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import javax.xml.ws.Response;

@Slf4j
@Service
public class apiService {
    public DefaultRes save_api(final JSONObject xmlJSONObj){
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_API, xmlJSONObj);
    }
}

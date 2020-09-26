package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.service.apiService;
import org.json.JSONObject;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@Slf4j
public class JSONhttp {
    private final apiService apiService;

    public JSONhttp(apiService apiService) {
        this.apiService = apiService;
    }

    public static int INDENT_FACTOR = 4;

    @GetMapping("json")
    public DefaultRes jsoncall() throws IOException, ParseException {
        HttpURLConnection connection = (HttpURLConnection) new URL("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?" +
                "&numOfRows=100" +
                "&ServiceKey=%2Bjfw9N1iHxC9qnKawxCBqbzgcf9lmav9Ss3uMTKljc1X%2BK8un%2FDeDNsz5aTnA8SixJG3Lv9ovDf6dchFZm4gJw%3D%3D")
                .openConnection();

        connection.connect();

        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        StringBuffer st = new StringBuffer();

        String line;

        while ((line = reader.readLine()) != null) {
            st.append(line);
        }

        JSONObject xmlJSONObj = XML.toJSONObject(st.toString());
        String jsonPrettyPrintString = xmlJSONObj.toString(INDENT_FACTOR);


        return apiService.save_api(jsonPrettyPrintString);
        //return jsonPrettyPrintString;
    }
}

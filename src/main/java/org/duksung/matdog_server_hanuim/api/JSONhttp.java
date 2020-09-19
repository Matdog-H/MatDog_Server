package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.service.apiService;
import org.json.JSONObject;
import org.json.XML;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

@RestController
@Slf4j
public class JSONhttp {
    private final apiService apiService;

    public JSONhttp(apiService apiService){this.apiService = apiService;}
    public static int INDENT_FACTOR = 4;
   // @Scheduled(cron = "10 * * * * *")
    @GetMapping("json")
    public DefaultRes jsoncall() throws MalformedURLException, IOException{
        HttpURLConnection connection = (HttpURLConnection) new URL("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?" +
                "bgnde=20140301" +
                "&endde=20140430" +
                "&pageNo=1" +
                "&numOfRows=1" +
                "&ServiceKey=%2Bjfw9N1iHxC9qnKawxCBqbzgcf9lmav9Ss3uMTKljc1X%2BK8un%2FDeDNsz5aTnA8SixJG3Lv9ovDf6dchFZm4gJw%3D%3D")
                .openConnection();

        connection.connect();

        BufferedInputStream bis = new BufferedInputStream(connection.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        StringBuffer st = new StringBuffer();

        String line;

        while((line = reader.readLine()) != null){
            st.append(line);
        }

        //json 형식으로 변형
        JSONObject xmlJSONObj = XML.toJSONObject(st.toString());
        String jsonPrettyPrintString = xmlJSONObj.toString(INDENT_FACTOR);
        //CrawlingService.crwalingSave(xmlJSONObj);
        //return apiService.save_api(xmlJSONObj);
        //System.out.println(xmlJSONObj.);
        return apiService.save_api(xmlJSONObj);
    }
}

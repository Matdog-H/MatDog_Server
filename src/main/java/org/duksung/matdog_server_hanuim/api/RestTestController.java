package org.duksung.matdog_server_hanuim.api;


import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class RestTestController {

    @GetMapping("/apitest")
    public String callapihttp(){
        StringBuffer result = new StringBuffer();
        try {
            String urlstr = "http://openapi.animal.go.kr/openapi/service/rest/recordAgencySrvc/recordAgency?addr=경기도&pageNo=1&numOfRows=10&ServiceKey=HNz4v9qCVuiOSB5zT3jBaCPlSoc5iJat5Gxh1COMv19LykLM5mvnq8noipY0CSJRUdW%2BnHGetQX8igIZhcLMAQ%3D%3D"+
                    "&type=xml"+
                    "&flag=Y";
            URL url = new URL(urlstr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(),"UTF-8"));

            String returnLine;
            result.append("<xmp>");
            while((returnLine = br.readLine()) !=null){
                result.append(returnLine + "/n");
            }
            urlConnection.disconnect();
        }catch(Exception e){
            e.printStackTrace();
        }
        return result+"</xmp>";
    }
}

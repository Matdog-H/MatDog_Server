package org.duksung.matdog_server_hanuim.api;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
@Slf4j
public class RestTestController {

    @GetMapping("/apitest")
    public String callapihttp(){
        StringBuffer result = new StringBuffer();
        try {
//            String urlstr = "http://openapi.animal.go.kr/openapi/service/rest/recordAgencySrvc/recordAgency?addr=경기도&pageNo=1&numOfRows=10&ServiceKey=ujBKY9Oc4YVgRDK98Qn2yr6MDoarMGG48uz9wqGe5fGDcvQXLduI26oWrm1d3RWK%2FSLdIMy8%2FDamwAsNZNs7kw%3D%3D"+
//                    "&type=xml"+
//                    "&flag=Y";
            String urlstr = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?bgnde=20140301&endde=20140430&pageNo=1&numOfRows=10&ServiceKey=ujBKY9Oc4YVgRDK98Qn2yr6MDoarMGG48uz9wqGe5fGDcvQXLduI26oWrm1d3RWK%2FSLdIMy8%2FDamwAsNZNs7kw%3D%3D";
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
        log.info(result.toString());
        return result+"</xmp>";
    }
}

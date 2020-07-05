package org.duksung.matdog_server_hanuim.api;

import org.apache.catalina.connector.InputBuffer;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@RestController
public class ShelterController {
    @GetMapping("/shelter")
    public String callapi(){
        StringBuffer result = new StringBuffer();
        try{
             String urlstr = "http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/sigungu?upr_cd=6110000"+
                    "ServiceKey=QCcjKd0qGVQ89KKSy8icFp%2Fm364PwFWf0Je0HFwmT372vxI3NBx%2FHco7BINNjLFFLb2xInf7Fz0Hmx1HwldogQ%3D%3D" +
                    "&type=json" +
                    "&pageNo=1" +
                    "&numOfRows=10" +
                    "&flag=Y";

            URL url = new URL(urlstr);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");

            BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));

            String retunLine;
            result.append("<xmp>");
            while ((retunLine = br.readLine()) != null){
                result.append(retunLine + "\n");
            }
            urlConnection.disconnect();
        } catch (Exception e){
            e.printStackTrace();
        }
        return result+"</xmp>";
    }
}

package org.duksung.matdog_server_hanuim.utils;

import com.sun.org.apache.xpath.internal.operations.String;
import org.json.XML;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class JSONMake {
    public static int INDENT_FACTOR = 4;

    public static void main(String[] args) throws MalformedURLException, IOException {
        HttpURLConnection conn = (HttpURLConnection) new URL("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?bgnde=20140301&endde=20140430&pageNo=1&numOfRows=10&ServiceKey=ujBKY9Oc4YVgRDK98Qn2yr6MDoarMGG48uz9wqGe5fGDcvQXLduI26oWrm1d3RWK%2FSLdIMy8%2FDamwAsNZNs7kw%3D%3D").openConnection();

        conn.connect();

        BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(bis));
        StringBuffer st = new StringBuffer();

        java.lang.String line;

        while ((line = reader.readLine()) != null) {
            st.append(line);
        }

        JSONObject xmlJSONObj = XML.toJSONObject(st.toString());
        java.lang.String jsonPrettyPrintString = xmlJSONObj.toString(INDENT_FACTOR);
        System.out.println(jsonPrettyPrintString);
    }
}

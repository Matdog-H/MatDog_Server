package org.duksung.matdog_server_hanuim.api;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class HtmlParser {
    public String HtmlParser(String urlToRead){
        StringBuffer  result = new StringBuffer();

        try{
            URL url = new URL(urlToRead);
            InputStream is = url.openStream();
            int ch;

            while((ch = is.read()) != -1){
                result.append((char) ch);
            }
        } catch (MalformedURLException e){
            e.printStackTrace();
        } catch (IOException e){
            e.printStackTrace();
        }
        return result.toString();
    }
}

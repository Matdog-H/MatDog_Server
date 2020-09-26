package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.api.HtmlParser;
import org.duksung.matdog_server_hanuim.dto.ApiDto;
import org.duksung.matdog_server_hanuim.mapper.ApiMapper;
import org.json.XML;
import org.json.simple.JSONArray;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;

import java.sql.Date;

@Slf4j
@Service
public class ShelterService {
    private final ApiMapper apiMapper;
    private StringBuilder dogStrUrl = new StringBuilder();
    public static int INDENT_FACTOR = 4;

    public ShelterService(final ApiMapper apiMapper){
        this.apiMapper = apiMapper;
    }

    public void save_shelter(){
        log.info("서비스 들어옴");
        dogStrUrl.append("http://openapi.animal.go.kr/openapi/service/rest/abandonmentPublicSrvc/abandonmentPublic?&ServiceKey=%2Bjfw9N1iHxC9qnKawxCBqbzgcf9lmav9Ss3uMTKljc1X%2BK8un%2FDeDNsz5aTnA8SixJG3Lv9ovDf6dchFZm4gJw%3D%3D");
        String jsonSouce = new HtmlParser().HtmlParser(dogStrUrl.toString());
        System.out.println(jsonSouce);

        JSONObject xmlJSONObj = XML.toJSONObject(jsonSouce);
        String jsonString = xmlJSONObj.toString(INDENT_FACTOR);
        log.info("서비스 들어옴 1");
        try{
            log.info("서비스 들어옴 2");
            JSONParser jsonParser = new JSONParser();
            log.info("서비스 들어옴 asdfsad");
            JSONObject jsonObject = (JSONObject) jsonParser.parse(jsonString);log.info("서비스 들어옴 3");
            JSONObject response = (JSONObject) jsonObject.get("response");
            JSONObject body = (JSONObject) response.get("body");log.info("서비스 들어옴 4");
            JSONObject items = (JSONObject) body.get("items");
            JSONArray item = (JSONArray) items.get("item");

            JSONObject dogInfo;

            for(int i = 0; i<item.size(); i++){

                dogInfo = (JSONObject) item.get(i);
                System.out.println(dogInfo);

                ApiDto apiDto = null;
                apiDto.setKindCd(dogInfo.get("kindCd").toString());
                apiDto.setSexCd(dogInfo.get("sexCd").toString());
                apiDto.setNeuterYn(dogInfo.get("neuterYn").toString());
                apiDto.setWeight(dogInfo.get("weight").toString());
                apiDto.setAge(dogInfo.get("age").toString());
                apiDto.setOrgNm(dogInfo.get("orgNm").toString());
                apiDto.setCareAddr(dogInfo.get("careAddr").toString());
                apiDto.setHappenDt((Date) dogInfo.get("happenDt"));
                apiDto.setSpecialMark(dogInfo.get("specialMark").toString());
                apiDto.setCareTel(dogInfo.get("careTel").toString());
                apiDto.setEmail("a");
                apiDto.setDm("a");
                apiDto.setFilename(dogInfo.get("filename").toString());

                apiMapper.save_api(1, apiDto);
                System.out.println(apiDto);
            }
        } catch (Exception e){
            e.getMessage();
        }
    }
}

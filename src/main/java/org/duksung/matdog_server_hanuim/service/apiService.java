package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.ApiDto;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.mapper.ApiMapper;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.sql.Date;
import java.text.SimpleDateFormat;

@Slf4j
@Service
public class apiService {
    private final ApiMapper apiMapper;
    private final LikeMapper likeMapper;
    private final RegisterMapper registerMapper;

    public apiService(final ApiMapper apiMapper, final LikeMapper likeMapper, final RegisterMapper registerMapper){
        this.apiMapper = apiMapper;
        this.likeMapper = likeMapper;
        this.registerMapper = registerMapper;
    }

    @Transactional
    public DefaultRes save_api(final String xmlJSONObj) {
        if(xmlJSONObj == null){
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        } else{
            try{
                JSONParser jsonParser = new JSONParser();
                JSONObject jsonObject = (JSONObject) jsonParser.parse(xmlJSONObj);
                JSONObject response = (JSONObject) jsonObject.get("response");
                JSONObject body = (JSONObject) response.get("body");
                JSONObject items = (JSONObject) body.get("items");
                JSONArray item = (JSONArray) items.get("item");

                JSONObject dogInfo;

                for(int i = 0; i<item.size(); i++){
                    dogInfo = (JSONObject) item.get(i);

                    ApiDto apiDto = new ApiDto();

                    apiDto.setRegisterStatus(1);
                    if(dogInfo.get("kindCd").toString().matches(".*개.*") == true)
                        apiDto.setKindCd(dogInfo.get("kindCd").toString().substring(4));
                    else
                        continue;
                    apiDto.setSexCd(dogInfo.get("sexCd").toString());
                    apiDto.setNeuterYn(dogInfo.get("neuterYn").toString());
                    apiDto.setWeight(dogInfo.get("weight").toString());
                    apiDto.setAge(dogInfo.get("age").toString());
                    apiDto.setOrgNm(dogInfo.get("orgNm").toString());
                    apiDto.setCareAddr(dogInfo.get("careAddr").toString());

                    long longDate = (long)dogInfo.get("happenDt")*79260;
                    System.out.println((long)dogInfo.get("happenDt"));
                    Date sqlDate = new Date(longDate);
                    System.out.println(sqlDate);


                    apiDto.setHappenDt(sqlDate);
                    apiDto.setSpecialMark(dogInfo.get("specialMark").toString());
                    apiDto.setCareTel(dogInfo.get("careTel").toString());
                    apiDto.setEmail("없음");
                    apiDto.setDm("없음");
                    apiDto.setFilename(dogInfo.get("filename").toString());

                    apiMapper.save_api(1, apiDto);
                    likeMapper.save_like(1, apiDto.getRegisterIdx(), 1, 0);
                    registerMapper.save_img(1, apiDto.getRegisterIdx(), (String) dogInfo.get("popfile"), 1);
                }
                return DefaultRes.res(StatusCode.OK, ResponseMessage.CREATED_REGISTER);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
    }
}

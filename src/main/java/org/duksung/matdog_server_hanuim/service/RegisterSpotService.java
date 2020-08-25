package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.mapper.RegisterSpotMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class RegisterSpotService {
    private final RegisterSpotMapper registerSpotMapper;

    public RegisterSpotService(final RegisterSpotMapper registerSpotMapper){
        log.info("목격 서비스");
        this.registerSpotMapper = registerSpotMapper;
    }

    //목격 공고 등록
    @Transactional
    public DefaultRes saveRegister_spot(final int userIdx, final Register_spot register_spot){
        try{
            log.info("목격 공고 저장");
            int insertId = registerSpotMapper.save_spot(userIdx, register_spot);
            Register_spot returnedData = register_spot;
            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(insertId);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER_SPOT, returnedData);
        } catch (Exception e){
            log.info("목격 공고 저장 안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //목격 공고 수정
    @Transactional
    public DefaultRes register_spot_update(final int userIdx, final int registerIdx, final Register_spot register_spot){
        if(registerSpotMapper.findByRegisterIdx_spot(registerIdx) != null){
            //gender,weight,age,protectPlace,findPlace,findDate,feature,tel,email,dm
            try{
                Register_spot myRegisterSpot = registerSpotMapper.findByRegisterIdx_spot(registerIdx);
                if(register_spot.getVariety() != null) myRegisterSpot.setVariety(register_spot.getVariety());
                myRegisterSpot.setGender(register_spot.getGender());
                myRegisterSpot.setAge(register_spot.getAge());
                if(register_spot.getProtectPlace() != null) myRegisterSpot.setProtectPlace(register_spot.getProtectPlace());
                if(register_spot.getFindDate() != null) myRegisterSpot.setFindDate(register_spot.getFindDate());
                if(register_spot.getFeature() != null) myRegisterSpot.setFeature(register_spot.getFeature());
                if(register_spot.getEmail() != null) myRegisterSpot.setEmail(register_spot.getEmail());
                if(register_spot.getDm() != null) myRegisterSpot.setDm(register_spot.getDm());
                int update_registerIdx = registerSpotMapper.update_spot(userIdx, registerIdx, myRegisterSpot);
                Register_spot returnedData = register_spot;
                returnedData.setRegisterIdx(update_registerIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTER_SPOT, returnedData);
            } catch (Exception e){
                log.info("목격 공고 수정 실패");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        log.info("목격 공고 수정 성공");
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }

    //모든 목격 공고 조회_최신순
    @Transactional
    public DefaultRes<List<Register_spot>> getAllRegister_spot(){
        List<Register_spot> registerSpotList = registerSpotMapper.findAll_spot();
        if(registerSpotList.isEmpty()){
            log.info("모든 목격 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("모든 목격 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpotList);
    }

    //나이순 목격 공고 조회
    @Transactional
    public DefaultRes<List<Register_spot>> getAllRegister_spot_age(){
        List<Register_spot> registerSpotList = registerSpotMapper.findAll_spot_age();
        if(registerSpotList.isEmpty()){
            log.info("나이순 목격 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 목격 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpotList);
    }

    //목격 공고 검색
    @Transactional
    public DefaultRes search_spot(final String variety, final String protectPlace){
        List<Register_spot> registerSpotList = registerSpotMapper.search_spot(variety, protectPlace);
        if(registerSpotList.isEmpty()){
            log.info("목격 공고 검색 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("목격 공고 검색 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpotList);
    }

    //목격 공고 삭제
    @Transactional
    public DefaultRes deleteByRegisterIdx_spot(final int userIdx, final int registerIdx){
        final Register_spot registerSpot = registerSpotMapper.findByRegisterIdx_spot(registerIdx);
        if(registerSpot == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        try{
            log.info("목격 공고 삭제 성공");
            registerSpotMapper.deleteRegister_spot(userIdx, registerIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_REGISTER);
        } catch (Exception e){
            log.info("목격 공고 삭제 실패");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.mapper.RegisterLostMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class RegisterLostService {
    private final RegisterLostMapper registerLostMapper;

    public RegisterLostService(final RegisterLostMapper registerLostMapper){
        log.info("실종 서비스");
        this.registerLostMapper = registerLostMapper;
    }

    //실종 공고 등록
    @Transactional
    public DefaultRes saveRegister_lost(final Register_lost register_lost){
        try{
            log.info("실종 공고 저장");
            registerLostMapper.save_lost(register_lost);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER_LOST);
        } catch (Exception e){
            log.info("실종 공고 저장 안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    //실종 공고 수정
    @Transactional
    public DefaultRes register_lost_update(final int registerIdx, final Register_lost register_lost){
        if(registerLostMapper.findByRegisterIdx_lost(registerIdx) != null){
            try{
                Register_lost myRegisterLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
                if(register_lost.getVariety() != null) myRegisterLost.setVariety(register_lost.getVariety());
                myRegisterLost.setGender(register_lost.getGender());
                myRegisterLost.setWeight(register_lost.getWeight());
                myRegisterLost.setAge(register_lost.getAge());
                if(register_lost.getProtectPlace() != null) myRegisterLost.setProtectPlace(register_lost.getProtectPlace());
                if(register_lost.getLostPlace() != null) myRegisterLost.setLostPlace(register_lost.getLostPlace());
                if(register_lost.getLostDate() != null) myRegisterLost.setLostDate(register_lost.getLostDate());
                if(register_lost.getFeature() != null) myRegisterLost.setFeature(register_lost.getFeature());
                if(register_lost.getTel() != null) myRegisterLost.setTel(register_lost.getTel());
                if(register_lost.getEmail() != null) myRegisterLost.setEmail(register_lost.getEmail());
                if(register_lost.getMemo() != null) myRegisterLost.setMemo(register_lost.getMemo());
                registerLostMapper.update_lost(registerIdx, myRegisterLost);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTER);
            } catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }

    //모든 실종 공고 조회
    @Transactional
    public DefaultRes<List<Register_lost>> getAllRegister_lost(){
        List<Register_lost> registerLostList = registerLostMapper.findAll_lost();
        if(registerLostList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    //실종 공고 삭제
    public DefaultRes deleteByRegisterIdx_lost(final int registerIdx){
        final Register_lost registerLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
        if(registerLost == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        try{
            registerLostMapper.deleteRegister_lost(registerIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_REGISTER);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

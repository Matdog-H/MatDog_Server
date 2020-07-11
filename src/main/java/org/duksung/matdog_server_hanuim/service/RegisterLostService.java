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
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Slf4j
@Service
public class RegisterLostService {
    private final RegisterLostMapper registerLostMapper;

    public RegisterLostService(final RegisterLostMapper registerLostMapper) {
        log.info("실종 서비스");
        this.registerLostMapper = registerLostMapper;
    }

    //실종 공고 등록
    @Transactional
    public DefaultRes saveRegister_lost(final int userIdx, final Register_lost register_lost) {
        try {
            log.info("실종 공고 저장");
            int insertId = registerLostMapper.save_lost(userIdx, register_lost);
            Register_lost returnedData = register_lost;
            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(insertId);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER_LOST, returnedData);
        } catch (Exception e) {
            log.info("실종 공고 저장 안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }


    //실종 공고 수정
    @Transactional
    public DefaultRes register_lost_update(final int userIdx, final int registerIdx, final Register_lost register_lost) {
        if (registerLostMapper.findByRegisterIdx_lost(registerIdx) != null) {
            try {
                log.info("실종 공고 수정 성공");
                Register_lost myRegisterLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
                if (myRegisterLost == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
                if (register_lost.getVariety() != null) myRegisterLost.setVariety(register_lost.getVariety());
                myRegisterLost.setGender(register_lost.getGender());
                myRegisterLost.setWeight(register_lost.getWeight());
                myRegisterLost.setAge(register_lost.getAge());
                if (register_lost.getProtectPlace() != null)
                    myRegisterLost.setProtectPlace(register_lost.getProtectPlace());
                if (register_lost.getLostPlace() != null) myRegisterLost.setLostPlace(register_lost.getLostPlace());
                if (register_lost.getLostDate() != null) myRegisterLost.setLostDate(register_lost.getLostDate());
                if (register_lost.getFeature() != null) myRegisterLost.setFeature(register_lost.getFeature());
                if (register_lost.getEmail() != null) myRegisterLost.setEmail(register_lost.getEmail());
                if (register_lost.getMemo() != null) myRegisterLost.setMemo(register_lost.getMemo());
                registerLostMapper.update_lost(userIdx, registerIdx, myRegisterLost);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTER);
            } catch (Exception e) {
                log.info("실종 공고 수정 실패");
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }


    //모든 실종 공고 조회
    @Transactional
    public DefaultRes<List<Register_lost>> getAllRegister_lost() {
        List<Register_lost> registerLostList = registerLostMapper.findAll_lost();
        if (registerLostList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    //나이순 공고 조회
    @Transactional
    public DefaultRes<List<Register_lost>> getAllRegister_lost_age() {
        List<Register_lost> registerLostList = registerLostMapper.findAll_lost_age();
        if (registerLostList.isEmpty()) {
            log.info("나이순 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    //실종 공고 검색
    @Transactional
    public DefaultRes search_lost(final String variety, final String protectPlace){
        List<Register_lost> registerLostList = registerLostMapper.search_lost(variety, protectPlace);
        if(registerLostList.isEmpty()){
            log.info("검색 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("검색 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    //실종 공고 삭제
    @Transactional
    public DefaultRes deleteByRegisterIdx_lost(final int userIdx, final int registerIdx) {
        final Register_lost registerLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
        if (registerLost == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        try {
            registerLostMapper.deleteRegister_lost(userIdx, registerIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_REGISTER);
        } catch (Exception e) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.List;

@Slf4j
@Service
public class RegisterService {
    private final RegisterMapper registerMapper;

    public RegisterService(final RegisterMapper registerMapper) {
        log.info("서비스");
        this.registerMapper = registerMapper;
    }

    //registerIdx 조회?
//    public DefaultRes getRegisterIdx(){
//
//    }

    //분양공고 등록하기
    @Transactional
    public DefaultRes saveRegister(final Register register) {
        try {
            log.info("저장");
            registerMapper.save(register);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER);
        } catch (Exception e) {
            log.info("저장안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
    //분양공고 삭제
//    @Transactional
//    public DefaultRes deleteRegister(final int registerIdx){
//        final Register register = registerMapper.findBy
//    }

    //공고 수정
    @Transactional
    public DefaultRes register_update(final int registerIdx, final Register register){
        Register temp = registerMapper.findByRegisterIdx(registerIdx);
        if(temp == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        try{
            temp.setStatus(register.getStatus());
            temp.setGender(register.getGender());
            temp.setTransGender(register.getTransGender());
            temp.setWeight(register.getWeight());
            temp.setAge(register.getAge());
            if(register.getProtectPlace() != null) temp.setProtectPlace(register.getProtectPlace());
            if(register.getLostDate() != null) temp.setLostDate(register.getLostDate());
            if(register.getLostPlace() != null) temp.setLostPlace(register.getLostPlace());
            if(register.getFindDate() != null) temp.setFindDate(register.getFindDate());
            if(register.getFindPlace() != null) temp.setFindPlace(register.getFindPlace());
            if(register.getFeature() != null) temp.setFeature(register.getFeature());
            if(register.getTel() != null) temp.setTel(register.getTel());
            if(register.getEmail() != null) temp.setEmail(register.getEmail());
            if(register.getMemo() != null) temp.setMemo(register.getMemo());
            registerMapper.register_update(registerIdx, temp);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.UPDATE_REGISTER);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //모든 공고 조회
    @Transactional
    public DefaultRes getAllRegister() {
        final List<Register> registerList = registerMapper.findAll();
        log.info(registerList.toString());
        if (registerList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);

        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }
}

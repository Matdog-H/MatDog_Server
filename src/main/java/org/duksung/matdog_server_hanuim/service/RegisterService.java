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
    //id로 회원 조회

    //분양공고 등록하기(목격)
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

    //분양공고 등록하기(실종)

    //분양공고 등록하기

    //분양공고 삭제
//    @Transactional
//    public DefaultRes deleteRegister(final int registerIdx){
//        final Register register = registerMapper.findBy
//    }

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

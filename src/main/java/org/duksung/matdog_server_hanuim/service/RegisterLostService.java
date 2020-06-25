package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.mapper.RegisterLostMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

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
            log.info("실공 공고 저장");
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
//    @Transactional
//    public DefaultRes register_lost_update(final int registerIdx, final Register_lost register_lost){
//        if(registerLostMapper.findByRegisterIdx_lost(registerIdx) != null){
//            try{
//               // Register_lost myRegisterLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
//                myRegisterLost.setVariety(register_lost.getVariety());
//                myRegisterLost.setGender(register_lost.getGender());
//
//            }
//        }
//    }
}

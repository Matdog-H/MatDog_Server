package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.DogImgUrl;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.User;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.List;

@Slf4j
@Service
public class RegisterService {
    private final RegisterMapper registerMapper;
    private final S3FileUploadService s3FileUploadService;

    public RegisterService(final RegisterMapper registerMapper, final S3FileUploadService s3FileUploadService) {
        log.info("분양 서비스");
        this.registerMapper = registerMapper;
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 분양 공고 객체 반환
     *
     * @param registerIdx
     * @return DefaultRes
     */
    public DefaultRes<Register> findByRegisterIdx(final int registerIdx){
        Register register = registerMapper.findByRegisterIdx(registerIdx);
        if(register != null){
            try{
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, register);
            } catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REGISTER);
    }

    //분양공고 등록하기
    @Transactional
    public DefaultRes saveRegister(final int userIdx, final Register register, final MultipartFile[] dogimg) {
        try {
            log.info("분양 공고 저장");
//
//            String endDate_s = register.getEndDate();
//            SimpleDateFormat transDate = new SimpleDateFormat("yyyy-mm-dd");
//            Date endDate_d = (Date) transDate.parse(endDate_s);
//
//            register.setEndDate(endDate_d);

            registerMapper.save(userIdx, register);
            Register returnedData = register;
            register.getRegisterIdx();
            log.info(Integer.toString(register.getRegisterIdx()));

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register.getRegisterIdx());

            for(int i = 0; i<dogimg.length; i++){
                log.info(Integer.toString(returnedData.getRegisterIdx())+"minjin");
                MultipartFile img = dogimg[i];
                String url = s3FileUploadService.upload(img);
                log.info(Integer.toString(returnedData.getRegisterIdx())+"hi");
                registerMapper.save_img(register.getRegisterIdx(), url, register.getRegisterStatus());
                log.info(Integer.toString(returnedData.getRegisterIdx())+"hi3");
            }
            log.info(Integer.toString(returnedData.getRegisterIdx())+"hi2");
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER, returnedData);
        } catch (Exception e) {
            log.info("저장안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

//    @Transactional
//    public DefaultRes img_save(final DogImgUrl dogImgUrl){
//        try{
//            registerMapper.save_img(dogImgUrl.getRegisterIdx(), dogImgUrl.getDogUrl(), dogImgUrl.getRegisterStatus());
//
//            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER_IMG);
//        }
//    }

    //검색
    @Transactional
    public DefaultRes search_register(final String variety, final String protectPlace){
        List<Register> registerList = registerMapper.search_register(variety, protectPlace);
        if(registerList.isEmpty()){
            log.info("검색 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("검색 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    //공고 수정
    @Transactional
    public DefaultRes register_update(final int userIdx, final int registerIdx, final Register register) {

        if (registerMapper.findByRegisterIdx(registerIdx) != null) {
            try {
                Register myRegister = registerMapper.findByRegisterIdx(registerIdx);
                if(myRegister == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);

                if(register.getVariety() != null) myRegister.setVariety(register.getVariety());
                myRegister.setGender(register.getGender());
                myRegister.setTransGender(register.getTransGender());
                myRegister.setWeight(register.getWeight());
                myRegister.setAge(register.getAge());
                if(register.getProtectPlace() != null) myRegister.setProtectPlace(register.getProtectPlace());
                if(register.getFeature() != null) myRegister.setFeature(register.getFeature());
                if(register.getTel() != null) myRegister.setTel(register.getTel());
                if(register.getEmail() != null) myRegister.setEmail(register.getEmail());
                if(register.getMemo() != null) myRegister.setMemo(register.getMemo());

                int update_registerIdx = registerMapper.update(userIdx, registerIdx, myRegister);
                log.info(Integer.toString(update_registerIdx));
                Register returnedDate = register;
                returnedDate.setRegisterIdx(registerIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_REGISTER, returnedDate);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }

    //모든 공고 조회
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister() {
        List<RegisterRes> registerList = registerMapper.findAll_register();
        if (registerList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    //나이순 공고 조회
    @Transactional
    public DefaultRes<List<Register>> getAllRegister_age(){
        List<Register> registerList = registerMapper.findAll_age();
        if(registerList.isEmpty()){
            log.info("나이순 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    //userIdx가 쓴 공고 반환
    @Transactional
    public DefaultRes<List<Register>> getUserWriteRegister(final int userIdx){
        List<Register> registerList = registerMapper.findByuserIdx(userIdx);
        if(registerList.isEmpty()){
            log.info("유저가 쓴 분양 공고 리스트 반환");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("유저가 쓴 분양 공고 리스트 반환 실패");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    //공고 삭제
    @Transactional
    public DefaultRes deleteByRegisterIdx(final int userIdx, final int registerIdx){
        final Register register = registerMapper.findByRegisterIdx(registerIdx);
        if(register == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        try{
            registerMapper.deleteRegister(userIdx, registerIdx);
            return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_REGISTER);
        } catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    //모든 공고 보여주기
    public DefaultRes<Register> viewAllRegister(final int registerStatus, final int registerIdx) {
        Register register = registerMapper.viewAllRegister(registerStatus, registerIdx);
        if (register != null) {
            try {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, register);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REGISTER);
    }
}

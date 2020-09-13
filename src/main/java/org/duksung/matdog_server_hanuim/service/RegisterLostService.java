package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterLostMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class RegisterLostService {
    private final RegisterLostMapper registerLostMapper;
    private final S3FileUploadService s3FileUploadService;
    private final UserService userService;
    private final LikeMapper likeMapper;

    public RegisterLostService(final RegisterLostMapper registerLostMapper, final S3FileUploadService s3FileUploadService, final UserService userService, final LikeMapper likeMapper) {
        log.info("실종 서비스");
        this.registerLostMapper = registerLostMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.userService = userService;
        this.likeMapper = likeMapper;
    }

    //실종 공고 등록
    @Transactional
    public DefaultRes saveRegister_lost(final int userIdx, final Register_lost register_lost, final MultipartFile[] dogimg) {
        try {
            log.info("실종 공고 저장");
            User user = userService.findUser_data(userIdx);

            if (register_lost.getTel() == null) register_lost.setTel(user.getTel());
            if (register_lost.getEmail() == null) register_lost.setEmail(user.getEmail());
            if (register_lost.getDm() == null) register_lost.setDm(user.getDm());

            //registerLostMapper.save_lost(userIdx, register_lost);
            Register_lost returnedData = register_lost;
            register_lost.getRegisterIdx();

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register_lost.getRegisterIdx());

            for (int i = 0; i < dogimg.length; i++) {
                if(i == 0){
                    MultipartFile img_resize = dogimg[i];
                    String url_resize = s3FileUploadService.resizeupload(img_resize);
                    register_lost.setDogUrl(url_resize);
                    registerLostMapper.save_lost(userIdx, register_lost);
                    likeMapper.save_like_lost(userIdx, register_lost.getRegisterIdx(), register_lost.getRegisterStatus(), 0);
                } else {
                MultipartFile img = dogimg[i];
                String url = s3FileUploadService.upload(img);
                registerLostMapper.save_img_lost(register_lost.getRegisterIdx(), url, register_lost.getRegisterStatus());
                }
            }
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
                if (register_lost.getRegisteDate() != null) myRegisterLost.setRegisteDate(register_lost.getRegisteDate());
                if (register_lost.getFeature() != null) myRegisterLost.setFeature(register_lost.getFeature());
                if (register_lost.getEmail() != null) myRegisterLost.setEmail(register_lost.getEmail());
                if (register_lost.getDm() != null) myRegisterLost.setDm(register_lost.getDm());
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
    public DefaultRes search_lost(final String variety, final String protectPlace) {
        List<RegisterRes> registerLostList = registerLostMapper.search_lost(variety, protectPlace);
        if (registerLostList.isEmpty()) {
            log.info("검색 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("검색 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    //품종 검색
    @Transactional
    public DefaultRes findDogList_lost(final String variety){
        List<RegisterRes> dogList_lost = registerLostMapper.findDogList_lost(variety);

        if(dogList_lost.isEmpty()){
            log.info("원하는 품종의 리스트 없음_실종");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }

        log.info("원하는 품종의 리스트 검색 성공_실종");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_lost);
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

    //모든 공고 보여주기
//    public DefaultRes<Register_lost> viewAllRegisterLost(final int registerStatus, final int registerIdx) {
//        Register_lost registerLost = registerLostMapper.viewAllRegister_lost(registerStatus, registerIdx);
//        if (registerLost != null) {
//            try {
//                log.info("1");
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLost);
//            } catch (Exception e) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                log.info("2");
//                log.error(e.getMessage());
//                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//            }
//        }
//        log.info("3");
//        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REGISTER);
//    }
//
//    public DefaultRes<List<dogImgUrlRes>> viewAllRegisterLost_img(final int registerStatus, final int registerIdx){
//        List<dogImgUrlRes> dogImgUrl = registerLostMapper.viewAllRegisterLost_img(registerStatus, registerIdx);
//
//        if(dogImgUrl != null){
//            try{
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogImgUrl);
//            } catch (Exception e) {
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                log.error(e.getMessage());
//                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//            }
//        }
//        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_REGISTER);
//    }
    @Transactional
//    public ViewAllDetailRes<Object> viewDetail_lost(final int registerStatus, final int registerIdx){
//        Register_lost registerLost = registerLostMapper.viewAllRegister_lost(registerStatus, registerIdx);
//        List<dogImgUrlRes> dogImgUrl = registerLostMapper.viewAllRegisterLost_img(registerStatus, registerIdx);
//
//        if(registerLost != null && dogImgUrl != null){
//            try{
//                return ViewAllDetailRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLost, dogImgUrl);
//            }catch (Exception e){
//                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//                log.error(e.getMessage());
//                return ViewAllDetailRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//            }
//        }
//        return ViewAllDetailRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
//    }
    public DetailLikeRes<Object> viewDetail_lost(final int userIdx, final int registerStatus, final int registerIdx){
        Register_lost registerLost = registerLostMapper.viewAllRegister_lost(registerStatus, registerIdx);
        List<dogImgUrlRes> dogImgUrl = registerLostMapper.viewAllRegisterLost_img(registerStatus, registerIdx);
        LikeReq likeStatus = likeMapper.showStatus_lost(userIdx, registerIdx, registerStatus);

        int i = likeStatus.getLikeStatus();

        if(registerLost != null || dogImgUrl != null){
            try{
                return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLost, dogImgUrl, i);
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DetailLikeRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DetailLikeRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }
}

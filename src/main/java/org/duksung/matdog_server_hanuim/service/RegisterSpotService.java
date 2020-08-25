package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.annotations.Select;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterSpotMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@Service
public class RegisterSpotService {
    private final RegisterSpotMapper registerSpotMapper;
    private final S3FileUploadService s3FileUploadService;
    private final UserService userService;
    private final LikeMapper likeMapper;

    public RegisterSpotService(final RegisterSpotMapper registerSpotMapper, final S3FileUploadService s3FileUploadService, final UserService userService, final LikeMapper likeMapper){
        log.info("목격 서비스");
        this.registerSpotMapper = registerSpotMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.userService = userService;
        this.likeMapper = likeMapper;
    }

    //목격 공고 등록
    @Transactional
    public DefaultRes saveRegister_spot(final int userIdx, final Register_spot register_spot, final MultipartFile[] dogimg){
        try{
            log.info("목격 공고 저장");
            User user = userService.findUser_data(userIdx);

            if(register_spot.getTel() == null) register_spot.setTel(user.getTel());
            if(register_spot.getEmail() == null) register_spot.setEmail(user.getEmail());
            if(register_spot.getDm() == null) register_spot.setDm(user.getDm());

            Register_spot returnedData = register_spot;
            register_spot.getRegisterIdx();

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register_spot.getRegisterIdx());

            for(int i =0; i < dogimg.length; i++){
                if(i == 0){
                    MultipartFile img_resize = dogimg[i];
                    String url_resize = s3FileUploadService.resizeupload(img_resize);
                    register_spot.setDogUrl(url_resize);
                    registerSpotMapper.save_spot(userIdx, register_spot);
                } else {
                    MultipartFile img = dogimg[i];
                    String url = s3FileUploadService.upload(img);
                    registerSpotMapper.save_img_spot(register_spot.getRegisterIdx(), url, register_spot.getRegisterStatus());
                }
            }
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
            //gender,weight,age,protectPlace,findPlace,findDate,feature,tel,email,memo
            try{
                Register_spot myRegisterSpot = registerSpotMapper.findByRegisterIdx_spot(registerIdx);
                if(register_spot.getVariety() != null) myRegisterSpot.setVariety(register_spot.getVariety());
                myRegisterSpot.setGender(register_spot.getGender());
                myRegisterSpot.setAge(register_spot.getAge());
                if(register_spot.getProtectPlace() != null) myRegisterSpot.setProtectPlace(register_spot.getProtectPlace());
                if(register_spot.getRegisteDate() != null) myRegisterSpot.setRegisteDate(register_spot.getRegisteDate());
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

    //모든 공고 보여주기
//    public DefaultRes<Register_spot> viewAllRegister(final int registerStatus, final int registerIdx) {
//        Register_spot registerSpot = registerSpotMapper.viewAllRegister_spot(registerStatus, registerIdx);
//        if (registerSpot != null) {
//            try {
//                log.info("1");
//                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpot);
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
//    public DefaultRes<List<dogImgUrlRes>> viewAllRegister_img(final int registerStatus, final int registerIdx){
//        List<dogImgUrlRes> dogImgUrl = registerSpotMapper.viewAllRegisterSpot_img(registerStatus, registerIdx);
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
    public DetailLikeRes<Object> viewDetail_spot(final int userIdx, final int registerStatus, final int registerIdx){
        Register_spot registerSpot = registerSpotMapper.viewAllRegister_spot(registerStatus, registerIdx);
        List<dogImgUrlRes> dogImgUrl = registerSpotMapper.viewAllRegisterSpot_img(registerStatus, registerIdx);
        LikeReq likeStatus = likeMapper.showStatus_spot(userIdx, registerStatus, registerIdx);

        if(registerSpot != null && dogImgUrl != null){
            try{
                return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpot, dogImgUrl, likeStatus.getLikeStatus());
            } catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DetailLikeRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DetailLikeRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }
}

package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterSpotMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
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

            if(register_spot.getCareTel() == null) register_spot.setCareTel(user.getTel());
            if(register_spot.getEmail() == null) register_spot.setEmail(user.getEmail());
            if(register_spot.getDm() == null) register_spot.setDm(user.getDm());
            if(register_spot.getWeight() == null) register_spot.setWeight("모름");
            if(register_spot.getAge() == null) register_spot.setAge("모름");
            if(register_spot.getCareAddr() == null) register_spot.setCareAddr("없음");
            if(register_spot.getSpecialMark() == null) register_spot.setSpecialMark("없음");


            Register_spot returnedData = register_spot;
            register_spot.getRegisterIdx();

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register_spot.getRegisterIdx());

            for(int i =0; i < dogimg.length; i++){
                if(i == 0){
                    MultipartFile img_resize = dogimg[i];
                    String url_resize = s3FileUploadService.resizeupload(img_resize);
                    register_spot.setFilename(url_resize);
                    registerSpotMapper.save_spot(userIdx, register_spot);
                    likeMapper.save_like_spot(userIdx, register_spot.getRegisterIdx(), register_spot.getRegisterStatus(), 0);
                } else {
                    MultipartFile img = dogimg[i];
                    String url = s3FileUploadService.upload(img);
                    registerSpotMapper.save_img_spot(userIdx, register_spot.getRegisterIdx(), url, register_spot.getRegisterStatus());
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
            try{
                Register_spot myRegisterSpot = registerSpotMapper.findByRegisterIdx_spot(registerIdx);
                if(register_spot.getKindCd() != null) myRegisterSpot.setKindCd(register_spot.getKindCd());
                myRegisterSpot.setSexCd(register_spot.getSexCd());
                myRegisterSpot.setAge(register_spot.getAge());
                if(register_spot.getCareAddr() != null) myRegisterSpot.setCareAddr(register_spot.getCareAddr());
                if(register_spot.getHappenDt() != null) myRegisterSpot.setHappenDt(register_spot.getHappenDt());
                if(register_spot.getSpecialMark() != null) myRegisterSpot.setSpecialMark(register_spot.getSpecialMark());
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
    public DefaultRes<List<RegisterRes>> getAllRegister_spot(){
        List<RegisterRes> registerSpotList = registerSpotMapper.findAll_spot();
        if(registerSpotList.isEmpty()){
            log.info("모든 목격 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("모든 목격 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpotList);
    }

    //나이순 목격 공고 조회
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister_spot_age(){
        List<RegisterRes> registerSpotList = registerSpotMapper.findAll_spot_age();
        if(registerSpotList.isEmpty()){
            log.info("나이순 목격 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 목격 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpotList);
    }

    @Transactional
    public DefaultRes search_spot(final String keyword, final int sort){
        List<RegisterRes> dogSearch_age = registerSpotMapper.search_spot_age(keyword);
        List<RegisterRes> dogSearch_date = registerSpotMapper.search_spot_date(keyword);

        if(sort==1){
            if(dogSearch_age.isEmpty()){
                log.info("임시보호 공고 검색 없음_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("임시보호 공고 검색 성공_나이");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogSearch_age);
            }
        } else if(sort==2){
            if(dogSearch_date.isEmpty()){
                log.info("임시보호 공고 검색 없음_등록일");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("임시보호 공고 검색 성공_등록일순");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogSearch_date);
            }
        }return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_CORRECT_REQUEST);
    }

    //원하는 품종 리스트 검색
    @Transactional
    public DefaultRes findDogList_spot(final String kindCd, final int sort){
        List<RegisterRes> dogList_age = registerSpotMapper.findDogList_spot_age(kindCd);
        List<RegisterRes> dogList_date = registerSpotMapper.findDogList_spot_date(kindCd);

        if(sort == 1){
            if(dogList_age.isEmpty()){
                log.info("원하는 품종의 리스트 없음_발견_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("원하는 품종의 리스트 검색 성공_나이");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_age);
            }
        } else if(sort == 2){
            if(dogList_date.isEmpty()){
                log.info("원하는 품종의 리스트 없음_발견_등록일순");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("원하는 품종의 리스트 검색 성공_등록일순");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_date);
            }
        }
        return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_CORRECT_REQUEST);
    }

    //목격 공고 삭제
    @Transactional
    public DefaultRes deleteByRegisterIdx_spot(final int userIdx, final int registerIdx){
        final Register_spot registerSpot = registerSpotMapper.findByRegisterIdx_spot(registerIdx);
        if(registerSpot == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        else{
            try{
                log.info("목격 공고 삭제 성공");
                registerSpotMapper.deleteRegister_spot_img(userIdx, registerIdx);
                registerSpotMapper.deleteRegister_spot_like(userIdx, registerIdx);
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

    @Transactional
    public DetailLikeRes<Object> viewDetail_spot(final int userIdx, final int registerStatus, final int registerIdx){
        Register_spot registerSpot = registerSpotMapper.viewAllRegister_spot(registerStatus, registerIdx);
        dogImgUrlRes dogImgUrl = registerSpotMapper.viewAllRegisterSpot_img(registerStatus, registerIdx);
        LikeReq likeStatus = likeMapper.showStatus_spot(userIdx, registerIdx, registerStatus);

        if(registerSpot != null || dogImgUrl != null ){
            try{
                if(likeStatus == null) {
                    likeMapper.save_like_spot(userIdx, registerIdx, registerStatus, 0);
                    LikeReq now_likeStatus = likeMapper.showStatus_spot(userIdx, registerIdx, registerStatus);

                    return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerSpot, dogImgUrl, now_likeStatus.getLikeStatus());
                } else
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

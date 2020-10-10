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

    /**
     * 실종 공고 등록
     * @param userIdx
     * @param register_lost
     * @param dogimg
     * @return
     */
    @Transactional
    public DefaultRes saveRegister_lost(final int userIdx, final Register_lost register_lost, final MultipartFile[] dogimg) {
        try {
            log.info("실종 공고 저장");
            User user = userService.findUser_data(userIdx);

            if (register_lost.getCareTel() == null) register_lost.setCareTel(user.getTel());
            if (register_lost.getEmail() == null) register_lost.setEmail(user.getEmail());
            if (register_lost.getDm() == null) register_lost.setDm(user.getDm());
            if (register_lost.getWeight() == null) register_lost.setWeight("모름");
            if (register_lost.getAge() == null) register_lost.setAge("모름");
            if (register_lost.getSpecialMark() == null) register_lost.setSpecialMark("없음");

            //registerLostMapper.save_lost(userIdx, register_lost);
            Register_lost returnedData = register_lost;
            register_lost.getRegisterIdx();

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register_lost.getRegisterIdx());

            for (int i = 0; i < dogimg.length; i++) {
                if(i == 0){
                    MultipartFile img_resize = dogimg[i];
                    String url_resize = s3FileUploadService.resizeupload(img_resize);
                    register_lost.setFilename(url_resize);
                    registerLostMapper.save_lost(userIdx, register_lost);
                    likeMapper.save_like_lost(userIdx, register_lost.getRegisterIdx(), register_lost.getRegisterStatus(), 0);
                } else {
                MultipartFile img = dogimg[i];
                String url = s3FileUploadService.upload(img);
                registerLostMapper.save_img_lost(userIdx, register_lost.getRegisterIdx(), url, register_lost.getRegisterStatus());
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

    /**
     * 실종 공고 수정
     * @param userIdx
     * @param registerIdx
     * @param register_lost
     * @return
     */
    @Transactional
    public DefaultRes register_lost_update(final int userIdx, final int registerIdx, final Register_lost register_lost) {
        if (registerLostMapper.findByRegisterIdx_lost(registerIdx) != null) {
            try {
                log.info("실종 공고 수정 성공");
                Register_lost myRegisterLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
                if (myRegisterLost == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
                if (register_lost.getKindCd() != null) myRegisterLost.setKindCd(register_lost.getKindCd());
                myRegisterLost.setSexCd(register_lost.getSexCd());
                myRegisterLost.setWeight(register_lost.getWeight());
                myRegisterLost.setAge(register_lost.getAge());
                if (register_lost.getLostPlace() != null) myRegisterLost.setLostPlace(register_lost.getLostPlace());
                if (register_lost.getHappenDt() != null) myRegisterLost.setHappenDt(register_lost.getHappenDt());
                if (register_lost.getSpecialMark() != null) myRegisterLost.setSpecialMark(register_lost.getSpecialMark());
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

    /**
     * 모든 실종 공고 조회
     * @return
     */
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister_lost() {
        List<RegisterRes> registerLostList = registerLostMapper.findAll_lost();
        if (registerLostList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    /**
     * 나이순 공고 조회
     * @return
     */
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister_lost_age() {
        List<RegisterRes> registerLostList = registerLostMapper.findAll_lost_age();
        if (registerLostList.isEmpty()) {
            log.info("나이순 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLostList);
    }

    /**
     * 실종 공고 검색
     * @param keyword
     * @param sort
     * @return
     */
    @Transactional
    public DefaultRes search_lost(final String keyword, final int sort) {
        List<RegisterRes> dogSearch_age = registerLostMapper.search_lost_age(keyword);
        List<RegisterRes> dogSearch_date = registerLostMapper.search_lost_date(keyword);

        if(sort == 1){
            if(dogSearch_age.isEmpty()){
                log.info("실종 공고 검색 없음_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("실종 공고 검색 성공_나이");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogSearch_age);
            }
        } else if(sort == 2){
            if(dogSearch_date.isEmpty()){
                log.info("실종 공고 검색 없음_등록일");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else {
                log.info("실종 공고 검색 성공_등록일순");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogSearch_date);
            }
        }
        return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_CORRECT_REQUEST);
    }

    /**
     * 품종 검색
     * @param kindCd
     * @param sort
     * @return
     */
    @Transactional
    public DefaultRes findDogList_lost(final String kindCd, final int sort){
        List<RegisterRes> dogList_age = registerLostMapper.findDogList_lost_age(kindCd);
        List<RegisterRes> dogList_date = registerLostMapper.findDogList_lost_date(kindCd);

        if(sort == 1){
            if(dogList_age.isEmpty()){
                log.info("원하는 품종의 리스트 없음_나이_실종");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("원하는 품종의 리스트 검색 성공_나이_실종");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_age);
            }
        } else if(sort == 2){
            if(dogList_date.isEmpty()){
                log.info("원하는 품종의 리스트 없음_실종_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("원하는 품종의 리스트 검색 성공_실종_등록일순");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_date);
            }
        }
        return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_CORRECT_REQUEST);
    }

    /**
     * 실종 공고 삭제
     * @param userIdx
     * @param registerIdx
     * @return
     */
    @Transactional
    public DefaultRes deleteByRegisterIdx_lost(final int userIdx, final int registerIdx) {
        final Register_lost registerLost = registerLostMapper.findByRegisterIdx_lost(registerIdx);
        if (registerLost == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        else{
            try {
                registerLostMapper.deleteRegister_lost_img(userIdx, registerIdx);
                registerLostMapper.deleteRegister_lost_like(userIdx, registerIdx);
                registerLostMapper.deleteRegister_lost(userIdx, registerIdx);
                return DefaultRes.res(StatusCode.NO_CONTENT, ResponseMessage.DELETE_REGISTER);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
    }

    /**
     * 실종 공고 상세보기
     * @param userIdx
     * @param registerStatus
     * @param registerIdx
     * @return
     */
    public DetailLikeRes<Object> viewDetail_lost(final int userIdx, final int registerStatus, final int registerIdx){
        Register_lost registerLost = registerLostMapper.viewAllRegister_lost(registerStatus, registerIdx);
        dogImgUrlRes dogImgUrl = registerLostMapper.viewAllRegisterLost_img(registerStatus, registerIdx);
        LikeReq likeStatus = likeMapper.showStatus_lost(userIdx, registerIdx, registerStatus);

        if(registerLost != null || dogImgUrl != null){
            try{
                if(likeStatus == null){
                    likeMapper.save_like_lost(userIdx, registerIdx, registerStatus, 0);
                    LikeReq now_likeStatus = likeMapper.showStatus_lost(userIdx, registerIdx, registerStatus);

                    return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLost, dogImgUrl, now_likeStatus.getLikeStatus());
                } else
                    return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerLost, dogImgUrl, likeStatus.getLikeStatus());
            }catch (Exception e){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DetailLikeRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DetailLikeRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }
}

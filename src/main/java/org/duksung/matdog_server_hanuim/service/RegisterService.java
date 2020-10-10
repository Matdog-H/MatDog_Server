package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.*;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@Slf4j
@Service
public class RegisterService {
    private final RegisterMapper registerMapper;
    private final S3FileUploadService s3FileUploadService;
    private final UserService userService;
    private final LikeMapper likeMapper;

    public RegisterService(final RegisterMapper registerMapper, final S3FileUploadService s3FileUploadService, final UserService userService, final LikeMapper likeMapper) {
        log.info("분양 서비스");
        this.registerMapper = registerMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.userService = userService;
        this.likeMapper = likeMapper;
    }

    /**
     * 분양 공고 객체 반환
     *
     * @param registerIdx
     * @return DefaultRes
     */
    public DefaultRes<Register> findByRegisterIdx(final int registerIdx) {
        Register register = registerMapper.findByRegisterIdx(registerIdx);
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

    /**
     * 분양 공고 저장
     *
     * @param userIdx
     * @param register
     * @param dogimg
     * @return
     */
    @Transactional
    public DefaultRes saveRegister(final int userIdx, final Register register, final MultipartFile[] dogimg) {
        try {
            log.info("분양 공고 저장");
            User user = userService.findUser_data(userIdx);

            if (register.getCareTel() == null) register.setCareTel(user.getTel());
            if (register.getEmail() == null) register.setEmail(user.getEmail());
            if (register.getDm() == null) register.setDm(user.getDm());
            if (register.getSpecialMark() == null) register.setSpecialMark("없음");

            Register returnedData = register;
            register.getRegisterIdx();

            returnedData.setUserIdx(userIdx);
            returnedData.setRegisterIdx(register.getRegisterIdx());

            for (int i = 0; i < dogimg.length; i++) {
                if (i == 0) {
                    MultipartFile img_resize = dogimg[i];
                    String url_resize = s3FileUploadService.resizeupload(img_resize);
                    register.setFilename(url_resize);
                    registerMapper.save(userIdx, register);
                    likeMapper.save_like(userIdx, register.getRegisterIdx(), register.getRegisterStatus(), 0);
                } else {
                    MultipartFile img = dogimg[i];
                    String url = s3FileUploadService.upload(img);
                    registerMapper.save_img(userIdx, register.getRegisterIdx(), url, register.getRegisterStatus());
                }
            }
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER, returnedData);
        } catch (Exception e) {
            log.info("저장안됨");
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 보호소 공고 검색
     * @param keyword
     * @param sort
     * @return
     */
    @Transactional
    public DefaultRes search_register(final String keyword, final int sort) {
        List<RegisterRes> dogSearch_age = registerMapper.search_register_age(keyword);
        List<RegisterRes> dogSearch_date = registerMapper.search_register_date(keyword);

        if(sort == 1){
            if(dogSearch_age.isEmpty()){
                log.info("보호소 공고 검색 없음_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("보호소 공고 검색 성공_나이");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogSearch_age);
            }
        } else if(sort==2){
            if(dogSearch_date.isEmpty()){
                log.info("보호소 공고 검색 없음_등록일");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else{
                log.info("보호소 공고 검색 성공_등록일순");
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
    public DefaultRes findDogList(final String kindCd, final int sort) {
        List<RegisterRes> dogList_age = registerMapper.findDogList_age(kindCd);
        List<RegisterRes> dogList_date = registerMapper.findDogList_date(kindCd);

        if (sort == 1) {
            if (dogList_age.isEmpty()) {
                log.info("원하는 품종의 리스트 없음_나이");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
            } else {
                log.info("원하는 품종의 리스트 검색 성공_나이");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_age);
            }
        } else if (sort == 2) {

            if (dogList_date.isEmpty()) {
                log.info("원하는 품종의 리스트 없음_등록일순");
                return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);

            } else {
                log.info("원하는 품종의 리스트 검색 성공_등록일순");
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, dogList_date);
            }

        }
        return DefaultRes.res(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.NOT_CORRECT_REQUEST);
    }

    /**
     * 공고 수정
     * @param userIdx
     * @param registerIdx
     * @param register
     * @return
     */
    @Transactional
    public DefaultRes register_update(final int userIdx, final int registerIdx, final Register register) {

        if (registerMapper.findByRegisterIdx(registerIdx) != null) {
            try {
                Register myRegister = registerMapper.findByRegisterIdx(registerIdx);
                if (myRegister == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);

                if (register.getKindCd() != null) myRegister.setKindCd(register.getKindCd());
                myRegister.setSexCd(register.getSexCd());
                myRegister.setNeuterYn(register.getNeuterYn());
                myRegister.setWeight(register.getWeight());
                myRegister.setAge(register.getAge());
                if (register.getCareAddr() != null) myRegister.setCareAddr(register.getCareAddr());
                if (register.getSpecialMark() != null) myRegister.setSpecialMark(register.getSpecialMark());
                if (register.getCareTel() != null) myRegister.setCareTel(register.getCareTel());
                if (register.getEmail() != null) myRegister.setEmail(register.getEmail());
                if (register.getDm() != null) myRegister.setDm(register.getDm());

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

    /**
     * 모든 공고 조회
     * @return
     */
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister() {
        List<RegisterRes> registerList = registerMapper.findAll_register();
        if (registerList.isEmpty())
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    /**
     * 나이순 공고 조회
     * @return
     */
    @Transactional
    public DefaultRes<List<RegisterRes>> getAllRegister_age() {
        List<RegisterRes> registerList = registerMapper.findAll_age();
        if (registerList.isEmpty()) {
            log.info("나이순 공고 조회 실패");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("나이순 공고 조회 성공");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    /**
     * userIdx가 쓴 공고 반환
     * @param userIdx
     * @return
     */
    @Transactional
    public DefaultRes<List<Register>> getUserWriteRegister(final int userIdx) {
        List<Register> registerList = registerMapper.findByuserIdx(userIdx);
        if (registerList.isEmpty()) {
            log.info("유저가 쓴 분양 공고 리스트 반환");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        }
        log.info("유저가 쓴 분양 공고 리스트 반환 실패");
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, registerList);
    }

    /**
     * 공고 삭제
     * @param userIdx
     * @param registerIdx
     * @return
     */
    @Transactional
    public DefaultRes deleteByRegisterIdx(final int userIdx, final int registerIdx) {
        final Register register = registerMapper.findByRegisterIdx(registerIdx);
        if (register == null)
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
        else {
            try {
                registerMapper.deleteRegister_img(userIdx, registerIdx);
                registerMapper.deleteRegister_like(userIdx, registerIdx);
                registerMapper.deleteRegister(userIdx, registerIdx);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.DELETE_REGISTER);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
    }

    /**
     * 모든 공고 보여주기
     * @param userIdx
     * @param registerStatus
     * @param registerIdx
     * @return
     */
    public DetailLikeRes<Object> viewDetail(final int userIdx, final int registerStatus, final int registerIdx) {
        Register register = registerMapper.viewAllRegister(registerStatus, registerIdx);
        dogImgUrlRes dogImgUrl = registerMapper.viewAllRegister_img(registerStatus, registerIdx);
        LikeReq likeStatus = likeMapper.showStatus(userIdx, registerIdx, registerStatus);

        if (register != null || dogImgUrl != null) {
            try {
                if (likeStatus == null) {
                    likeMapper.save_like(userIdx, registerIdx, registerStatus, 0);
                    LikeReq now_likeStatus = likeMapper.showStatus(userIdx, registerIdx, registerStatus);

                    return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, register, dogImgUrl, now_likeStatus.getLikeStatus());
                } else
                    return DetailLikeRes.res(StatusCode.OK, ResponseMessage.READ_REGISTER, register, dogImgUrl, likeStatus.getLikeStatus());
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DetailLikeRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DetailLikeRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_REGISTER);
    }
}
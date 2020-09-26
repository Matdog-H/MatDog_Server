package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.mapper.UserMapper;
import org.duksung.matdog_server_hanuim.model.*;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

import java.util.LinkedList;
import java.util.List;

@Slf4j
@Service
public class UserService {

    private final UserMapper userMapper;
    private final S3FileUploadService s3FileUploadService;
    private final LikeMapper likeMapper;
    private final RegisterMapper registerMapper;

    /**
     * UserMapper 생성자 의존성 주입
     *
     * @param userMapper
     * @param s3FileUploadService
     */
    public UserService(final UserMapper userMapper, final S3FileUploadService s3FileUploadService, final LikeMapper likeMapper, final RegisterMapper registerMapper) {
        log.info("서비스");
        this.userMapper = userMapper;
        this.s3FileUploadService = s3FileUploadService;
        this.likeMapper = likeMapper;
        this.registerMapper = registerMapper;
    }

    /**
     * 모든 회원 조회
     *
     * @return DefaultRes
     */
    public DefaultRes getAllUsers() {
        final List<User> userList = userMapper.findAll();
        log.info("들어왔음");
        log.info(userList.toString());
        if (userList.isEmpty()) {
            log.info("비어있음");
            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
        }
        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, userList);
    }

    /**
     * ID으로 회원 조회
     *
     * @param id 아이디
     * @return DefaultRes
     */
//    public DefaultRes findById(final String id) {
//        final User user = userMapper.findById(id);
//        if (user == null) {
//            return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
//        }
//        return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
//    }
    public DefaultRes findById(final String id) {
        final User user = userMapper.findById(id);
        try{
            if (user == null) {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.NOT_FOUND_USER);
            } else {
                return DefaultRes.res(StatusCode.FORBIDDEN, ResponseMessage.ALREADY_USER);
            }
        }catch (Exception e){
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 회원 가입
     *
     * @param signUpReq 회원 데이터
     * @return DefaultRes
     */
    @Transactional
    public DefaultRes save(final SignUpReq signUpReq) {
        try {
//            if(signUpReq.getProfile() != null)
//                signUpReq.setProfileUrl(s3FileUploadService.resizeupload(signUpReq.getProfile()));
            userMapper.save(signUpReq);
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_USER);
        } catch (Exception e) {
            //Rollback
            //String data = "필수입력값을 입력하세요!";
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            log.error(e.getMessage());
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }

    /**
     * 유저 객체 반환
     *
     * @param userIdx 유저 정보
     * @return DefaultRes - User 객체
     */
    public DefaultRes findUser(final int userIdx) {
        final User user = userMapper.findByUidx(userIdx);
        if (user != null) {
            try {
                return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, user);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.BAD_REQUEST, ResponseMessage.NOT_FOUND_USER);
    }

    public User findUser_data(final int userIdx) {
        final User user = userMapper.findByUidx(userIdx);
        if (user != null) {
            try {
                return user;
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
            }
        }
        return null;
    }

    @Transactional
    public DefaultRes user_update(final int userIdx, final User user) {
        if (userMapper.findByUidx(userIdx) != null) {
            try {
                User myUser = userMapper.findByUidx(userIdx);
                if (myUser == null)
                    return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
                if (user.getName() != null) myUser.setName(user.getName());
                if (user.getAddr() != null) myUser.setAddr(user.getAddr());
                if (user.getBirth() != null) myUser.setBirth(user.getBirth());
                if (user.getTel() != null) myUser.setTel(user.getTel());
                if (user.getEmail() != null) myUser.setEmail(user.getEmail());
                if (user.getDm() != null) myUser.setDm(user.getDm());

                myUser.setDmcheck(user.getDmcheck());
                myUser.setEmailcheck(user.getEmailcheck());
                myUser.setTelcheck(user.getTelcheck());
//
//                if(signUpReq.getProfile() != null)
//                    signUpReq.setProfileUrl(s3FileUploadService.upload(signUpReq.getProfile()));

//                if (user.getProfileUrl() !=null) myUser.setProfileUrl(s3FileUploadService.upload(user.getProfile()));
                userMapper.updateUserInfo(userIdx, myUser);
                return DefaultRes.res(StatusCode.OK, ResponseMessage.UPDATE_USER);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }


//    @Transactional
//    public DefaultRes findUserLikes(final int userIdx){
//        if(userMapper.findByUidx(userIdx) != null){
//            List<Register_like> listRegisterList = likeMapper.findRegisterLikeByUserIdx(userIdx);
//            List<Register> listRegister = new LinkedList<>();
//
//            try{
//
//            }
//        }
//    }

    /**
     * ID으로 회원 조회
     *
     * @param id 아이디
     * @return DefaultRes
     */
    public DefaultRes findById_check(final String id) {
        final String checkId = userMapper.findID(id);
        if (checkId == null) {
            //없음 -> 가입가능
            return DefaultRes.res(StatusCode.OK, ResponseMessage.READ_USER, checkId);
        }
        //있음 -> 가입불가능
        return DefaultRes.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    /**
     * 유저 좋아요 공고 리스트 반환
     * @param userIdx
     * @return
     */
    @Transactional
    public ViewAllResLike<Object> findUserLikes(final int userIdx) {
        if (userMapper.findByUidx(userIdx) != null) {
            List<Register_like> registerLikeList = likeMapper.findRegisterLikeByUserIdx(userIdx);
            List<Register_like> registerLikeList_lost = likeMapper.findRegisterLikeByUserIdx_lost(userIdx);
            List<Register_like> registerLikeList_spot = likeMapper.findRegisterLikeByUserIdx_spot(userIdx);

            List<RegisterRes> registerResList = new LinkedList<>();

            String id = userMapper.findByUidx(userIdx).getId();

            for (Register_like l : registerLikeList) { //Register_like -> registerLike List로 변환
                RegisterRes registerRes = registerMapper.findByRegisterIdx_like(l.getRegisterIdx());
                if (registerRes != null) {
                    registerResList.add(registerRes);
                }
            }
            for (Register_like l2 : registerLikeList_lost) {
                RegisterRes registerRes_lost = registerMapper.findByRegisterIdx_like_lost(l2.getRegisterIdx());
                if (registerRes_lost != null) {
                    registerResList.add(registerRes_lost);
                }
            }
            for (Register_like l3 : registerLikeList_spot) {
                RegisterRes registerRes_spot = registerMapper.findByRegisterIdx_like_spot(l3.getRegisterIdx());
                if (registerRes_spot != null) {
                    registerResList.add(registerRes_spot);
                }
            }

            try {
                if (registerResList != null)
                    return ViewAllResLike.res(StatusCode.CREATED, ResponseMessage.READ_REGISTER, id, registerResList);
                return ViewAllResLike.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_REGISTER, id, registerResList);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return ViewAllResLike.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }

        return ViewAllResLike.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }

    @Transactional
    public ViewAllResLike<Object> findUserWrite(final int userIdx){
        if(userMapper.findByUidx(userIdx) != null){
            List<Register> registerWriteList = registerMapper.findAllWrite(userIdx);
            List<Register_lost> registerWriteList_lost = registerMapper.findAllWrite_lost(userIdx);
            List<Register_spot> registerWriteList_spot = registerMapper.findAllWrite_spot(userIdx);

            List<RegisterRes> registerResList = new LinkedList<>();

            String id = userMapper.findByUidx(userIdx).getId();

            for(Register l : registerWriteList){
                RegisterRes registerRes = registerMapper.findByRegisterIdx_write(l.getRegisterIdx());
                if(registerRes != null)
                    registerResList.add(registerRes);
            }
            for(Register_lost l2 : registerWriteList_lost){
                RegisterRes registerRes_lost = registerMapper.findByRegisterIdx_write_lost(l2.getRegisterIdx());
                if(registerRes_lost != null)
                    registerResList.add(registerRes_lost);
            }
            for(Register_spot l3 : registerWriteList_spot){
                RegisterRes registerRes_spot = registerMapper.findByRegisterIdx_write_spot(l3.getRegisterIdx());
                if(registerRes_spot != null)
                    registerResList.add(registerRes_spot);
            }

            try{
                if(registerResList != null)
                    return ViewAllResLike.res(StatusCode.CREATED, ResponseMessage.READ_REGISTER, id, registerResList);
                return ViewAllResLike.res(StatusCode.NO_CONTENT, ResponseMessage.NOT_FOUND_REGISTER, id, registerResList);
            } catch (Exception e) {
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error(e.getMessage());
                return ViewAllResLike.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
            }
        }
        return ViewAllResLike.res(StatusCode.NOT_FOUND, ResponseMessage.NOT_FOUND_USER);
    }
}

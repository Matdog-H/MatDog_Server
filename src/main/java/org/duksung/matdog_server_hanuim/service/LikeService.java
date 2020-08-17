package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register_like;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
public class LikeService {
    private final LikeMapper likeMapper;

    public LikeService(final LikeMapper likeMapper){
        log.info("좋아요 서비스");
        this.likeMapper = likeMapper;
    }

    @Transactional
    public DefaultRes setLike(final int userIdx, final int registerIdx, final int registerStatus, final int likeStatus){
        Register_like register_like = likeMapper.findLikeRegister(userIdx, registerIdx, registerStatus);

        try{
            if(register_like == null){
                log.info("좋아요 공고 생성");
                likeMapper.save_like(userIdx, registerIdx, registerStatus, likeStatus);
            } else{
                log.info("좋아요 공고 상태 변경");
                likeMapper.update_like(userIdx, registerIdx, registerStatus, likeStatus);
            }
            log.info("들ㅈㅁ디ㅏㄹ읜ㄷㅇ르");
            Register_like returnedData = register_like;
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LIKE_CONTENT, register_like);
        } catch (Exception e){
            log.info("좋아요 저장 실패");
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

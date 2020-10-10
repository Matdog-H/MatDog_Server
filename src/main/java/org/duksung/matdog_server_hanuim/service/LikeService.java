package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.mapper.LikeMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
public class LikeService {
    private final LikeMapper likeMapper;

    public LikeService(final LikeMapper likeMapper) {
        log.info("좋아요 서비스");
        this.likeMapper = likeMapper;
    }

    /**
     * 좋아요 상태 변경 및 생성
     * @param userIdx
     * @param registerIdx
     * @param registerStatus
     * @param likeStatus
     * @return
     */
    @Transactional
    public DefaultRes setLike(final int userIdx, final int registerIdx, final int registerStatus, final int likeStatus) {
        try {
            if (registerStatus == 1) {
                log.info("좋아요 공고 상태 변경_일반");
                likeMapper.update_like(userIdx, registerIdx, registerStatus, likeStatus);
            } else if (registerStatus == 2) {
                log.info("좋아요 공고 상태 변경_실종");
                likeMapper.update_like_lost(userIdx, registerIdx, registerStatus, likeStatus);
            } else if (registerStatus == 3) {
                log.info("좋아요 공고 상태 변경_발견");
                likeMapper.update_like_spot(userIdx, registerIdx, registerStatus, likeStatus);
            }
            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.LIKE_CONTENT);
        } catch (Exception e) {
            log.info("좋아요 저장 실패");
            log.error(e.getMessage());
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
        }
    }
}

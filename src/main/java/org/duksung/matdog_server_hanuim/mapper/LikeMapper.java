package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register_like;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import java.util.List;

@Mapper
public interface LikeMapper{
    /**
     * 좋아요 상태값 보여주기
     * @param userIdx
     * @param registerIdx
     * @param registerStatus
     * @return
     */
    @Select("SELECT likeStatus FROM register_like WHERE register_like.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT likeStatus FROM register_like_lost WHERE register_like_lost.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT likeStatus FROM register_like_spot WHERE register_like_spot.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);

    /**
     * userIdx로 좋아요 공고 찾기
     * @param userIdx
     * @return
     */
    @Select("SELECT * FROM register_like WHERE register_like.userIdx = #{userIdx} AND register_like.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_like_spot WHERE register_like_spot.userIdx = #{userIdx} AND register_like_spot.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx_spot(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_like_lost WHERE register_like_lost.userIdx = #{userIdx} AND register_like_lost.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx_lost(@Param("userIdx") final int userIdx);

    /**
     * 좋아요 공고 생성
     * @param userIdx
     * @param registerIdx
     * @param registerStatus
     * @param likeStatus
     * @return
     */
    @Insert("INSERT INTO register_like(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Insert("INSERT INTO register_like_lost(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Insert("INSERT INTO register_like_spot(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    /**
     * 좋아요 상태값 변경
     * @param userIdx
     * @param registerIdx
     * @param registerStatus
     * @param likeStatus
     * @return
     */
    @Update("UPDATE register_like set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Update("UPDATE register_like_lost set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Update("UPDATE register_like_spot set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    /**
     * 좋아요 공고 찾기
     * @param userIdx
     * @param registerIdx
     * @param registerStatus
     * @return
     */
    @Select("SELECT * FROM register_like WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT * FROM register_like_lost WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT * FROM register_like_spot WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
}

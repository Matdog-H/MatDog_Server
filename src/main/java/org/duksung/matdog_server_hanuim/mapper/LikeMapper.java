package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register_like;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import java.util.List;

@Mapper
public interface LikeMapper{
    @Select("SELECT likeStatus FROM register_like WHERE register_like.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT likeStatus FROM register_like_lost WHERE register_like_lost.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT likeStatus FROM register_like_spot WHERE register_like_spot.userIdx = #{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    LikeReq showStatus_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);


    @Select("SELECT * FROM register_like WHERE register_like.userIdx = #{userIdx} AND register_like.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_like_spot WHERE register_like_spot.userIdx = #{userIdx} AND register_like_spot.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx_spot(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_like_lost WHERE register_like_lost.userIdx = #{userIdx} AND register_like_lost.likeStatus = 1")
    List<Register_like> findRegisterLikeByUserIdx_lost(@Param("userIdx") final int userIdx);

    //생성
    @Insert("INSERT INTO register_like(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Insert("INSERT INTO register_like_lost(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Insert("INSERT INTO register_like_spot(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    //상태 변경
    @Update("UPDATE register_like set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Update("UPDATE register_like_lost set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);
    @Update("UPDATE register_like_spot set likeStatus = #{likeStatus} WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    int update_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    //좋아요 게시물 보여주기
    @Select("SELECT * FROM register_like WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT * FROM register_like_lost WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
    @Select("SELECT * FROM register_like_spot WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
}

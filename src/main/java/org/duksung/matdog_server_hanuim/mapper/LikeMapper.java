package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register_like;
import org.duksung.matdog_server_hanuim.model.LikeReq;

@Mapper
public interface LikeMapper{
    //없다면 생성?
//    @Insert("INSERT INTO register_like(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{register_like.registerIdx}, #{register_like.registerStatus}, #{register_like.likeStatus})")
//    int save_like(@Param("register_like") final Register_like register_like);
//
//    //있으면 상태변경?
//    @Update("UPDATE register_like set likeStatus = #{register_like.likeStatus}")
//    int update_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("likereq") final LikeReq likeReq);

    //생성
    @Insert("INSERT INTO register_like(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    @Insert("INSERT INTO register_like_lost(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    @Insert("INSERT INTO register_like_spot(userIdx, registerIdx, registerStatus, likeStatus) VALUES(#{userIdx}, #{registerIdx}, #{registerStatus}, #{likeStatus})")
    int save_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    //상태 변경
    @Update("UPDATE register_like set likeStatus = #{likeStatus}")
    int update_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    @Update("UPDATE register_like_lost set likeStatus = #{likeStatus}")
    int update_like_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    @Update("UPDATE register_like_spot set likeStatus = #{likeStatus}")
    int update_like_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus, @Param("likeStatus") final int likeStatus);

    //좋아요 게시물 보여주기
    @Select("SELECT * FROM register_like WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);

    @Select("SELECT * FROM register_like_lost WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);

    @Select("SELECT * FROM register_like_spot WHERE userIdx=#{userIdx} AND registerIdx=#{registerIdx} AND registerStatus=#{registerStatus}")
    Register_like findLikeRegister_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("registerStatus") final int registerStatus);
}

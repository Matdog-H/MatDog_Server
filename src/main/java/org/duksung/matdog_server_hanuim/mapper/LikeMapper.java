package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register_like;

import java.util.List;

@Mapper
public interface LikeMapper {
    @Select("SELECT * FROM register_like WHERE register_like.userIdx = #{userIdx}")
    List<Register_like> findRegisterLikeByUserIdx(@Param("userIdx") final int userIdx);
}

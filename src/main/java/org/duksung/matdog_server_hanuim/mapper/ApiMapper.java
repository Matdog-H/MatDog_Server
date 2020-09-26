package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.duksung.matdog_server_hanuim.dto.ApiDto;

@Mapper
public interface ApiMapper {
    @Insert("INSERT INTO register(userIdx, registerStatus, kindCd, sexCd, neuterYn, weight, age, orgNm, careAddr, happenDt, specialMark, careTel, email, dm, filename) " +
            "VALUES(#{userIdx}, #{api.registerStatus}, #{api.kindCd}, #{api.sexCd}, #{api.neuterYn}, #{api.weight}, " +
            "#{api.age}, #{api.orgNm}, #{api.careAddr}, #{api.happenDt}, " +
            "#{api.specialMark}, #{api.careTel}, " +
            "#{api.email}, #{api.dm}, #{api.filename})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "api.registerIdx")
    int save_api(@Param("userIdx") final int userIdx, @Param("api") final ApiDto apiDto);
}

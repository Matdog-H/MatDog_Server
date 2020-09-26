package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.plugin.Intercepts;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface RegisterSpotMapper {
    //모든 목격 공고 리스트 조회_최신순
    @Select("SELECT * FROM register_spot ORDER BY registerIdx ASC")
    List<RegisterRes> findAll_spot();

    //모든 목격 공고 리스트 조회_나이순
    @Select("SELECT * FROM register_spot ORDER BY age DESC")
    List<RegisterRes> findAll_spot_age();

    //공고 상세화면 조회
    @Select("SELECT * FROM register_spot WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_spot viewAllRegister_spot(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    @Select("SELECT popfile FROM dog_img_spot WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    dogImgUrlRes viewAllRegisterSpot_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //    //검색 기능_나이
//    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%',#{kindCd},'%') AND careAddr LIKE concat('%',#{careAddr},'%') ORDER BY age")
//    List<RegisterRes> search_spot_age(@Param("kindCd") final String kindCd, @Param("careAddr") final String careAddr);
//    //검색 기능_등록일
//    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%',#{kindCd},'%') AND careAddr LIKE concat('%',#{careAddr},'%') ORDER BY happenDt")
//    List<RegisterRes> search_spot_date(@Param("kindCd") final String kindCd, @Param("careAddr") final String careAddr);

    //검색 기능_나이
    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%',#{keyword},'%') OR careAddr LIKE concat('%',#{keyword},'%') ORDER BY age DESC")
    List<RegisterRes> search_spot_age(@Param("keyword") final String keyword);
    //검색 기능_등록일
    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%',#{keyword},'%') OR careAddr LIKE concat('%',#{keyword},'%') ORDER BY happenDt")
    List<RegisterRes> search_spot_date(@Param("keyword") final String keyword);

    //원하는 품종 리스트 검색_나이
    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%', #{kindCd}, '%') ORDER BY age")
    List<RegisterRes> findDogList_spot_age(@Param("kindCd") final String kindCd);

    //원하는 품종 리스트 검색_나이
    @Select("SELECT * FROM register_spot WHERE kindCd LIKE concat('%', #{kindCd}, '%') ORDER BY happenDt")
    List<RegisterRes> findDogList_spot_date(@Param("kindCd") final String kindCd);

    //registerIdx 조회
    @Select("SELECT * FROM register_spot WHERE registerIdx = #{registerIdx}")
    Register_spot findByRegisterIdx_spot(@Param("registerIdx") final int registerIdx);

    //목격 공고 등록
    @Insert("INSERT INTO register_spot(userIdx, registerStatus, kindCd,sexCd,weight,age,careAddr,findPlace,findDate,happenDt,specialMark,careTel,email,dm, filename)" +
            "VALUES(#{userIdx},#{re_spot.registerStatus}, #{re_spot.kindCd},#{re_spot.sexCd},#{re_spot.weight}," +
            "#{re_spot.age},#{re_spot.careAddr},#{re_spot.findPlace},#{re_spot.findDate},#{re_spot.happenDt},#{re_spot.specialMark}," +
            "#{re_spot.careTel},#{re_spot.email},#{re_spot.dm},#{re_spot.filename})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re_spot.registerIdx")
    int save_spot(@Param("userIdx") final int userIdx, @Param("re_spot") final Register_spot re_spot);

    //이미지 저장
    @Insert("INSERT INTO dog_img_spot(userIdx, registerIdx, popfile, registerStatus) VALUES(#{userIdx}, #{registerIdx}, #{popfile}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img_spot.urlIdx")
    int save_img_spot(@Param("userIdx") final int userIdx,@Param("registerIdx") final int registerIdx, @Param("popfile") final String popfile, @Param("registerStatus") final int registerStatus);

    //목격 공고 수정
    @Update("UPDATE register_spot SET kindCd=#{register_spot.kindCd},sexCd=#{register_spot.sexCd},weight=#{register_spot.weight}," +
            "age=#{register_spot.age},careAddr=#{register_spot.careAddr},findPlace=#{register_spot.findPlace},findDate=#{register_spot.findDate},happenDt=#{register_spot.happenDt}," +
            "specialMark=#{register_spot.specialMark},careTel=#{register_spot.careTel},email=#{register_spot.email},dm=#{register_spot.dm} " +
            "where userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    int update_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register_spot") Register_spot register_spot);

    //목격 공고 삭제
    @Delete("DELETE FROM register_spot WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM dog_img_spot WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_spot_img(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM register_like_spot WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_spot_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

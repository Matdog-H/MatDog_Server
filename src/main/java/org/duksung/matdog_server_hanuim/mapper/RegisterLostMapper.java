package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import java.util.List;

@Mapper
public interface RegisterLostMapper {
    //모든 실종 공고 리스트 조회(최신순 정렬)
    @Select("SELECT * FROM register_lost ORDER BY registerIdx ASC")
    List<RegisterRes> findAll_lost();

    //모든 실종 공고 리스트 조회(나이순 정렬)
    @Select("SELECT * FROM register_lost ORDER BY age DESC")
    List<RegisterRes> findAll_lost_age();

    //공고 상세 화면 조회
    @Select("SELECT * FROM register_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_lost viewAllRegister_lost(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT popfile FROM dog_img_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    dogImgUrlRes viewAllRegisterLost_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능_나이
    @Select("SELECT * FROM register_lost WHERE kindCd LIKE concat('%',#{keyword},'%') OR lostPlace LIKE concat('%',#{keyword},'%') ORDER BY age DESC")
    List<RegisterRes> search_lost_age(@Param("keyword") final String keyword);
    //검색 기능_등록일
    @Select("SELECT * FROM register_lost WHERE kindCd LIKE concat('%',#{keyword},'%') OR lostPlace LIKE concat('%',#{keyword},'%') ORDER BY happenDt")
    List<RegisterRes> search_lost_date(@Param("keyword") final String keyword);

    //원하는 품종 리스트 검색_나이
    @Select("SELECT * FROM register_lost WHERE kindCd LIKE concat('%',#{kindCd},'%') ORDER BY age")
    List<RegisterRes> findDogList_lost_age(@Param("kindCd") final String kindCd);
    //원하는 품종 리스트 검색_등록일순
    @Select("SELECT * FROM register_lost WHERE kindCd LIKE concat('%',#{kindCd},'%') ORDER BY happenDt")
    List<RegisterRes> findDogList_lost_date(@Param("kindCd") final String kindCd);

    //registerIdx 조회
    @Select("SELECT * FROM register_lost WHERE registerIdx = #{registerIdx}")
    Register_lost findByRegisterIdx_lost(@Param("registerIdx") final int registerIdx);

    //실종 공고 등록
    @Insert("INSERT INTO register_lost(userIdx, registerStatus, kindCd, sexCd, weight, age, happenDt, lostDate,lostPlace, specialMark, careTel, email, dm, filename)" +
            "VALUES(#{userIdx}, #{re_lost.registerStatus},#{re_lost.kindCd}, #{re_lost.sexCd}, #{re_lost.weight}, #{re_lost.age}, " +
            "#{re_lost.happenDt},#{re_lost.lostDate}, #{re_lost.lostPlace},#{re_lost.specialMark}, #{re_lost.careTel}, " +
            "#{re_lost.email}, #{re_lost.dm}, #{re_lost.filename})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re_lost.registerIdx")
    int save_lost(@Param("userIdx") final int userIdx, @Param("re_lost") final Register_lost re_lost);

    //이미지 저장
    @Insert("INSERT INTO dog_img_lost(userIdx, registerIdx, popfile, registerStatus) VALUES(#{userIdx}, #{registerIdx}, #{popfile}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img.popfile")
    int save_img_lost(@Param("userIdx") final int userIdx,@Param("registerIdx") final int registerIdx, @Param("popfile") final String popfile, @Param("registerStatus") final int registerStatus);

    //실종 공고 수정
    @Update("UPDATE register_lost SET kindCd=#{register_lost.kindCd}, sexCd=#{register_lost.sexCd}, weight=#{register_lost.weight},age=#{register_lost.age}," +
            "lostDate=#{register_lost.lostDate},lostPlace=#{register_lost.lostPlace},happenDt=#{register_lost.happenDt},specialMark=#{register_lost.specialMark}, " +
            "careTel=#{register_lost.careTel}, email=#{register_lost.email},dm=#{register_lost.dm} " +
            "WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void update_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register_lost") Register_lost register_lost);

    //실종 공고 삭제
    @Delete("DELETE FROM register_lost WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM dog_img_lost WHERE  userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_lost_img(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM register_like_lost WHERE  userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_lost_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

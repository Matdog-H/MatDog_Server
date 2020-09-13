package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;

import java.util.List;

@Mapper
public interface RegisterLostMapper {
    //모든 실종 공고 리스트 조회(최신순 정렬)
    @Select("SELECT * FROM register_lost ORDER BY registerIdx ASC")
    List<Register_lost> findAll_lost();

    //모든 실종 공고 리스트 조회(나이순 정렬)
    @Select("SELECT * FROM register_lost ORDER BY age DESC")
    List<Register_lost> findAll_lost_age();

    //공고 상세 화면 조회
    @Select("SELECT * FROM register_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_lost viewAllRegister_lost(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT dogUrl FROM dog_img_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    List<dogImgUrlRes> viewAllRegisterLost_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능
    @Select("SELECT * FROM register_lost WHERE variety LIKE concat('%',#{variety},'%') OR protectPlace LIKE concat('%',#{protectPlace},'%')")
    List<RegisterRes> search_lost(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //원하는 품종 리스트 검색
    @Select("SELECT * FROM register_lost WHERE variety LIKE concat('%',#{variety},'%')")
    List<RegisterRes> findDogList_lost(@Param("variety") final String variety);

    //registerIdx 조회
    @Select("SELECT * FROM register_lost WHERE registerIdx = #{registerIdx}")
    Register_lost findByRegisterIdx_lost(@Param("registerIdx") final int registerIdx);

    //실종 공고 등록
    @Insert("INSERT INTO register_lost(userIdx, registerStatus, variety, gender, weight, age, protectPlace, registeDate,lostPlace, feature, tel, email, dm, dogUrl)" +
            "VALUES(#{userIdx}, #{re_lost.registerStatus},#{re_lost.variety}, #{re_lost.gender}, #{re_lost.weight}, #{re_lost.age}, #{re_lost.protectPlace}, " +
            "#{re_lost.registeDate},#{re_lost.lostPlace},#{re_lost.feature}, #{re_lost.tel}, " +
            "#{re_lost.email}, #{re_lost.dm}, #{re_lost.dogUrl})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re_lost.registerIdx")
    int save_lost(@Param("userIdx") final int userIdx, @Param("re_lost") final Register_lost re_lost);

    @Insert("INSERT INTO register_lost(dogUrl) VALUES(#{dogUrl})")
    int url_lost(@Param("dogUrl") final String dogUrl);

    //이미지 저장
    @Insert("INSERT INTO dog_img_lost(registerIdx, dogUrl, registerStatus) VALUES(#{registerIdx}, #{dogUrl}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img.urlIdx")
    int save_img_lost(@Param("registerIdx") final int registerIdx, @Param("dogUrl") final String dogUrl, @Param("registerStatus") final int registerStatus);

    //실종 공고 수정
    @Update("UPDATE register_lost SET variety=#{register_lost.variety}, gender=#{register_lost.gender}, weight=#{register_lost.weight},age=#{register_lost.age}," +
            "protectPlace=#{register_lost.protectPlace},lostPlace=#{register_lost.lostPlace},registeDate=#{register_lost.registeDate},feature=#{register_lost.feature}, " +
            "tel=#{register_lost.tel}, email=#{register_lost.email},dm=#{register_lost.dm} " +
            "WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void update_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register_lost") Register_lost register_lost);

    //실종 공고 삭제
    @Delete("DELETE FROM register_lost WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_lost(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

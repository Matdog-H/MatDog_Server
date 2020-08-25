package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.plugin.Intercepts;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import org.springframework.stereotype.Service;

import java.util.List;

@Mapper
public interface RegisterSpotMapper {
    //모든 목격 공고 리스트 조회_최신순
    @Select("SELECT * FROM register_spot ORDER BY registerIdx ASC")
    List<Register_spot> findAll_spot();

    //모든 목격 공고 리스트 조회_나이순
    @Select("SELECT * FROM register_spot ORDER BY age DESC")
    List<Register_spot> findAll_spot_age();

    //공고 상세화면 조회
    @Select("SELECT * FROM register_spot WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_spot viewAllRegister_spot(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT dogUrl FROM dog_img_spot WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    List<dogImgUrlRes> viewAllRegisterSpot_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능
    @Select("SELECT * FROM register_spot WHERE variety LIKE concat('%',#{variety},'%') OR protectPlace LIKE concat('%',#{protectPlace},'%')")
    List<Register_spot> search_spot(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //registerIdx 조회
    @Select("SELECT * FROM register_spot WHERE registerIdx = #{registerIdx}")
    Register_spot findByRegisterIdx_spot(@Param("registerIdx") final int registerIdx);

    //목격 공고 등록
    @Insert("INSERT INTO register_spot(userIdx, registerStatus, variety,gender,weight,age,protectPlace,findPlace,registeDate,feature,tel,email,dm, dogUrl)" +
            "VALUES(#{userIdx},#{re_spot.registerStatus}, #{re_spot.variety},#{re_spot.gender},#{re_spot.weight}," +
            "#{re_spot.age},#{re_spot.protectPlace},#{re_spot.findPlace},#{re_spot.registeDate},#{re_spot.feature}," +
            "#{re_spot.tel},#{re_spot.email},#{re_spot.dm},#{re_spot.dogUrl})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re_spot.registerIdx")
    int save_spot(@Param("userIdx") final int userIdx, @Param("re_spot") final Register_spot re_spot);

    //이미지 저장
    @Insert("INSERT INTO dog_img_spot(registerIdx, dogUrl, registerStatus) VALUES(#{registerIdx}, #{dogUrl}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img_spot.urlIdx")
    int save_img_spot(@Param("registerIdx") final int registerIdx, @Param("dogUrl") final String dogUrl, @Param("registerStatus") final int registerStatus);

    //목격 공고 수정
    @Update("UPDATE register_spot SET variety=#{register_spot.variety},gender=#{register_spot.gender},weight=#{register_spot.weight}," +
            "age=#{register_spot.age},protectPlace=#{register_spot.protectPlace},findPlace=#{register_spot.findPlace},registeDate=#{register_spot.registeDate}," +
            "feature=#{register_spot.feature},tel=#{register_spot.tel},email=#{register_spot.email},dm=#{register_spot.dm} " +
            "where userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    int update_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register_spot") Register_spot register_spot);

    //목격 공고 삭제
    @Delete("DELETE FROM register_spot WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_spot(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

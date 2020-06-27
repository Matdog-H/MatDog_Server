package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.apache.ibatis.plugin.Intercepts;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
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

    //검색 기능
    @Select("SELECT * FROM register_spot WHERE variety LIKE #{variety} OR protectPlace LIKE #{protectPlace}")
    List<Register_spot> search_spot(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //registerIdx 조회
    @Select("SELECT * FROM register_spot WHERE registerIdx = #{registerIdx}")
    Register_spot findByRegisterIdx_spot(@Param("registerIdx") final int registerIdx);

    //목격 공고 등록
    @Insert("INSERT INTO register_spot(userIdx,registerIdx,variety,gender,weight,age,protectPlace,findPlace,findDate,feature,tel,email,memo)" +
            "VALUES(#{register_spot.userIdx},#{register_spot.registerIdx},#{register_spot.variety},#{register_spot.gender},#{register_spot.weight}," +
            "#{register_spot.age},#{register_spot.protectPlace},#{register_spot.findPlace},#{register_spot.findDate},#{register_spot.feature}," +
            "#{register_spot.tel},#{register_spot.email},#{register_spot.memo})")
    @Options(useGeneratedKeys = true, keyColumn = "register_spot.registerIdx")
    int save_spot(@Param("register_spot") final Register_spot register_spot);

    //목격 공고 수정
    @Update("UPDATE register_spot SET variety=#{register_spot.variety},gender=#{register_spot.gender},weight=#{register_spot.weight}," +
            "age=#{register_spot.age},protectPlace=#{register_spot.protectPlace},findPlace=#{register_spot.findPlace},findDate=#{register_spot.findDate}," +
            "feature=#{register_spot.feature},tel=#{register_spot.tel},email=#{register_spot.email},memo=#{register_spot.memo} " +
            "where registerIdx = #{registerIdx}")
    void update_spot(@Param("registerIdx") final int registerIdx, @Param("register_spot") Register_spot register_spot);

    //목격 공고 삭제
    @Delete("DELETE FROM register_spot WHERE registerIdx = #{registerIdx}")
    void deleteRegister_spot(@Param("registerIdx") final int registerIdx);
}

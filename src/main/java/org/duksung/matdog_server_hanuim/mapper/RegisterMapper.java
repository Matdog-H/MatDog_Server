package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.dto.User;

import java.util.List;

@Mapper
public interface RegisterMapper {
    //모든 분양 공고 리스트 조회(최신순 정렬)
    @Select("SELECT * FROM register ORDER BY registerIdx ASC")
    List<Register> findAll_register();

    //모든 분양 공고 리스트 조회(나이순 정렬)
    @Select("SELECT * FROM register ORDER BY age DESC")
    List<Register> findAll_age();

    //userIdx에 따른 registerIdx 조회
    @Select("SELECT * FROM register WHERE userIdx = #{userIdx}")
    List<Register> findByuserIdx(@Param("userIdx") final int userIdx);

    //registerIdx 조회
    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    Register findByRegisterIdx(@Param("registerIdx") final int registerIdx);

    //검색 기능
    @Select("SELECT * FROM register WHERE variety LIKE #{variety} OR protectPlace LIKE #{protectPlace}")
    List<Register> search_register(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, variety, gender, transGender, weight, age, protectPlace, feature, tel, email, memo) " +
            "VALUES(#{userIdx}, #{register.variety}, #{register.gender}, #{register.transGender}, #{register.weight}, " +
            "#{register.age}, #{register.protectPlace}, " +
            "#{register.feature}, #{register.tel}, " +
            "#{register.email}, #{register.memo})")
    @Options(useGeneratedKeys = true, keyColumn = "register.registerIdx")
    int save(@Param("userIdx") final int userIdx, @Param("register") final Register register);

    //분양 공고 수정
    @Update("UPDATE register SET variety=#{register.variety}, gender=#{register.gender}, transGender=#{register.transGender},weight=#{register.weight},age=#{register.age}," +
            "protectPlace=#{register.protectPlace}, feature=#{register.feature}, tel=#{register.tel}, email=#{register.email},memo=#{register.memo} " +
            "where registerIdx = #{registerIdx}")
    int update(@Param("registerIdx") final int registerIdx, @Param("register") final Register register);

    //분양 공고 삭제
    @Delete("DELETE FROM register WHERE registerIdx = #{registerIdx}")
    void deleteRegister(@Param("registerIdx") final int registerIdx);
}

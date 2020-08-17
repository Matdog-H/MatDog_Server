package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.model.RegisterRes;

import java.util.List;

@Mapper
public interface RegisterMapper {
    //모든 분양 공고 리스트 조회(최신순 정렬)
//    @Select("SELECT * FROM register ORDER BY registerIdx ASC")
//    List<Register> findAll_register();
    @Select("SELECT * FROM register ORDER BY endDate")
    List<RegisterRes> findAll_register();

    //모든 분양 공고 리스트 조회(나이순 정렬)
    @Select("SELECT * FROM register ORDER BY age DESC")
    List<Register> findAll_age();

    //userIdx에 따른 registerIdx 조회
    @Select("SELECT * FROM register WHERE userIdx = #{userIdx}")
    List<Register> findByuserIdx(@Param("userIdx") final int userIdx);

    //registerIdx 조회
    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    Register findByRegisterIdx(@Param("registerIdx") final int registerIdx);

    //공고 상세화면 보여주기
    @Select("SELECT * FROM register WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register viewAllRegister(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능 concat('%',#{userName},'%')
    @Select("SELECT * FROM register WHERE variety LIKE concat('%',#{variety},'%') OR protectPlace LIKE concat('%',#{protectPlace},'%')")
    List<Register> search_register(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, registerStatus, variety, gender, transGender, weight, age, protectPlace, endDate, feature, tel, email, memo) " +
            "VALUES(#{userIdx}, #{re.registerStatus}, #{re.variety}, #{re.gender}, #{re.transGender}, #{re.weight}, " +
            "#{re.age}, #{re.protectPlace}, #{re.endDate}, " +
            "#{re.feature}, #{re.tel}, " +
            "#{re.email}, #{re.memo})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re.registerIdx")
    int save(@Param("userIdx") final int userIdx, @Param("re") final Register re);

    //이미지 저장
    @Insert("INSERT INTO dog_img(registerIdx, dogUrl, registerStatus) VALUES(#{registerIdx}, #{dogUrl}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img.urlIdx")
    int save_img(@Param("registerIdx") final int registerIdx, @Param("dogUrl") final String dogUrl, @Param("registerStatus") final int registerStatus);

    //분양 공고 수정
    @Update("UPDATE register SET variety=#{register.variety}, gender=#{register.gender}, transGender=#{register.transGender},weight=#{register.weight},age=#{register.age}," +
            "protectPlace=#{register.protectPlace}, feature=#{register.feature}, tel=#{register.tel}, email=#{register.email},memo=#{register.memo} " +
            "where userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    int update(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register") final Register register);

    //분양 공고 삭제
    @Delete("DELETE FROM register WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

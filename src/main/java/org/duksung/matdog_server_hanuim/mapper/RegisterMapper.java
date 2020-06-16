package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.Register;
import java.util.List;

@Mapper
public interface RegisterMapper {
//    //회원 id로 조회
//    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
//    Register findBy

    //모든 공고 리스트 조회
    @Select("SELECT * FROM register")
    List<Register> findAll();

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, registerIdx, status, gender, transGender, weight, age, " +
            "protectPlace, lostDate, lostPlace, findDate, findPlace, feature, tel, email, memo, heart) " +
            "VALUES(#{register.userIdx}, #{register.registerIdx}, #{register.status}, #{register.gender}, " +
            "#{register.transGender}, #{register.weight}, #{register.age}, #{register.protectPlace}, #{register.lostDate}, " +
            "#{register.lostPlace}, #{register.findDate}, #{register.findPlace}, #{register.feature}, #{register.tel}, " +
            "#{register.email}, #{register.memo}, #{register.heart})")
    @Options(useGeneratedKeys = true, keyColumn = "register.registerIdx")
    int save(@Param("register") final Register register);
//
//    @Insert("INSERT INTO user(user_id, user_pw, user_gender) VALUES(#{user.user_id}, #{user.user_pw}, #{user.user_gender})")
//    @Options(useGeneratedKeys = true, keyColumn = "user.user_idx")
//    int save(@Param("user") final User user);

    //분양 공고 삭제
//    @Delete("DELETE FROM register WHERE registerIdx = #{registerIdx}")
//    void deleteRegister(@Param("registerIdx") final int registerIdx);
}

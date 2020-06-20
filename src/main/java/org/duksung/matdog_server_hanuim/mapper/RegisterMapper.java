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

    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    Register findByRegisterIdx(@Param("registerIdx") final int registerIdx);

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, registerIdx, variety, gender, transGender, weight, age, protectPlace, feature, tel, email, memo) " +
            "VALUES(#{register.userIdx},#{register.registerIdx}, #{register.variety}, #{register.gender}, #{register.transGender}, #{register.weight}, #{register.age}, #{register.protectPlace}, " +
            "#{register.feature}, #{register.tel}, " +
            "#{register.email}, #{register.memo})")
    @Options(useGeneratedKeys = true, keyColumn = "register.registerIdx")
    int save(@Param("register") final Register register);

    //분양 공고 수정
//    @Update("UPDATE register SET status = #{register.status}, gender = #{register.gender}, transGender=#{register.transGender}, "
//            +"weight = #{register.weight}, age = #{register.age} "
//            +"protectPlace=#{register.protectPlace}, lostDate=#{register.lostDate}, lostPlace=#{register.lostPlace},"
//            +"findDate=#{register.findDate},findPlace=#{register.findPlace},"
//            +"feature=#{register.feature},tel=#{register.tel},email=#{register.email},memo=#{register.memo},heart=#{register.heart}"
//            +"WHERE registerIdx = #{register.registerIdx}")
//    void register_update(@Param("register.registerIdx") final int registerIdx, @Param("register") final Register register);
//
//    @Insert("INSERT INTO user(user_id, user_pw, user_gender) VALUES(#{user.user_id}, #{user.user_pw}, #{user.user_gender})")
//    @Options(useGeneratedKeys = true, keyColumn = "user.user_idx")
//    int save(@Param("user") final User user);

    //분양 공고 삭제
//    @Delete("DELETE FROM register WHERE registerIdx = #{registerIdx}")
//    void deleteRegister(@Param("registerIdx") final int registerIdx);
}

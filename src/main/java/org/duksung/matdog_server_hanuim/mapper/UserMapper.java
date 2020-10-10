package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.User;
import org.duksung.matdog_server_hanuim.model.LoginReq;
import org.duksung.matdog_server_hanuim.model.SignUpReq;
import java.util.List;

@Mapper
public interface UserMapper {
    /**
     * 모든 회원 리스트 조회
     * @return
     */
    @Select("SELECT * FROM user")
    List<User> findAll();

    /**
     * 아이디 중복 조회
     * @param id
     * @return
     */
    @Select("SELECT id FROM user WHERE id = #{id}")
    String findID(@Param("id") final String id);

    /**
     * ID로 조회
     * @param id
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") final String id);

    /**
     * Useridx로 조회
     * @param userIdx
     * @return
     */
    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    User findByUidx(@Param("userIdx") final int userIdx);

    /**
     * 회원 등록, Auto Increment는 회원 고유 번호
     * Auto Increment 값을 받아오고 싶으면 리턴 타입을 int(Auto Increment 컬럼 타입)으로 하면 된다.
     * @param signUpReq
     * @return
     */
    @Insert("INSERT INTO user(id, pw, name, addr, birth, tel, telcheck, email, emailcheck, dm, dmcheck) VALUES(#{signUpReq.id}, #{signUpReq.pw}, #{signUpReq.name}, #{signUpReq.addr}, #{signUpReq.birth}, #{signUpReq.tel}, #{signUpReq.telcheck}, #{signUpReq.email}, #{signUpReq.emailcheck}, #{signUpReq.dm}, #{signUpReq.dmcheck})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save(@Param("signUpReq") final SignUpReq signUpReq);

    /**
     * 유저 객체 수정
     * @param userIdx
     * @param user
     */
    @Update("UPDATE user SET name = #{user.name}, tel = #{user.tel}, addr = #{user.addr}," +
            "birth = #{user.birth}, email = #{user.email}, dm = #{user.dm}," +
            " telcheck = #{user.telcheck}, emailcheck = #{user.emailcheck}, dmcheck = #{user.dmcheck}" +
            " WHERE userIdx = #{userIdx}")
    void updateUserInfo(@Param("userIdx") final int userIdx, @Param("user") final User user);

    /**
     * 아이디와 비밀번호로 조회
     * @param loginReq
     * @return
     */
    @Select("SELECT * FROM user WHERE id = #{loginReq.id} AND pw = #{loginReq.pw}")
    User findByIdAndPassword(@Param("loginReq") final LoginReq loginReq);
}

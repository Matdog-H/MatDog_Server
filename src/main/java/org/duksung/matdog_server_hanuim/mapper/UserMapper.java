package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.User;
import org.duksung.matdog_server_hanuim.model.LoginReq;
import org.duksung.matdog_server_hanuim.model.SignUpReq;

import java.util.List;

@Mapper
public interface UserMapper {

    //모든 회원 리스트 조회
    @Select("SELECT * FROM user")
    List<User> findAll();

    //아이디 중복 조회
    @Select("SELECT id FROM user WHERE id = #{id}")
    String findID(@Param("id") final String id);

    //ID로 조회
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") final String id);

    //Useridx로 조회
    @Select("SELECT * FROM user WHERE userIdx = #{userIdx}")
    User findByUidx(@Param("userIdx") final int userIdx);

    //@Update("UPDATE user SET memo = #{userDescriptionReq.u_description} WHERE u_idx = #{userIdx}")
    //void saveUserDescription(@Param("userIdx") final int userIdx, @Param("userDescriptionReq") final UserDescriptionReq userDescriptionReq);


    //회원 등록, Auto Increment는 회원 고유 번호
    //Auto Increment 값을 받아오고 싶으면 리턴 타입을 int(Auto Increment 컬럼 타입)으로 하면 된다.
    @Insert("INSERT INTO user(id, pw, profileUrl) VALUES(#{user.id}, #{signUpReq.name}, #{signUpReq.pw}, #{signUpReq.profileUrl})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save(@Param("signUpReq") final SignUpReq signUpReq);

    /**
     * 유저 객체 수정
     * @param userIdx
     * @param user
     */
    @Update("UPDATE user SET name = #{user.name}, tel = #{user.tel}, addr = #{user.addr}," +
            "birth = #{user.birth}, email = #{user.email}, memo = #{user.memo} WHERE userIdx = #{userIdx}")
    void updateUserInfo(@Param("userIdx") final int userIdx, @Param("user") final User user);

    // 아이디와 비밀번호로 조회
    @Select("SELECT * FROM user WHERE id =#{loginReq.id} AND pw = #{loginReq.pw}")
    User findByIdAndPassword(@Param("loginReq") final LoginReq loginReq);
}

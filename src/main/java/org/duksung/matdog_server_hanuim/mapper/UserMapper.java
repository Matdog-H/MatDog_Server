package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.User;

import java.util.List;

@Mapper
public interface UserMapper {

    //모든 회원 리스트 조회
    @Select("SELECT * FROM user")
    List<User> findAll();

    //ID으로 조회
    @Select("SELECT * FROM user WHERE id = #{id}")
    User findById(@Param("id") final String id);

    //회원 등록, Auto Increment는 회원 고유 번호
    //Auto Increment 값을 받아오고 싶으면 리턴 타입을 int(Auto Increment 컬럼 타입)으로 하면 된다.
    @Insert("INSERT INTO user(id, pw, name, addr, birth, tel, email, memo) VALUES(#{user.id}, #{user.pw}, #{user.name}, #{user.addr}, #{user.birth}, #{user.tel},#{user.email},#{user.memo})")
    @Options(useGeneratedKeys = true, keyColumn = "user.userIdx")
    int save(@Param("user") final User user);
}

package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.*;
import org.duksung.matdog_server_hanuim.dto.DogImgUrl;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.model.dogImgUrlRes;
import org.duksung.matdog_server_hanuim.model.viewAllRes;

import java.util.List;

@Mapper
public interface RegisterMapper {
    //모든 분양 공고 리스트 조회(최신순 정렬)
//    @Select("SELECT * FROM register ORDER BY registerIdx ASC")
//    List<Register> findAll_register();
    @Select("SELECT * FROM register ORDER BY registeDate")
    List<RegisterRes> findAll_register();

    /**
     * 내가 쓴 공고리스트 반환
     * @return
     */
    @Select("SELECT * FROM register WHERE userIdx = #{userIdx}")
    List<Register> findAllWrite(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_spot WHERE userIdx = #{userIdx}")
    List<Register_spot> findAllWrite_spot(@Param("userIdx") final int userIdx);
    @Select("SELECT * FROM register_lost WHERE userIdx = #{userIdx}")
    List<Register_lost> findAllWrite_lost(@Param("userIdx") final int userIdx);

    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_write(@Param("registerIdx") final int registerIdx);
    @Select("SELECT * FROM register_spot WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_write_spot(@Param("registerIdx") final int registerIdx);
    @Select("SELECT * FROM register_lost WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_write_lost(@Param("registerIdx") final int registerIdx);

    //모든 분양 공고 리스트 조회(나이순 정렬)
    @Select("SELECT * FROM register ORDER BY age DESC")
    List<Register> findAll_age();

    //userIdx에 따른 registerIdx 조회
    @Select("SELECT * FROM register WHERE userIdx = #{userIdx}")
    List<Register> findByuserIdx(@Param("userIdx") final int userIdx);

    //registerIdx 조회
    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    Register findByRegisterIdx(@Param("registerIdx") final int registerIdx);

    /**
     *
     * @param registerIdx
     * @return
     */
    @Select("SELECT * FROM register WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_like(@Param("registerIdx") final int registerIdx);
    @Select("SELECT * FROM register_spot WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_like_spot(@Param("registerIdx") final int registerIdx);
    @Select("SELECT * FROM register_lost WHERE registerIdx = #{registerIdx}")
    RegisterRes findByRegisterIdx_like_lost(@Param("registerIdx") final int registerIdx);

    //공고 상세화면 보여주기
//    @Select("SELECT * FROM register R, dog_img D WHERE R.registerStatus = #{D.registerStatus} AND R.registerIdx = #{D.registerIdx} ")
//    viewAllRes viewAllRegister(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
//    @Select("SELECT r.registerIdx, r.registerStatus, r.variety, r.gender, r.transGender, r.weight, r.age, r.protectPlace, r.endDate, r.feature,r.tel, r.email, r.memo, d.dogUrl " +
//            "FROM register r, dog_img d " +
//            "WHERE r.registerIdx = d.registerIdx=#{registerIdx} AND r.registerStatus = d.registerStatus=#{registerStatus}")
//    viewAllRes viewAllRegister(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT * FROM register WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register viewAllRegister(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT dogUrl FROM dog_img WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    List<dogImgUrlRes> viewAllRegister_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    @Select("SELECT * FROM register_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_lost viewAllRegister_lost(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT dogUrl FROM dog_img_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    List<dogImgUrlRes> viewAllRegisterLost_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능 concat('%',#{userName},'%')
    @Select("SELECT * FROM register WHERE variety LIKE concat('%',#{variety},'%') OR protectPlace LIKE concat('%',#{protectPlace},'%')")
    List<RegisterRes> search_register(@Param("variety") final String variety, @Param("protectPlace") final String protectPlace);

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, registerStatus, variety, gender, transGender, weight, age, protectPlace, registeDate, feature, tel, email, dm, dogUrl) " +
            "VALUES(#{userIdx}, #{re.registerStatus}, #{re.variety}, #{re.gender}, #{re.transGender}, #{re.weight}, " +
            "#{re.age}, #{re.protectPlace}, #{re.registeDate}, " +
            "#{re.feature}, #{re.tel}, " +
            "#{re.email}, #{re.dm}, #{re.dogUrl})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re.registerIdx")
    int save(@Param("userIdx") final int userIdx, @Param("re") final Register re);

    //이미지 저장
    @Insert("INSERT INTO dog_img(registerIdx, dogUrl, registerStatus) VALUES(#{registerIdx}, #{dogUrl}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img.urlIdx")
    int save_img(@Param("registerIdx") final int registerIdx, @Param("dogUrl") final String dogUrl, @Param("registerStatus") final int registerStatus);

    //분양 공고 수정
    @Update("UPDATE register SET variety=#{register.variety}, gender=#{register.gender}, transGender=#{register.transGender},weight=#{register.weight},age=#{register.age}," +
            "protectPlace=#{register.protectPlace}, feature=#{register.feature}, tel=#{register.tel}, email=#{register.email},dm=#{register.dm} " +
            "where userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    int update(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register") final Register register);

    //분양 공고 삭제
    @Delete("DELETE FROM register WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

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
    @Select("SELECT * FROM register ORDER BY happenDt")
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
    List<RegisterRes> findAll_age();

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
    @Select("SELECT popfile FROM dog_img WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    dogImgUrlRes viewAllRegister_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    @Select("SELECT * FROM register_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    Register_lost viewAllRegister_lost(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);
    @Select("SELECT popfile FROM dog_img_lost WHERE registerStatus = #{registerStatus} AND registerIdx = #{registerIdx}")
    List<dogImgUrlRes> viewAllRegisterLost_img(@Param("registerStatus") final int registerStatus, @Param("registerIdx") final int registerIdx);

    //검색 기능 concat('%',#{userName},'%')
    @Select("SELECT * FROM register WHERE kindCd LIKE concat('%',#{kindCd},'%') AND careAddr LIKE concat('%',#{careAddr},'%')")
    List<RegisterRes> search_register(@Param("kindCd") final String kindCd, @Param("careAddr") final String careAddr);

    //원하는 품종 리스트 검색_나이
    @Select("SELECT * FROM register WHERE kindCd LIKE concat('%', #{kindCd}, '%') ORDER BY age")
    List<RegisterRes> findDogList_age(@Param("kindCd") final String kindCd);
    //원하는 품종 리스트 검색_등록일순
    @Select("SELECT * FROM register WHERE kindCd LIKE concat('%', #{kindCd}, '%') ORDER BY happenDt")
    List<RegisterRes> findDogList_date(@Param("kindCd") final String kindCd);

    //분양 공고 등록
    @Insert("INSERT INTO register(userIdx, registerStatus, kindCd, sexCd, neuterYn, weight, age, orgNm, careAddr, happenDt, specialMark, careTel, email, dm, filename) " +
            "VALUES(#{userIdx}, #{re.registerStatus}, #{re.kindCd}, #{re.sexCd}, #{re.neuterYn}, #{re.weight}, " +
            "#{re.age}, #{re.orgNm}, #{re.careAddr}, #{re.happenDt}, " +
            "#{re.specialMark}, #{re.careTel}, " +
            "#{re.email}, #{re.dm}, #{re.filename})")
    @Options(useGeneratedKeys = true, keyColumn = "registerIdx", keyProperty = "re.registerIdx")
    int save(@Param("userIdx") final int userIdx, @Param("re") final Register re);

    //이미지 저장
    @Insert("INSERT INTO dog_img(registerIdx, popfile, registerStatus) VALUES(#{registerIdx}, #{popfile}, #{registerStatus})")
    @Options(useGeneratedKeys = true, keyColumn = "dog_img.urlIdx")
    int save_img(@Param("registerIdx") final int registerIdx, @Param("popfile") final String popfile, @Param("registerStatus") final int registerStatus);

    //분양 공고 수정
    @Update("UPDATE register SET kindCd=#{register.kindCd}, sexCd=#{register.sexCd}, neuterYn=#{register.neuterYn},weight=#{register.weight},age=#{register.age}," +
            "orgNm=#{register.orgNm}, careAddr=#{register.careAddr}, specialMark=#{register.specialMark}, careTel=#{register.careTel}, email=#{register.email},dm=#{register.dm} " +
            "where userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    int update(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx, @Param("register") final Register register);

    //분양 공고 삭제
    @Delete("DELETE FROM register WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM dog_img WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_img(@Param("registerIdx") final int registerIdx);
    @Delete("DELETE FROM register_like WHERE userIdx = #{userIdx} AND registerIdx = #{registerIdx}")
    void deleteRegister_like(@Param("userIdx") final int userIdx, @Param("registerIdx") final int registerIdx);
}

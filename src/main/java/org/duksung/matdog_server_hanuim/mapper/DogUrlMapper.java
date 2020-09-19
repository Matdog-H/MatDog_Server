package org.duksung.matdog_server_hanuim.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.duksung.matdog_server_hanuim.dto.DogImgUrl;

@Mapper
public interface DogUrlMapper {
    @Insert("INSERT INTO dog_img(registerIdx, popfile, registerStatus) VALUES(#{dogImgUrl.registerIdx}, #{dogImgUrl.popfile}, #{dogImgUrl.registerStatus})")
    int save(@Param("dogImgUrl") final DogImgUrl dogImgUrl);

}

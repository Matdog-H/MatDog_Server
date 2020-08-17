package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.DogImgUrl;
import org.duksung.matdog_server_hanuim.service.DogImgService;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class DogImgController {
    private final DogImgService dogImgService;
    private final JwtService jwtService;
    private final UserService userService;

    public DogImgController(final DogImgService dogImgService, JwtService jwtService, UserService userService)
    {
        this.dogImgService = dogImgService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

//    @PostMapping("dogimg")
//    public ResponseEntity save_img(
//            DogImgUrl dogImgUrl,
//            @RequestPart(value = "dogImg", required = false) final MultipartFile dogImg){
//        try{
//            log.info("이미지 등록 성공 12312");
//            if(dogImg != null) dogImgUrl.setDogImg(dogImg);
//            return new ResponseEntity<>(dogImgService.save(dogImgUrl), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("이미지 등록 실패 12312");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}

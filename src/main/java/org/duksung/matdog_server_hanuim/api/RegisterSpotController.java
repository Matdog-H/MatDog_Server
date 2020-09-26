package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.RegisterSpotService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterSpotController {
    private final RegisterSpotService registerSpotService;
    private final JwtService jwtService;
    private final UserService userService;

    public RegisterSpotController(RegisterSpotService registerSpotService, JwtService jwtService, UserService userService){
        log.info("목격 컨트롤러");
        this.registerSpotService = registerSpotService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    //목격 공고 등록
    @PostMapping("program/spot")
    public ResponseEntity registerNotice_spot(
            Register_spot registerSpot,
            @RequestHeader(value = "Authorization") String token,
            @RequestPart(value = "dogimg") final MultipartFile[] dogimg){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("목격 공고 등록 성공");
                return new ResponseEntity<>(registerSpotService.saveRegister_spot(userIdx, registerSpot, dogimg), HttpStatus.OK);
            } catch (Exception e){
                log.info("목격 공고 등록 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //목격 공고 수정
    @PutMapping("program/spot/{registerIdx}")
    public ResponseEntity update_register_spot(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestBody final Register_spot registerSpot){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("목격 공고 수정 성공");
                return new ResponseEntity<>(registerSpotService.register_spot_update(userIdx, registerIdx, registerSpot), HttpStatus.OK);
            } catch (Exception e){
                log.info("목격 공고 수정 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //목격 공고 삭제
    @DeleteMapping("program/spot/{registerIdx}")
    public ResponseEntity delete_spot_register(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("목격 공고 삭제 성공");
                return new ResponseEntity<>(registerSpotService.deleteByRegisterIdx_spot(userIdx, registerIdx), HttpStatus.OK);
            } catch (Exception e){
                log.info("목격 공고 삭제 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //내가 쓴 모든 공고 가져오기(목격)_최신순
    @GetMapping("program/allSpot")
    public ResponseEntity getSpotRegister(){
        try{
            log.info("모든 목격 공고 가져오기 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerSpotService.getAllRegister_spot();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("모든 목격 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //내가 쓴 모든 공고 가져오기(목격)_나이순
    @GetMapping("program/allSpotAge")
    public ResponseEntity getSpotRegister_age(){
        try{
            log.info("나이순 목격 공고 가져오기 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerSpotService.getAllRegister_spot_age();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("나이순 목격 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //목격 공고 검색
//    @GetMapping("program/spot/search/{sort}")
//    public ResponseEntity searchSpot(
//            @RequestParam(value = "kindCd", defaultValue = "") final String kindCd,
//            @RequestParam(value = "careAddr", defaultValue = "") final String careAddr,
//            @PathVariable(value = "sort") final int sort
//    ) {
//        try {
//            log.info("목격 공고 검색 성공");
//            DefaultRes<List<Register_spot>> defaultRes = registerSpotService.search_spot(kindCd, careAddr, sort);
//            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
//        } catch (Exception e) {
//            log.info("분양 검색 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("program/spot/search/{sort}")
    public ResponseEntity searchSpot(
            @RequestParam(value = "keyword", defaultValue = "") final String keyword,
            @PathVariable(value = "sort") final int sort
    ) {
        try {
            log.info("목격 공고 검색 성공");
            DefaultRes<List<Register_spot>> defaultRes = registerSpotService.search_spot(keyword, sort);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("분양 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //원하는 품종 리스트 검색
    @GetMapping("program/spot/finddog/{sort}")
    public ResponseEntity findDogList_spot(
            @RequestParam(value = "kindCd") final String kindCd,
            @PathVariable(value = "sort") final int sort){
        try{
            log.info("품종 리스트 검색 성공_발견");
            DefaultRes<List<RegisterRes>> defaultRes = registerSpotService.findDogList_spot(kindCd, sort);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch(Exception e){
            log.info("품종 리스트 검색 실패_발견");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("program/viewallspot/{registerStatus}/{registerIdx}")
    public ResponseEntity viewAll_register(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "registerStatus") final int registerStatus,
            @PathVariable(value = "registerIdx") final int registerIdx){
        int userIdx = jwtService.decode(token).getUser_idx();

        try{
            log.info("공고 상세보기 성공");
            return new ResponseEntity<>(registerSpotService.viewDetail_spot(userIdx, registerStatus, registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("공고 상세보기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

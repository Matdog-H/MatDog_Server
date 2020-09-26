package org.duksung.matdog_server_hanuim.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.RegisterLostService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterLostController {
    private final RegisterLostService registerLostService;
    //private final AuthAspect authAspect;
    private final JwtService jwtService;
    private final UserService userService;

    public RegisterLostController(RegisterLostService registerLostService, JwtService jwtService, UserService userService){
        log.info("실종 컨트롤러");
        this. registerLostService = registerLostService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    //실종 공고 등록
    @PostMapping("program/lost")
    public ResponseEntity registerNotice_lost(
            Register_lost registerLost,
            @RequestHeader(value = "Authorization") String token,
            @RequestPart(value = "dogimg") final MultipartFile[] dogimg){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("실종 공고 등록 성공");
                return new ResponseEntity<>(registerLostService.saveRegister_lost(jwtService.decode(token).getUser_idx(), registerLost, dogimg), HttpStatus.OK);
            } catch (Exception e){
                log.info("실종 공고 등록 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //실종 공고 수정
    @PutMapping("program/lost/{registerIdx}")
    public ResponseEntity update_register_lost(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestBody final Register_lost registerLost,
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("실종 공고 수정 성공");
                return new ResponseEntity<>(registerLostService.register_lost_update(userIdx, registerIdx, registerLost), HttpStatus.OK);
            } catch (Exception e){
                log.info("실종 공고 수정 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //실종 공고 삭제
    @DeleteMapping("program/lost/{registerIdx}")
    public ResponseEntity delete_lost_register(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("실종 공고 삭제 성공");
                return new ResponseEntity<>(registerLostService.deleteByRegisterIdx_lost(userIdx, registerIdx), HttpStatus.OK);
            } catch (Exception e){
                log.info("실종 공고 삭제 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //내가 쓴 모든 공고 가져오기(실종)
    @GetMapping("program/allLost")
    public ResponseEntity getLostRegister(){
        try{
            log.info("모든 실종 공고 가져오기 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerLostService.getAllRegister_lost();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("모든 실종 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //내가 쓴 모든 공고 가져오기(실종)_나이순
    @GetMapping("program/allLostAge")
    public ResponseEntity getLostRegister_age(){
        try{
            log.info("나이순 실종 공고 가져오기 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerLostService.getAllRegister_lost_age();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("나이순 실종 공고 가쟈오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //실종 공고 검색
//    @GetMapping("program/lost/search/{sort}")
//    public ResponseEntity searchLost(
//            @RequestParam(value = "kindCd") final String kindCd,
//            @RequestParam(value = "lostPlace") final String lostPlace,
//            @PathVariable(value = "sort") final int sort){
//        try{
//            log.info("실종 공고 검색 성공");
//            DefaultRes<List<RegisterRes>> defaultRes = registerLostService.search_lost(kindCd, lostPlace, sort);
//            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
//        } catch (Exception e){
//            log.info("실종 공고 검색 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("program/lost/search/{sort}")
    public ResponseEntity searchLost(
            @RequestParam(value = "keyword", defaultValue = "") final String keyword,
            @PathVariable(value = "sort") final int sort){
        try{
            log.info("실종 공고 검색 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerLostService.search_lost(keyword, sort);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("실종 공고 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("program/lost/finddog/{sort}")
    public ResponseEntity findDogList_lost(
            @RequestParam(value = "kindCd") final String kindCd,
            @PathVariable(value = "sort") final int sort){
        try{
            log.info("품종 리스트 검색 성공_실종");
            DefaultRes<List<RegisterRes>> defaultRes = registerLostService.findDogList_lost(kindCd, sort);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("품종 리스트 검색 실패_실종");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //공고 상세화면
//    @GetMapping("program/viewalllost/{registerStatus}/{registerIdx}")
//    public ResponseEntity viewAll_register(
//            @PathVariable(value = "registerStatus") final int registerStatus,
//            @PathVariable(value = "registerIdx") final int registerIdx){
//        try{
//            log.info("공고 상세보기 성공");
//            return new ResponseEntity<>(registerLostService.viewAllRegisterLost(registerStatus, registerIdx), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("공고 상세보기 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
//    @GetMapping("program/viewalllostimg/{registerStatus}/{registerIdx}")
//    public ResponseEntity viewAll_register_img(
//            @PathVariable(value = "registerStatus") final int registerStatus,
//            @PathVariable(value = "registerIdx") final int registerIdx){
//        try{
//            log.info("공고 상세보기 성공_이미지");
//            return new ResponseEntity<>(registerLostService.viewAllRegisterLost_img(registerStatus, registerIdx), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("공고 상세보기 실패_이미지");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    @GetMapping("program/viewalllost/{registerStatus}/{registerIdx}")
    public ResponseEntity viewAll_register(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "registerStatus") final int registerStatus,
            @PathVariable(value = "registerIdx") final int registerIdx){
        int userIdx = jwtService.decode(token).getUser_idx();
        try{
            log.info("공고 상세보기 성공");
            return new ResponseEntity<>(registerLostService.viewDetail_lost(userIdx, registerStatus, registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("공고 상세보기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

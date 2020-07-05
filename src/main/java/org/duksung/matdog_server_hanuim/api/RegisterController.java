package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterReq;
import org.duksung.matdog_server_hanuim.service.*;
import org.duksung.matdog_server_hanuim.utils.Auth;
import org.duksung.matdog_server_hanuim.utils.AuthAspect;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterController {
    private static final DefaultRes UNAUTHORIZED_RES = new DefaultRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);

    private final RegisterService registerService;
    private final RegisterLostService registerLostService;
    private final RegisterSpotService registerSpotService;
    //private final AuthAspect authAspect;
    private final JwtService jwtService;
    private final UserService userService;

    public RegisterController(RegisterService registerService, RegisterLostService registerLostService,
                              RegisterSpotService registerSpotService, JwtService jwtService, UserService userService) {
        log.info("분양 컨트롤러");
        this.registerService = registerService;
        this.registerLostService = registerLostService;
        this.registerSpotService = registerSpotService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    //공고 등록
    @PostMapping("program/register")
    public ResponseEntity registerNotice(
            @RequestBody final Register register,
            @RequestHeader(value = "Authorization") String token) {
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if (user.getStatus() == 200) {
            try {
                return new ResponseEntity<>(registerService.saveRegister(jwtService.decode(token).getUser_idx(), register), HttpStatus.OK);
            } catch (Exception e) {
                log.info("분양 공고 등록 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else {
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    //분양 검색
    @GetMapping("program/register/search")
    public ResponseEntity searchRegister(
            @RequestParam(value = "variety", defaultValue = "") final String variety,
            @RequestParam(value = "protectPlace", defaultValue = "") final String protectPlace
    ) {
        try {
            log.info("분양 검색 성공");
            DefaultRes<List<Register>> defaultRes = registerService.search_register(variety, protectPlace);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("분양 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //공고 수정
//    @PutMapping("program/register/{registerIdx}")
//    public ResponseEntity update_register(
//            @PathVariable(value = "registerIdx") final int registerIdx,
//            @RequestBody final Register register){
//        try{
//            return new ResponseEntity<>(registerService.register_update(registerIdx, register), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("분양 공고 수정 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
    //공고 수정
//    @Auth
//    @PutMapping("program/register")
//    public ResponseEntity update_register(
//            @RequestBody final Register register,
//            @RequestHeader(value = "Authorization") String token){
//        int registerIdx = jwtService.decode_register(token).getRegister_idx();
//        DefaultRes myRegister = registerService.findRegister(registerIdx);
//
//        if(myRegister.getStatus() == 200){
//            try{
//                log.info("분양 공고 수정 성공");
//                return new ResponseEntity<>(registerService.register_update(registerIdx, register), HttpStatus.OK);
//            } catch (Exception e){
//                log.info("분양 공고 수정 실패");
//                log.error(e.getMessage());
//                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }else {
//            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }
//    @Auth
//    @PutMapping("program/register")
//    public ResponseEntity update_register(
//            @RequestBody final Register register,
//            @RequestHeader(value = "Authorization") String token){
////        int registerIdx = jwtService.decode(token).getUser_idx();
////        DefaultRes myRegister = registerService.findRegister(registerIdx);
////        log.info(Integer.toString(registerIdx));
//
//        int userIdx = jwtService.decode(token).getUser_idx();
//        DefaultRes <List<Register>> defaultRes = registerService.getUserWriteRegister(userIdx);
//        log.info(Integer.toString(userIdx));
//
//        if(defaultRes.getStatus() == 200){
//            try{
//                log.info("분양 공고 수정 성공");
//                return new ResponseEntity<>(registerService.register_update(registerIdx, register), HttpStatus.OK);
//            } catch (Exception e){
//                log.info("분양 공고 수정 실패");
//                log.error(e.getMessage());
//                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//            }
//        }else {
//            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
//        }
//    }
    @Auth
    @PutMapping("program/register/{registerId x}")
    public ResponseEntity updateRegister(
            @RequestHeader(value = "Authorization") final String toekn,
            @PathVariable("registerIdx") final int registerIdx,
            @RequestBody Register register) {
        //DefaultRes myRegister = registerService.findByRegisterIdx(registerIdx);
        try{

            if(registerService.checkAuth(jwtService.decode(toekn).getUser_idx(), registerIdx))
                return new ResponseEntity<>(registerService.register_update(registerIdx, register), HttpStatus.OK);
            return new ResponseEntity<>(UNAUTHORIZED_RES, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    //공고 삭제
    @DeleteMapping("program/register/{registerIdx}")
    public ResponseEntity delete_register(
            @PathVariable(value = "registerIdx") final int registerIdx) {
        try {
            return new ResponseEntity<>(registerService.deleteByRegisterIdx(registerIdx), HttpStatus.OK);
        } catch (Exception e) {
            log.info("분양 공고 삭제 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * [getRegister]
     * userMapper에서 finduserIdx해서 userIdx를 가져와서 그 user가 쓴 공고를 가져온다
     * <p>
     * [getHeartRegister]
     * userMapper에서 finduserIdx해서 userIdx를 가져와서 그 user가 찜한
     */

    //내가 쓴 모든 공고 가져오기(분양)_최신순
    @GetMapping("program/allregister")
    public ResponseEntity getRegister() {
        try {
            log.info("최신순 분양 공고 가져오기 성공");
            DefaultRes<List<Register>> defaultRes = registerService.getAllRegister();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("최신순 분양 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //내가 쓴 모든 공고 가져오기(목격)_나이순
    @GetMapping("program/allregisterAge")
    public ResponseEntity getRegister_age() {
        try {
            log.info("나이순 공고 가져오기 성공");
            DefaultRes<List<Register>> defaultRes = registerService.getAllRegister_age();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("나이순 분양 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("program/all")
    public ResponseEntity getAll_register() {
        try {
            log.info("모든 공고 가져오기");
            DefaultRes<List<Register>> defaultRes = registerService.getAllRegister();
            DefaultRes<List<Register_lost>> defaultRes_lost = registerLostService.getAllRegister_lost();
            DefaultRes<List<Register_spot>> defaultRes_spot = registerSpotService.getAllRegister_spot();
            List<RegisterAll> registerAll = new ArrayList<RegisterAll>();
            //registerAll.addAll(defaultRes);


            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
            //return new ResponseEntity<>(defaultRes_lost, HttpStatus.OK);
            //return new ResponseEntity<>(defaultRes_spot, HttpStatus.OK);
        } catch (Exception e) {
            log.info("모든 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

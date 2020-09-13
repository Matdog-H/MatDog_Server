package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.*;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.RegisterReq;
import org.duksung.matdog_server_hanuim.model.RegisterRes;
import org.duksung.matdog_server_hanuim.service.*;
import org.duksung.matdog_server_hanuim.utils.Auth;
import org.duksung.matdog_server_hanuim.utils.AuthAspect;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
            Register register,
            @RequestHeader(value = "Authorization") String token,
            @RequestPart(value = "dogimg") final MultipartFile[] dogimg) {
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if (user.getStatus() == 200) {
            try {
                return new ResponseEntity<>(registerService.saveRegister(jwtService.decode(token).getUser_idx(), register, dogimg), HttpStatus.OK);
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
            DefaultRes<List<RegisterRes>> defaultRes = registerService.search_register(variety, protectPlace);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("분양 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("program/register/finddog")
    public ResponseEntity findDogList(
            @RequestParam(value = "variety") final String variety){
        try{
            log.info("품종 리스트 검색 성공");
            DefaultRes<List<RegisterRes>> defaultRes = registerService.findDogList(variety);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("품종 리스트 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 공고 수정
     * @param register
     * @param token
     * @param registerIdx
     * @return
     */
    @PutMapping("program/register/{registerIdx}")
    public ResponseEntity update_register(
            Register register,
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "registerIdx") final int registerIdx){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("분양 공고 수정 성공");
                return new ResponseEntity<>(registerService.register_update(userIdx, registerIdx, register), HttpStatus.OK);
            } catch (Exception e){
                log.info("분양 공고 수정 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }

    /**
     * 공고 삭제
     * @param registerIdx
     * @param token
     * @return
     */
    @DeleteMapping("program/register/{registerIdx}")
    public ResponseEntity delete_register(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIdx);

        if(user.getStatus() == 200){
            try{
                log.info("분양 공고 삭제 성공");
                return new ResponseEntity<>(registerService.deleteByRegisterIdx(userIdx, registerIdx), HttpStatus.OK);
            } catch (Exception e){
                log.info("분양 공고 삭제 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        } else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
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
            DefaultRes<List<RegisterRes>> defaultRes = registerService.getAllRegister();
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
            DefaultRes<List<RegisterRes>> defaultRes = registerService.getAllRegister_age();
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
            DefaultRes<List<RegisterRes>> defaultRes = registerService.getAllRegister();
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

    //공고 상세화면
//    @GetMapping("program/viewall/{registerStatus}/{registerIdx}")
//    public ResponseEntity viewAll_register(
//            @PathVariable(value = "registerStatus") final int registerStatus,
//            @PathVariable(value = "registerIdx") final int registerIdx){
//        try{
//            log.info("공고 상세보기 성공");
//            return new ResponseEntity<>(registerService.viewDetail(registerStatus, registerIdx), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("공고 상세보기 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

    @GetMapping("program/viewall/{registerStatus}/{registerIdx}")
    public ResponseEntity viewAll_register(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "registerStatus") final int registerStatus,
            @PathVariable(value = "registerIdx") final int registerIdx){
        int userIdx = jwtService.decode(token).getUser_idx();

        try{
            log.info("공고 상세보기 성공");
            return new ResponseEntity<>(registerService.viewDetail(userIdx, registerStatus, registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("공고 상세보기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

//    @GetMapping("program/viewall/{registerStatus}/{registerIdx}")
//    public ResponseEntity viewAll_register(
//            @PathVariable(value = "registerStatus") final int registerStatus,
//            @PathVariable(value = "registerIdx") final int registerIdx){
//        try{
//            log.info("공고 상세보기 성공");
//            return new ResponseEntity<>(registerService.viewAllRegister(registerStatus, registerIdx), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("공고 상세보기 실패");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }

//    @GetMapping("program/viewallimg/{registerStatus}/{registerIdx}")
//    public ResponseEntity viewAll_register_img(
//            @PathVariable(value = "registerStatus") final int registerStatus,
//            @PathVariable(value = "registerIdx") final int registerIdx){
//        try{
//            log.info("공고 상세보기 성공_이미지");
//            return new ResponseEntity<>(registerService.viewAllRegister_img(registerStatus, registerIdx), HttpStatus.OK);
//        } catch (Exception e){
//            log.info("공고 상세보기 실패_이미지");
//            log.error(e.getMessage());
//            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
//        }
//    }
}

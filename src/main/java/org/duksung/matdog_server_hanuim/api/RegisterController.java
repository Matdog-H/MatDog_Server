package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.service.RegisterService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
import java.util.List;
import java.util.Optional;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterController {
    private final RegisterService registerService;

    public RegisterController(RegisterService registerService) {
        log.info("분양 컨트롤러");
        this.registerService = registerService;
    }

    //공고 등록
    @PostMapping("program/register")
    public ResponseEntity registerNotice(@RequestBody final Register register) {
        try {
            return new ResponseEntity<>(registerService.saveRegister(register), HttpStatus.OK);
            //return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.info("분양 공고 등록 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //공고 수정
    @PutMapping("program/register/{registerIdx}")
    public ResponseEntity update_register(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestBody final Register register){
        try{
            return new ResponseEntity<>(registerService.register_update(registerIdx, register), HttpStatus.OK);
        } catch (Exception e){
            log.info("분양 공고 수정 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //공고 삭제
    @DeleteMapping("program/register/{registerIdx}")
    public ResponseEntity delete_register(
            @PathVariable(value = "registerIdx") final int registerIdx){
        try{
            return new ResponseEntity<>(registerService.deleteByRegisterIdx(registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("분양 공고 삭제 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     [getRegister]
     userMapper에서 finduserIdx해서 userIdx를 가져와서 그 user가 쓴 공고를 가져온다

     [getHeartRegister]
     userMapper에서 finduserIdx해서 userIdx를 가져와서 그 user가 찜한
     */
    //찜 공고 가져오기
//    @GetMapping("/program/heart")
//    public ResponseEntity getHeartRegister(@RequestParam("userIdx") final Optional<Integer> userIdx){
//        try{
//
//        }catch (Exception e){
//
//        }
//    }

    //내가 쓴 모든 공고 가져오기(분양)
    @GetMapping("program/allregister")
    public ResponseEntity getRegister(){
        try{
            DefaultRes<List<Register>> defaultRes = registerService.getAllRegister();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

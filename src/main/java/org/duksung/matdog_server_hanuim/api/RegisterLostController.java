package org.duksung.matdog_server_hanuim.api;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register_lost;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.service.RegisterLostService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterLostController {
    private final RegisterLostService registerLostService;

    public RegisterLostController(RegisterLostService registerLostService){
        log.info("실종 컨트롤러");
        this. registerLostService = registerLostService;
    }

    //실종 공고 등록
    @PostMapping("program/lost")
    public ResponseEntity registerNotice_lost(@RequestBody final Register_lost registerLost){
        try{
            return new ResponseEntity<>(registerLostService.saveRegister_lost(registerLost), HttpStatus.OK);
        } catch (Exception e){
            log.info("실종 공고 등록 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //실종 공고 수정
    @PutMapping("program/lost/{registerIdx}")
    public ResponseEntity update_register_lost(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestBody final Register_lost registerLost){
        try{
            log.info("실종 공고 수정 성공");
            return new ResponseEntity<>(registerLostService.register_lost_update(registerIdx, registerLost), HttpStatus.OK);
        } catch (Exception e){
            log.info("실종 공고 수정 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //실종 공고 삭제
    @DeleteMapping("program/lost/{registerIdx}")
    public ResponseEntity delete_lost_register(
            @PathVariable(value = "registerIdx") final int registerIdx){
        try{
            return new ResponseEntity<>(registerLostService.deleteByRegisterIdx_lost(registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("실종 공고 삭제 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //내가 쓴 모든 공고 가져오기(실종)
    @GetMapping("program/allLost")
    public ResponseEntity getLostRegister(){
        try{
            DefaultRes<List<Register_lost>> defaultRes = registerLostService.getAllRegister_lost();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("모든 실종 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

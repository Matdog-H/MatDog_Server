package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.Register_spot;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.service.RegisterSpotService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class RegisterSpotController {
    private final RegisterSpotService registerSpotService;

    public RegisterSpotController(RegisterSpotService registerSpotService){
        log.info("목격 컨트롤러");
        this.registerSpotService = registerSpotService;
    }

    //목격 공고 등록
    @PostMapping("program/spot")
    public ResponseEntity registerNotice_spot(@RequestBody final Register_spot registerSpot){
        try{
            log.info("목격 공고 등록 성공");
            return new ResponseEntity<>(registerSpotService.saveRegister_spot(registerSpot), HttpStatus.OK);
        } catch (Exception e){
            log.info("목격 공고 등록 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //목격 공고 수정
    @PutMapping("program/spot/{registerIdx}")
    public ResponseEntity update_register_spot(
            @PathVariable(value = "registerIdx") final int registerIdx,
            @RequestBody final Register_spot registerSpot){
        try{
            log.info("목격 공고 수정 성공");
            return new ResponseEntity<>(registerSpotService.register_spot_update(registerIdx, registerSpot), HttpStatus.OK);
        } catch (Exception e){
            log.info("목격 공고 수정 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //목격 공고 삭제
    @DeleteMapping("program/spot/{registerIdx}")
    public ResponseEntity delete_spot_register(
            @PathVariable(value = "registerIdx") final int registerIdx){
        try{
            log.info("목격 공고 삭제 성공");
            return new ResponseEntity<>(registerSpotService.deleteByRegisterIdx_spot(registerIdx), HttpStatus.OK);
        } catch (Exception e){
            log.info("목격 공고 삭제 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //내가 쓴 모든 공고 가져오기(목격)_최신순
    @GetMapping("program/allSpot")
    public ResponseEntity getSpotRegister(){
        try{
            log.info("모든 목격 공고 가져오기 성공");
            DefaultRes<List<Register_spot>> defaultRes = registerSpotService.getAllRegister_spot();
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
            DefaultRes<List<Register_spot>> defaultRes = registerSpotService.getAllRegister_spot_age();
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("나이순 목격 공고 가져오기 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //목격 공고 검색
    @GetMapping("program/spot/search")
    public ResponseEntity searchSpot(
            @RequestParam(value = "variety") final String variety,
            @RequestParam(value = "protectPlace") final String protectPlace
    ){
        try{
            log.info("목격 공고 검색 성공");
            DefaultRes<List<Register_spot>> defaultRes = registerSpotService.search_spot(variety, protectPlace);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e){
            log.info("분양 검색 실패");
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

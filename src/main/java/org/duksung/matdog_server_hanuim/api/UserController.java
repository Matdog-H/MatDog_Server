package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.User;
import org.duksung.matdog_server_hanuim.dto.ViewAllResLike;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.SignUpReq;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;
    private final JwtService jwtService;

    public UserController(final UserService userService, JwtService jwtService) {
        this.userService = userService;
        this.jwtService = jwtService;
    }

    /**
     * 회원가입
     * @param signUpReq
     * @return
     */
    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody final SignUpReq signUpReq) {
        try {
            return new ResponseEntity<>(userService.save(signUpReq), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 내 정보 가져오기
     * @param token
     * @return
     */
    @GetMapping("/my")
    public ResponseEntity getMyData(
            @RequestHeader(value = "Authorization") String token){
        int userIdx = jwtService.decode(token).getUser_idx();

        try{
            DefaultRes defaultRes = userService.findUser(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        }catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 회원 정보 수정
     * @param token
     * @param user
     * @return
     */
    @PutMapping("")
    public ResponseEntity update_user(
            @RequestHeader(value = "Authorization") String token,
            @RequestBody final User user) {
        try {
            int userIdx = jwtService.decode(token).getUser_idx();

            return new ResponseEntity<>(userService.user_update(userIdx, user), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 아이디 중복검사
     * @param id
     * @return
     */
    @PostMapping("/check/{id}")
    public ResponseEntity idCheck(
            @PathVariable(value = "id") final String id) {
        DefaultRes defaultRes = userService.findById(id);
        return new ResponseEntity<>(defaultRes, HttpStatus.OK);
    }

    /**
     * 찜공고 리스트 조회
     * @param token
     * @return
     */
    @GetMapping("likes")
    public ResponseEntity getUserLike(
            @RequestHeader(value = "Authorization") String token) {
        int userIdx = jwtService.decode(token).getUser_idx();
        try {
            ViewAllResLike defaultRes = userService.findUserLikes(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * 내가 쓴 공고 리스트 조회
     * @param token
     * @return
     */
    @GetMapping("write")
    public ResponseEntity getUserWrite(
            @RequestHeader(value = "Authorization") String token) {
        int userIdx = jwtService.decode(token).getUser_idx();
        try {
            ViewAllResLike defaultRes = userService.findUserWrite(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.Register;
import org.duksung.matdog_server_hanuim.dto.User;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.UserSignUpReq;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.duksung.matdog_server_hanuim.utils.Auth;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
@RequestMapping("user")
public class UserController {

    private final UserService userService;

    public UserController(final UserService userService) {
        this.userService = userService;
    }

    @Auth
    @GetMapping("")
    public ResponseEntity getUser(@RequestParam("id") final Optional<String> id) {
        try {
            //id가 null일 경우 false, null이 아닐 경우 true
            if (id.isPresent()) return new ResponseEntity<>(userService.findById(id.get()), HttpStatus.OK);
            return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/signup")
    public ResponseEntity signup(@RequestBody final User user) {
        try {
            return new ResponseEntity<>(userService.save(user), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * @param userIdx
     * @return User 객체
     */
    @GetMapping("/{userIdx}")
    public ResponseEntity getUserInfo(
            @PathVariable("userIdx") final int userIdx) {
        try {
            DefaultRes defaultRes = userService.findUser(userIdx);
            return new ResponseEntity<>(defaultRes, HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /* 회원 정보 수정 */

    @PutMapping("/{userIdx}")
    public ResponseEntity update_user(
            @PathVariable(value = "userIdx") final int userIdx,
            @RequestBody final User user) {
        try {
            return new ResponseEntity<>(userService.user_update(userIdx, user), HttpStatus.OK);
        } catch (Exception e) {
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    //아이디 중복검사
    @PostMapping("/check/{id}")
    public ResponseEntity idCheck(
            @PathVariable(value = "id") final String id) {
        DefaultRes defaultRes = userService.findById(id);
        return new ResponseEntity<>(defaultRes, HttpStatus.OK);
    }
    // change
}
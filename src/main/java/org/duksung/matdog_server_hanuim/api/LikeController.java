package org.duksung.matdog_server_hanuim.api;

import lombok.Builder;
import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.model.LikeReq;
import org.duksung.matdog_server_hanuim.service.JwtService;
import org.duksung.matdog_server_hanuim.service.LikeService;
import org.duksung.matdog_server_hanuim.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@RestController
@Slf4j
public class LikeController {
    private final LikeService likeService;
    private final JwtService jwtService;
    private final UserService userService;

    public LikeController(LikeService likeService, JwtService jwtService, UserService userService){
        log.info("좋아요 컨트롤러");
        this.likeService = likeService;
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @PostMapping("like/{registerIdx}/{registerStatus}/{likeStatus}")
    public ResponseEntity like(
            @RequestHeader(value = "Authorization") String token,
            @PathVariable(value = "likeStatus") final int likeStatus,
            @PathVariable(value = "registerIdx") final int registerIdx,
            @PathVariable(value = "registerStatus") final int registerStatus){
        int userIndex = jwtService.decode(token).getUser_idx();
        DefaultRes user = userService.findUser(userIndex);

        if(user.getStatus() == 200){
            try{
                log.info("좋아요 성공");
                return new ResponseEntity<>(likeService.setLike(userIndex, registerIdx, registerStatus, likeStatus), HttpStatus.OK);
            } catch (Exception e){
                log.info("좋아요 실패");
                log.error(e.getMessage());
                return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }  else{
            return new ResponseEntity(FAIL_DEFAULT_RES, HttpStatus.UNAUTHORIZED);
        }
    }
}

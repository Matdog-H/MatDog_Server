package org.duksung.matdog_server_hanuim.api;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.service.DogImgService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import static org.duksung.matdog_server_hanuim.model.DefaultRes.FAIL_DEFAULT_RES;

@Slf4j
@RestController
public class DogImgController {
    private final DogImgService dogImgService;

    public DogImgController(DogImgService dogImgService){
        this.dogImgService = dogImgService;
    }
    /**
     * 품종분석을 위한 이미지 전달
     * @param dogimg
     * @return
     */
    @PostMapping("imgTransfer")
    public ResponseEntity dogImgTransfer(
            @RequestPart(value = "dogimg") final MultipartFile dogimg){
        try{
            return new ResponseEntity<>(dogImgService.dogImgTransfer(dogimg), HttpStatus.OK);
        }catch (Exception e){
            log.error(e.getMessage());
            return new ResponseEntity<>(FAIL_DEFAULT_RES, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

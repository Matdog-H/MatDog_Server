package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.mapper.DogUrlMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;

@Service
@Slf4j
public class DogImgService {
    private final S3FileUploadService s3FileUploadService;

    public DogImgService(S3FileUploadService s3FileUploadService) {
        log.info("서비스");
        this.s3FileUploadService=s3FileUploadService;
    }

    //공고 이미지 등록
    @Transactional
    public DefaultRes dogImgTransfer(final MultipartFile dogimg) throws IOException {
        String url =  s3FileUploadService.dogImgTransfer(dogimg);
        return DefaultRes.res(StatusCode.OK, ResponseMessage.IMG_TRANSFER, url);
    }
}

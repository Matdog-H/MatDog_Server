package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.dto.DogImgUrl;
import org.duksung.matdog_server_hanuim.mapper.DogUrlMapper;
import org.duksung.matdog_server_hanuim.mapper.RegisterMapper;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

@Service
@Slf4j
public class DogImgService {
    private final DogUrlMapper dogUrlMapper;
    private final RegisterMapper registerMapper;
    private final S3FileUploadService s3FileUploadService;

    public DogImgService(final DogUrlMapper dogUrlMapper, final RegisterMapper registerMapper, final S3FileUploadService s3FileUploadService) {
        log.info("서비스");
        this.dogUrlMapper = dogUrlMapper;
        this.registerMapper = registerMapper;
        this.s3FileUploadService=s3FileUploadService;
    }

    //공고 이미지 등록
//    @Transactional
//    public DefaultRes save(DogImgUrl dogImgUrl){
//       // int registerIdx = registerMapper.save()
//        try{
//            if(dogImgUrl.getDogImg() != null)
//                dogImgUrl.setDogUrl(s3FileUploadService.upload(dogImgUrl.getDogImg()));
//            dogUrlMapper.save(dogImgUrl);
//            log.info("공고 이미지 등록 성공");
//            return DefaultRes.res(StatusCode.CREATED, ResponseMessage.CREATED_REGISTER_IMG);
//        } catch (Exception e){
//            log.info("공고 이미지 등록 실패");
//            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
//            log.error(e.getMessage());
//            return DefaultRes.res(StatusCode.DB_ERROR, ResponseMessage.DB_ERROR);
//        }
//    }
}

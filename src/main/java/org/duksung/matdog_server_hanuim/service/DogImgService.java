package org.duksung.matdog_server_hanuim.service;

import lombok.extern.slf4j.Slf4j;
import org.duksung.matdog_server_hanuim.model.DefaultRes;
import org.duksung.matdog_server_hanuim.utils.ResponseMessage;
import org.duksung.matdog_server_hanuim.utils.StatusCode;
import org.python.util.PythonInterpreter;
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
        this.s3FileUploadService = s3FileUploadService;
    }

    /**
     * 강아지 객체인식
     *
     * @param dogimg
     * @return
     * @throws IOException
     */
    @Transactional
    public DefaultRes dogImgTransfer(final MultipartFile dogimg) throws IOException {
        String url = s3FileUploadService.dogImgTransfer(dogimg);

        System.out.println("1");
        PythonInterpreter interpreter = new PythonInterpreter();
        System.out.println(interpreter);
        System.out.println("222222");
        interpreter.execfile("Untitled.py");
        System.out.println("6");
        interpreter.exec("print(url_read(url))");
        System.out.println("7");


        return DefaultRes.res(StatusCode.OK, ResponseMessage.IMG_TRANSFER, url);
    }
}

package net.shop.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileService {

    /**
     * @param file
     * @return 文件最终存储的地址
     */
    String  uploadUserImg(MultipartFile file) throws IOException;
}

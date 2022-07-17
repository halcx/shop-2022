package net.shop.service.impl;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSBuilder;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectResult;
import lombok.extern.slf4j.Slf4j;
import net.shop.config.OSSConfig;
import net.shop.service.FileService;
import net.shop.utils.CommonUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class FileServiceImpl implements FileService {

    @Autowired
    private OSSConfig ossConfig;

    @Override
    public String uploadUserImg(MultipartFile file){
        String bucketname = ossConfig.getBucketname();
        String endpoint = ossConfig.getEndpoint();
        String accessKeyId = ossConfig.getAccessKeyId();
        String accessKeySecret = ossConfig.getAccessKeySecret();

        //创建一个oss客户端对象
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret);

        //获取原始文件名 xxx.png
        String originalFilename = file.getOriginalFilename();

        //jdk8的日期格式化来初始化文件夹
        LocalDateTime now = LocalDateTime.now();
        //格式化模版
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
        //拿到了文件夹
        String folder = now.format(dateTimeFormatter);

        //拼装oss路径    2022/12/1/guiasguodsgfoas.png
        String fileName = CommonUtils.generateUUID();
        String extensionName = originalFilename.substring(originalFilename.lastIndexOf("."));
        //在oss上面创建user文件夹
        String newFileName = "user/"+folder+"/"+fileName+extensionName;

        try {
            PutObjectResult putObjectResult = ossClient.putObject(bucketname, newFileName, file.getInputStream());
            //拼装返回路径
            if(putObjectResult!=null){
                String imgUrl = "https://" + bucketname + "." + endpoint + "/" + newFileName;
                return imgUrl;
            }
        } catch (IOException e) {
            log.error("文件上传失败:{}",e);
        }finally {
            //关闭服务，否则可能造成内存泄漏
            ossClient.shutdown();
        }


        return null;
    }
}

package net.shop.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import net.shop.enums.BizCodeEnum;
import net.shop.request.UserLoginRequest;
import net.shop.request.UserRegisterRequest;
import net.shop.service.FileService;
import net.shop.service.UserService;
import net.shop.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author Wang Xiaohan
 * @since 2022-07-11
 */
@Api(tags = "用户模块")
@RestController
@RequestMapping("/api/user/v1")
public class UserController {

    @Autowired
    private FileService fileService;

    @Autowired
    private UserService userService;

    /**
     * 上传用户头像
     * @param file 文件 默认最大为1mb
     * @return
     */
    @ApiOperation("用户头像上传")
    @PostMapping(value = "/upload")
    public JsonData uploadUsrImg(
            @ApiParam(value = "文件上传",required = true)
            @RequestPart("file") MultipartFile file) throws IOException {

        String result = fileService.uploadUserImg(file);
        return result!=null? JsonData.buildSuccess(result):JsonData.buildResult(BizCodeEnum.FILE_UPLOAD_USER_IMG_FAIL);

    }

    @ApiOperation("用户注册")
    @PostMapping("/register")
    public JsonData register(@ApiParam("用户注册对象") @RequestBody UserRegisterRequest registerRequest){
        return userService.register(registerRequest);
    }

    /**
     * 用户登陆
     * @return
     */
    @ApiOperation("用户登录")
    @PostMapping("/login")
    public JsonData login(@ApiParam("用户登陆对象")@RequestBody UserLoginRequest loginRequest){
        JsonData jsonData = userService.login(loginRequest);
        return jsonData;
    }

}


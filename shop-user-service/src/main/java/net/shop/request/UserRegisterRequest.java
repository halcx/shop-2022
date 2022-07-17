package net.shop.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 用户注册类
 */
@Data
@ApiModel(value = "用户注册对象",description = "用户注册请求对象")
public class UserRegisterRequest {

    @ApiModelProperty(value = "昵称",example = "老王")
    private String name;

    @ApiModelProperty(value = "密码",example = "1234")
    private String pwd;

    @ApiModelProperty(value = "头像",example = "https://shop-user-service-2022-img.oss-cn-hangzhou.aliyuncs.com/user/2022/07/17/a7c2917708b34b2faebbb576d9081620.png")
    @JsonProperty("head_img")
    private String headImg;

    @ApiModelProperty(value = "个性签名",example = "老王top1")
    private String slogan;

    @ApiModelProperty(value = "性别",example = "0")
    private Integer sex;

    @ApiModelProperty(value = "邮箱",example = "1278571538@qq.com")
    private String mail;

    @ApiModelProperty(value = "验证码",example = "1234")
    private String code;

}

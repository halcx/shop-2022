package net.shop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginUser {
    /**
     * 主键
     */
    private Long id;
    private String name;
    @JsonProperty(value = "head_img")
    private String headImg;
    private String mail;

}

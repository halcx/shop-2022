package net.shop.service;

import net.shop.request.UserLoginRequest;
import net.shop.request.UserRegisterRequest;
import net.shop.utils.JsonData;
import net.shop.vo.UserVO;

public interface UserService {
    JsonData register(UserRegisterRequest registerRequest);

    JsonData login(UserLoginRequest loginRequest);

    UserVO findUserDetail();
}

package net.shop.service;

import net.shop.request.UserRegisterRequest;
import net.shop.utils.JsonData;

public interface UserService {
    JsonData register(UserRegisterRequest registerRequest);
}
package net.shop.constent;

public class CacheKey {

    /**
     *  模版化：第一个是类型，比如是修改密码的还是注册的，第二个是接收的号码
     */
    public static final String CHECK_CODE_KEY = "code:%s:%s";
}

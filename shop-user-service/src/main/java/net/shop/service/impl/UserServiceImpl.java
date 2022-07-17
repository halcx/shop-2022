package net.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.shop.enums.BizCodeEnum;
import net.shop.enums.SendCodeEnum;
import net.shop.mapper.UserMapper;
import net.shop.model.UserDO;
import net.shop.request.UserRegisterRequest;
import net.shop.service.NotifyService;
import net.shop.service.UserService;
import net.shop.utils.CommonUtils;
import net.shop.utils.JsonData;
import org.apache.commons.codec.digest.Md5Crypt;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {

    @Autowired
    private NotifyService notifyService;

    @Autowired
    private UserMapper userMapper;

    /**
     * * 邮箱验证码验证
     * * 密码加密（TODO）
     * * 账号唯一性检查(TODO)
     * * 插入数据库
     * * 新注册用户福利发放(TODO)
     * @param registerRequest
     * @return
     */
    @Override
    public JsonData register(UserRegisterRequest registerRequest) {
        boolean checkCode = false;
        if(StringUtils.isNotBlank(registerRequest.getCode())){
            checkCode = notifyService.checkCode(SendCodeEnum.USER_REGISTER, registerRequest.getMail(), registerRequest.getCode());
        }
        if(!checkCode){
            return JsonData.buildResult(BizCodeEnum.CODE_ERROR);
        }

        //包装对象
        UserDO userDO = new UserDO();
        //根据属性名称拷贝
        BeanUtils.copyProperties(registerRequest,userDO);
        userDO.setCreateTime(new Date());
        userDO.setSlogan("test slogan");
        //设置密码 加密
        //生成密钥，也就是盐
        userDO.setSecret("$1$"+ CommonUtils.getStringNumRandom(8));

        //密码+盐处理
        String cryptPwd  = Md5Crypt.md5Crypt(registerRequest.getPwd().getBytes(StandardCharsets.UTF_8),userDO.getSecret());
        userDO.setPwd(cryptPwd);

        //账号唯一性检查 TODO
        if(checkUnique(userDO.getMail())){
            int rows = userMapper.insert(userDO);
            log.info("rows:{},注册成功:{}",rows,userDO.toString());
            userRegisterInitTask(userDO);
            return JsonData.buildSuccess();
        }
        else {
            return JsonData.buildResult(BizCodeEnum.ACCOUNT_REPEAT);
        }
    }

    /**
     * 账号唯一性检查
     * @param mail
     * @return
     */
    private boolean checkUnique(String mail) {
        QueryWrapper<UserDO> queryWrapper = new QueryWrapper<UserDO>().eq("mail", mail);
        List<UserDO> userDOS = userMapper.selectList(queryWrapper);

        return userDOS.size()>0?false:true;

    }

    /**
     * 用户注册，初始化福利信息等 TODO
     * @param userDO
     */
    private void userRegisterInitTask(UserDO userDO){

    }
}

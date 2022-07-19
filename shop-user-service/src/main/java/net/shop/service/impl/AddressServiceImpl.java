package net.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import net.shop.enums.AddressStatusEnum;
import net.shop.intercepter.LoginInterceptor;
import net.shop.mapper.AddressMapper;
import net.shop.model.AddressDO;
import net.shop.model.LoginUser;
import net.shop.request.AddressRequest;
import net.shop.service.AddressService;
import net.shop.vo.AddressVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
@Slf4j
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressVO detail(Long id) {
        AddressDO addressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id", id));
        if(addressDO==null){
            return null;
        }
        AddressVO addressVO = new AddressVO();
        BeanUtils.copyProperties(addressDO,addressVO);
        return addressVO;
    }

    /**
     * 新增收货地址
     * @param addressRequest
     * @return
     */
    @Override
    public void add(AddressRequest addressRequest) {
        LoginUser loginUser = LoginInterceptor.threadLocal.get();
        AddressDO addressDO = new AddressDO();
        addressDO.setCreateTime(new Date());
        addressDO.setUserId(loginUser.getId());
        BeanUtils.copyProperties(addressRequest,addressDO);

        //判断是否有默认收货地址
        if(addressDO.getDefaultStatus()==AddressStatusEnum.DEFAULT_STATUS.getStatus()){
            //查找数据库是否有默认收货地址
            //假如有默认收货地址的话，要把之前的改为0
            AddressDO defaultAddress = addressMapper.selectOne(new QueryWrapper<AddressDO>()
                    .eq("user_id", loginUser.getId())
                    .eq("default_status", AddressStatusEnum.DEFAULT_STATUS.getStatus()));
            if (defaultAddress!=null){
                defaultAddress.setDefaultStatus(AddressStatusEnum.COMMON_STATUS.getStatus());
                addressMapper.update(defaultAddress,new QueryWrapper<AddressDO>().eq("id",defaultAddress.getId()));
            }


        }
        int insert = addressMapper.insert(addressDO);
        log.info("新增收货地址:rows={},data={}",insert,addressDO);
    }

    /**
     * 根据ID删除地址
     * @param addressId
     * @return
     */
    @Override
    public int delete(int addressId) {
        int rows = addressMapper.delete(new QueryWrapper<AddressDO>().eq("id", addressId));
        return rows;
    }
}

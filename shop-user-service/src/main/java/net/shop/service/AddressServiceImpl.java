package net.shop.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import net.shop.mapper.AddressMapper;
import net.shop.model.AddressDO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl implements AddressService{

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public AddressDO detail(Long id) {
        AddressDO addressDO = addressMapper.selectOne(new QueryWrapper<AddressDO>().eq("id", id));
        return addressDO;
    }
}

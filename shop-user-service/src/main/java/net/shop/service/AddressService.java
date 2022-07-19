package net.shop.service;

import net.shop.model.AddressDO;
import net.shop.request.AddressRequest;
import net.shop.vo.AddressVO;

import java.util.List;

public interface AddressService {
    AddressVO detail(Long id);

    /**
     * 新增收货地址
     * @param addressRequest
     * @return
     */
    void add(AddressRequest addressRequest);

    /**
     * 删除指定收货地址
     * @param addressId
     * @return
     */
    int delete(int addressId);

    /**
     * 查找用户全部收货地址
     * @return
     */
    List<AddressVO> listUserAllAddress();
}

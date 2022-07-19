package net.shop.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.shop.enums.BizCodeEnum;
import net.shop.model.AddressDO;
import net.shop.request.AddressRequest;
import net.shop.service.AddressService;
import net.shop.utils.JsonData;
import net.shop.vo.AddressVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 电商-公司收发货地址表 前端控制器
 * </p>
 *
 * @author Wang Xiaohan
 * @since 2022-07-11
 */
@Api(tags = "收货地址模块")
@RestController
@RequestMapping("/api/address/v1/")
public class AddressController {

    @Autowired
    private AddressService addressService;

    @ApiOperation("根据id查找地址详情")
    @GetMapping("/detail/{address_id}")
    public Object detail(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id") Long id){
        AddressVO detail = addressService.detail(id);
        return detail == null ? JsonData.buildResult(BizCodeEnum.ADDRESS_NOT_EXIT):JsonData.buildSuccess(detail);
    }

    @ApiOperation("新增收获地址")
    @PostMapping("/add")
    public JsonData add(@ApiParam("地址对象")@RequestBody AddressRequest addressRequest){
        addressService.add(addressRequest);
        return JsonData.buildSuccess();
    }

    @ApiOperation("删除指定收获地址")
    @DeleteMapping("/delete/{address_id}")
    public JsonData delete(
            @ApiParam(value = "地址id",required = true)
            @PathVariable("address_id")int addressId){
        int rows = addressService.delete(addressId);

        return rows == 1 ? JsonData.buildSuccess():JsonData.buildResult(BizCodeEnum.ADDRESS_DEL_FAIL);
    }

}


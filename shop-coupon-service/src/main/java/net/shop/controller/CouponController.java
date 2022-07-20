package net.shop.controller;


import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import net.shop.enums.CouponCategoryEnum;
import net.shop.service.CouponService;
import net.shop.utils.JsonData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author wxh
 * @since 2022-07-20
 */
@Api("优惠卷模块")
@RestController
@RequestMapping("/api/coupon/v1")
public class CouponController {

    @Autowired
    private CouponService couponService;

    @ApiOperation("分页查询优惠券")
    @GetMapping("/page_coupon")
    public JsonData pageCoupon(
            @ApiParam(value = "当前页")
            @RequestParam(value = "page",defaultValue = "1") int page,
            @ApiParam(value = "每页显示多少")
            @RequestParam(value = "size",defaultValue = "10") int size
    ){
        Map<String, Object> pageMap = couponService.pageCouponActivity(page, size);
        return JsonData.buildSuccess(pageMap);

    }

    /**
     * 领取优惠券
     * @param couponId
     * @return
     */
    @GetMapping("/add/promotion/{coupon_id}")
    public JsonData addPromotionCoupon(@ApiParam(value ="优惠券id",required = true )@PathVariable("coupon_id") String couponId){
        JsonData jsonData = couponService.addCoupon(couponId, CouponCategoryEnum.PROMOTION);
        return jsonData;
    }

}


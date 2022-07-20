package net.shop.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import lombok.extern.slf4j.Slf4j;
import net.shop.enums.CouponCategoryEnum;
import net.shop.enums.CouponPublishEnum;
import net.shop.mapper.CouponMapper;
import net.shop.model.CouponDO;
import net.shop.service.CouponService;
import net.shop.vo.CouponVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CouponServiceImpl implements CouponService {

    @Autowired
    private CouponMapper couponMapper;

    @Override
    public Map<String, Object> pageCouponActivity(int page, int size) {
        //第1页，每页2条
        Page<CouponDO> pageInfo = new Page<>(page, size);
        IPage<CouponDO> couponDOPage = couponMapper.selectPage(pageInfo, new QueryWrapper<CouponDO>()
                .eq("publish", CouponPublishEnum.PUBLISH)
                .eq("category", CouponCategoryEnum.PROMOTION)
                .orderByDesc("create_time"));

        Map<String, Object> pageMap = new HashMap<>(3);

        //总条数
        pageMap.put("total_record", couponDOPage.getTotal());
        //总页数
        pageMap.put("total_page", couponDOPage.getPages());
        pageMap.put("current_data", couponDOPage.getRecords().stream().map(obj -> beanProcess(obj)).collect(Collectors.toList()));

        return pageMap;
    }

    /**
     * 传进入一个DO，返回一个VO
     * @param obj
     * @return
     */
    private CouponVO beanProcess(CouponDO obj) {
        CouponVO couponVO = new CouponVO();
        BeanUtils.copyProperties(obj,couponVO);
        return couponVO;
    }


}

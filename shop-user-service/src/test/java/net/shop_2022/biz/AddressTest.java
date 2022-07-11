package net.shop_2022.biz;

import lombok.extern.slf4j.Slf4j;
import net.shop.UserApplication;
import net.shop.model.AddressDO;
import net.shop.service.AddressService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = UserApplication.class)
@Slf4j
public class AddressTest {

    @Autowired
    private AddressService addressService;

    @Test
    public void testAddressDetail(){
        AddressDO detail = addressService.detail(1L);
        log.info(detail.toString());
        Assert.assertNotNull(detail);
    }
}

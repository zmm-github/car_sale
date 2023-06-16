package com.zmm.car;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

@SpringBootTest
class CarSaleApplicationTests {

    @Test
    public void test01(){
        Date date = new Date();
        System.out.println(date);
    }

//    @Resource
//    private SysUserMapper sysUserMapper;
//
//    @Test
//    void testMapper() {
//        List<SysUser> sysUsers = sysUserMapper.selectList(null);
//        sysUsers.forEach(System.out::println);
//    }

}

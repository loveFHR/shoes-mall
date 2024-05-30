package com.ewan;

import com.ewan.utils.MailUtil;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Ewan
 * @date 2024/4/29
 */

@SpringBootTest
public class DoTest {
    @Resource
    private  MailUtil mailUtil;

    @Test
    public  void testEmail(){
    }

}

    
    
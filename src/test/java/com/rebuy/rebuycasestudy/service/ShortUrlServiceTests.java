package com.rebuy.rebuycasestudy.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class ShortUrlServiceTests {

    @Autowired
    private ShortUrlService shortUrlService;

    @Test
    void createHash1() {
        assertEquals(shortUrlService.encodeHash(14776335), "ZZZZ");
    }

    @Test
    void createHash2() {
        assertEquals(shortUrlService.encodeHash(1), "0001");
    }
}

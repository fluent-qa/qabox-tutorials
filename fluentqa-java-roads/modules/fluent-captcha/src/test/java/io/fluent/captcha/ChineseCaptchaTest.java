package io.fluent.captcha;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ChineseCaptchaTest {

    @Test
    public void testChineseCaptcha(){
        ChineseCaptcha chineseCaptcha = new ChineseCaptcha();
        String result = chineseCaptcha.getContentType();
        System.out.println(result);

    }
}
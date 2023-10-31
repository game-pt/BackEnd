package com.a405.gamept.util;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

@SpringBootTest
class ChatGptClientUtilTest {

    @Autowired
    ChatGptClientUtil chatGptClientUtil;

    @Test
    void enterPrompt() throws IOException, InterruptedException {
        chatGptClientUtil.enterPrompt("1 + 1은 뭐야? 어린아이도 이해할 수 있도록 설명해줄래?");
    }

}
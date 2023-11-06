package com.a405.gamept.util;

import com.a405.gamept.util.dto.request.ChatGptRequestDto;
import com.a405.gamept.util.dto.request.ChatGptMessage;
import com.a405.gamept.util.dto.response.ChatGptResponseDto;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

/**
 * ChatGptClientUtil
 *
 * API를 통해 ChatGPT와 관련된 모든 기능을 포함하는 Util 객체를 정의.
 */
@Component
public class ChatGptClientUtil {
    private final String model;
    private final String uri;
    private final String key;

    public ChatGptClientUtil(@Value("${gpt.model}") String model, @Value("${gpt.api-uri}") String uri, @Value("${gpt.api-key}") String key) {
        this.model = model;
        this.uri = uri;
        this.key = key;
    }

    /**
     * enterPrompt <br/><br/>
     *
     * 입력한 프롬프트를 ChatGPT에 전송하고, ChatGPT로부터 온 출력 결과를 반환.
     * @param prompt ChatGPT에 입력할 프롬프트
     * @throws IOException
     * @throws InterruptedException
     */
    public String enterPrompt(String prompt) {
        ChatGptMessage[] messages = new ChatGptMessage[1];
        messages[0] = new ChatGptMessage("user", prompt);
        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto(model, messages, 1, 2048);

        try {
            String input = mapper.writeValueAsString(chatGptRequestDto);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(uri))
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + key)
                    .POST(HttpRequest.BodyPublishers.ofString(input))
                    .build();

            HttpClient client = HttpClient.newHttpClient();
            var response = client.send(request, HttpResponse.BodyHandlers.ofString());

            // 응답이 정상인 경우,
            if (response.statusCode() == 200) {
                ChatGptResponseDto chatGptResponseDto = mapper.readValue(response.body(), ChatGptResponseDto.class);
                String answer = chatGptResponseDto.choices()[chatGptResponseDto.choices().length - 1].message().content();

                // 대답이 제대로 온 경우,
                if (!answer.isEmpty()) {
                    System.out.println(answer);
                    return answer.replace("\n", "").trim();
                }

            // 응답이 비정상인 경우,
            } else {
                System.out.println(response.statusCode());
                System.out.println(response.body());
                return null;
            }
            
        // 에러 발생
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

}

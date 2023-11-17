package com.a405.gamept.util;

import com.a405.gamept.game.util.enums.GameErrorMessage;
import com.a405.gamept.game.util.exception.GameException;
import com.a405.gamept.play.entity.Prompt;
import com.a405.gamept.util.dto.request.ChatGptForStreamRequestDto;
import com.a405.gamept.util.dto.request.ChatGptRequestDto;
import com.a405.gamept.util.dto.request.ChatGptMessage;
import com.a405.gamept.util.dto.response.ChatGptForStreamResponseDto;
import com.a405.gamept.util.dto.response.ChatGptResponseDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.net.URI;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * ChatGptClientUtil
 * <p>
 * API를 통해 ChatGPT와 관련된 모든 기능을 포함하는 Util 객체를 정의.
 */
@Component
@Slf4j
public class ChatGptClientUtil {
    private final String model;
    private final String uri;
    private final String key;
    private final String PROMPT_FOR_SETTING =
            "[TRPG 기본 설정]\n" +
                    "당신은 TRPG의 게임 마스터가 되어 나의 채팅에 맞춰 스토리를 진행시켜야 합니다. " +
                    "나는 판타지 세계의 모험가가 되어 게임을 플레이합니다. " +
                    "TRPG 게임은 중세시대를 배경으로 합니다. " +
                    "몬스터로 고블린, 오크, 코볼트, 슬라임, 놀, 드래곤, 악마 등이 등장할 수 있습니다. " +
                    "TPRG 게임에서 몬스터를 처치하려면 내가 몬스터를 강하게 공격했다는 것이 드러나는 채팅을 입력해야 합니다. " +
                    "TRPG 게임에서 몬스터를 쓰러뜨리면 전리품을 얻을 수도 있습니다. " +
                    "TRPG 게임은 길드에서 퀘스트를 수주할 수 있습니다. " +
                    "TRPG 게임은 퀘스트를 완수하면 길드에서 보상을 받을 수 있습니다. " +
                    "TPRG 게임은 주로 대화 형식으로 게임이 이루어집니다. " +
                    "나의 대답을 당신이 말할 수 없습니다. " +
                    "내가 대답할 수 있는 여지를 주어야 합니다. " +
                    "TRPG 게임은 반드시 플레이어의 전투 상황이 시작될 때, [전투] 라고 출력해주어야 합니다. " +
                    "전투 상황이란, 플레이어가 몬스터나 플레이어에게 적대적인 NPC를 맞닥뜨렸을 때의 상황을 말합니다. " +
                    "TRPG 게임은 반드시 플레이어가 함정에 빠졌을 때, [함정] 이라고 출력해주어야 합니다. " +
                    "함정이란, 갑작스럽게 바닥 혹은 천장이 무너지거나, 눈에 띄지 않는 발판을 밟으면, 플레이어를 향하여 화살이나 마법이 날라와 플레이어를 공격하는 경우를 말합니다. " +
                    "첫 시작은 당신이 길드 마스터의 입장이 되어 내가 길드에 입장한 것을 반겨줍니다.";
    @Value("${gpt.model}")
    private String MODEL;
    @Value("${gpt.api-uri}")
    private String API_URL;
    @Value("${gpt.api-key}")
    private String TOKEN;

    public ChatGptClientUtil(@Value("${gpt.model}") String model, @Value("${gpt.api-uri}") String uri, @Value("${gpt.api-key}") String key) {
        this.model = model;
        this.uri = uri;
        this.key = key;
    }

    /**
     * enterPrompt <br/><br/>
     * <p>
     * 입력한 프롬프트를 ChatGPT에 전송하고, ChatGPT로부터 온 출력 결과를 반환.
     *
     * @param prompt ChatGPT에 입력할 프롬프트
     * @throws IOException
     * @throws InterruptedException
     */
    public String enterPrompt(String prompt, String memory, List<Prompt> promptLogs) {
        int mSize = 1;
        if (prompt != null && !prompt.equals("")) mSize++;
        if (memory != null && !memory.equals("")) mSize++;
        mSize += promptLogs.size();
        ChatGptMessage[] messages = new ChatGptMessage[mSize];

        int mIndex = 0;
        messages[mIndex++] = new ChatGptMessage("system", PROMPT_FOR_SETTING);
        if (memory != null && !memory.equals(""))
            messages[mIndex++] = new ChatGptMessage("system", memory);
        for (Prompt prevPrompt : promptLogs) {
            messages[mIndex++] = new ChatGptMessage(prevPrompt.getRole(), prevPrompt.getContent());
        }
        if (prompt != null && !prompt.equals(""))
            messages[mIndex++] = new ChatGptMessage("user", prompt);

        ObjectMapper mapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ChatGptRequestDto chatGptRequestDto = new ChatGptRequestDto(model, messages, 1, 1024);

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
                    log.info("ChatGPT: " + answer);
                    return answer;
                }

                // 응답이 비정상인 경우,
            } else {
                log.info("ResponseStatus: " + response.statusCode());
                log.info("ResponseBody: " + response.body());
                return null;
            }

            // 에러 발생
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

        return null;
    }

    public String getChatGPTResult(String memory, List<Prompt> promptList, String eventPrompt) {
        List<Prompt> sendList = new ArrayList<>();

        if (memory != null || !memory.equals("")) {
            // 메모리 프롬프트 삽입
            sendList.add(Prompt.builder()
                    .role("system")
                    .content(memory)
                    .build());
        }

        // user인지를 구분하는 로직
        for(Prompt prompt : promptList) {
            sendList.add(Prompt.builder()
                    .role((prompt.getRole() != "assistant")? "user": prompt.getRole())
                    .content(prompt.getContent())
                    .build());
        }

        // 이벤트 프롬프트 삽입
        sendList.set(sendList.size() - 1, sendList.get(sendList.size() - 1).toBuilder()
                .content(sendList.get(sendList.size() - 1).getContent() + "\n" + eventPrompt)
                .build());

        Map<String, Object> map = new HashMap<>();
        map.put("model", MODEL);
        map.put("messages", sendList);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(API_URL))
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + TOKEN)
                .POST(HttpRequest.BodyPublishers.ofString(new Gson().toJson(map)))
                .build();

        log.info("느려!!");
        HttpResponse<String> response = null;  // Response 받을 변수
        try {
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            log.info("ChatGPT result: " + jsonNode.get("choices").get(0).get("message").get("content").asText());

            // ChatGPT 응답 content 반환
            System.out.println("됐다!!");
            return jsonNode.get("choices").get(0).get("message").get("content").asText();
        } catch (RuntimeException | IOException | InterruptedException e) {
            log.error(e.getMessage());
            System.out.println("에러!!");
            throw new GameException(GameErrorMessage.PROMPT_INVALID);
        }
    }

    public String enterPromptForSse(SseEmitter emitter, String prompt, String settingPrompt, String memory, List<Prompt> promptList) throws JsonProcessingException {
        int mSize = 1;
        if (prompt != null && !prompt.equals("")) mSize++;
        if (memory != null && !memory.equals("")) mSize++;
        mSize += promptList.size();
        ChatGptMessage[] messages = new ChatGptMessage[mSize];

        int mIndex = 0;
        if (settingPrompt != null && !settingPrompt.equals(""))
            messages[mIndex++] = new ChatGptMessage("system", settingPrompt);
        if (memory != null && !memory.equals(""))
            messages[mIndex++] = new ChatGptMessage("system", memory);
        for (Prompt prevPrompt : promptList) {
            messages[mIndex++] = new ChatGptMessage(prevPrompt.getRole().equals("assistant") ? "assistant" : "user", prevPrompt.getContent());
        }
        if (prompt != null && !prompt.equals(""))
            messages[mIndex++] = new ChatGptMessage("user", prompt);

        ObjectMapper mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                .setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
        ChatGptForStreamRequestDto chatGptRequestDtoForStream = new ChatGptForStreamRequestDto(model, messages, 1, 1024, true);

        String input = mapper.writeValueAsString(chatGptRequestDtoForStream);
        StringBuilder outputSB = new StringBuilder();

//        SseEmitter emitter = new SseEmitter((long) (5 * 60 * 1000));
        WebClient client = WebClient.create();

        log.info("느려!!");
        client.post().uri(uri)
                .header("Content-Type", "application/json")
                .header("Authorization", "Bearer " + key)
                .body(BodyInserters.fromValue(chatGptRequestDtoForStream))
                .exchangeToFlux(response -> response.bodyToFlux(String.class))
                .doOnNext(line -> {
                    try {
                        if (line.equals("[DONE]")) {
//                            emitter.complete();
                            emitter.send(SseEmitter.event().name("sse").data(line));
                            return;
                        }
                        ChatGptForStreamResponseDto chatGptForStreamResponseDto = mapper.readValue(line, ChatGptForStreamResponseDto.class);
                        String answer = chatGptForStreamResponseDto.choices()[chatGptForStreamResponseDto.choices().length - 1].delta().content();
                        if (answer != null) {
                            outputSB.append(answer);
                            emitter.send(SseEmitter.event().name("sse").data(line));
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
//                .doOnError(emitter::completeWithError)
//                .doOnComplete(emitter::complete)
                .blockLast();

        log.info("됐다!!");
        return outputSB.toString();
    }

}

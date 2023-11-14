package com.a405.gamept.member.service;

import com.a405.gamept.member.dto.command.KakaoTokenCommandDto;
import com.a405.gamept.member.dto.command.KakaoUserCommandDto;
import com.a405.gamept.member.dto.response.KakaoTokenResopnseDto;
import com.a405.gamept.member.dto.response.KakaoUserResopnseDto;
import com.a405.gamept.member.entity.Member;
import com.a405.gamept.member.repository.MemberRedisRepository;
import com.a405.gamept.member.util.enums.MemberErrorMessage;
import com.a405.gamept.member.util.exception.MemberException;
import com.a405.gamept.util.ValidateUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URIBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class MemberServiceImpl implements MemberService {

    private final MemberRedisRepository memberRepository;
    @Value(value = "${security.kakao.client-id}")
    private String KAKAO_CLIENT_ID;
    @Value(value = "${security.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;
    @Value(value = "${security.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;
    @Value(value = "${security.kakao.user-info-uri}")
    private String KAKAO_USER_INFO_URI;

    @Override
    @Transactional(readOnly = true)
    public KakaoTokenResopnseDto getKakaoToken(KakaoTokenCommandDto kakaoTokenCommandDto) throws MemberException {
        ValidateUtil.validate(kakaoTokenCommandDto);

        URIBuilder uriBuilder = null;
        try {
            uriBuilder = new URIBuilder(KAKAO_TOKEN_URI);
        } catch (URISyntaxException e) {
            throw new MemberException(MemberErrorMessage.URI_INVALID);
        }
        uriBuilder.addParameter("grant_type", "authorization_code");
        uriBuilder.addParameter("client_id", KAKAO_CLIENT_ID);
        uriBuilder.addParameter("redirect_uri", KAKAO_REDIRECT_URI);
        uriBuilder.addParameter("code", kakaoTokenCommandDto.code());

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(KAKAO_TOKEN_URI))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .POST(BodyPublishers.ofString(uriBuilder.getQueryParams().toString()
                        .replace("[", "").replace("]", "").replace(", ", "&")))
                .build();

        HttpResponse<String> response;  // Response 받을 변수
        try {
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new MemberException(MemberErrorMessage.TOKEN_NOT_FOUND);
        }

        KakaoTokenResopnseDto kakaoTokenResopnseDto = null;
        if (response.statusCode() != HttpStatus.OK.value()) {  // 200이 아닐 경우
            throw new MemberException(MemberErrorMessage.TOKEN_INVALID);
        }
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body());
            log.info("Response Body: " + jsonNode);

            // 토큰 세팅
            kakaoTokenResopnseDto = KakaoTokenResopnseDto.builder()
                    .scope(jsonNode.get("scope").asText())
                    .accessToken(jsonNode.get("access_token").asText())
                    .refreshToken(jsonNode.get("refresh_token").asText())
                    .expiresIn(jsonNode.get("expires_in").asInt())
                    .refreshTokenExpiresIn(jsonNode.get("refresh_token_expires_in").asInt())
                    .build();

        } catch (JsonProcessingException e) {
            throw new MemberException(MemberErrorMessage.TOKEN_INVALID);
        }

        ValidateUtil.validate(kakaoTokenResopnseDto);

        return kakaoTokenResopnseDto;
    }

    @Override
    @Transactional
    public KakaoUserResopnseDto kakaoLogin(KakaoUserCommandDto kakaoUserCommandDto) throws MemberException {
        ValidateUtil.validate(kakaoUserCommandDto);

        HttpClient client = HttpClient.newBuilder().build();
        HttpRequest getRequest = HttpRequest.newBuilder()
                .uri(URI.create(KAKAO_USER_INFO_URI))
                .header("Content-type", "application/x-www-form-urlencoded;charset=utf-8")
                .header("Authorization", "Bearer " + kakaoUserCommandDto.accessToken())
                .POST(BodyPublishers.ofString(""))
                .build();

        HttpResponse<String> response;  // Response 받을 변수
        try {
            response = client.send(getRequest, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new MemberException(MemberErrorMessage.TOKEN_INVALID);
        }

        if (response.statusCode() != HttpStatus.OK.value()) {  // 200이 아닐 경우
            throw new MemberException(MemberErrorMessage.TOKEN_INVALID);
        }

        KakaoUserResopnseDto kakaoUserResopnseDto = null;
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(response.body()).get("kakao_account");

            log.info("Response Body: " + jsonNode);
            memberRepository.save(Member.builder()
                    .email(jsonNode.get("email").asText())
                    .refreshToken(kakaoUserCommandDto.refreshToken())
                    .expiresIn(kakaoUserCommandDto.refreshTokenExpiresIn())
                    .build());

            kakaoUserResopnseDto = KakaoUserResopnseDto.builder()
                    .nickname(jsonNode.get("profile").get("nickname").asText())
                    .email(jsonNode.get("email").asText())
                    .accessToken(kakaoUserCommandDto.accessToken())
                    .refreshToken(kakaoUserCommandDto.refreshToken())
                    .build();
        } catch (JsonProcessingException e) {
            throw new MemberException(MemberErrorMessage.TOKEN_INVALID);
        }

        ValidateUtil.validate(kakaoUserResopnseDto);

        return kakaoUserResopnseDto;
    }
}

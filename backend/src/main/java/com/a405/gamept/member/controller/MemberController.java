package com.a405.gamept.member.controller;

import com.a405.gamept.member.dto.command.KakaoTokenCommandDto;
import com.a405.gamept.member.dto.command.KakaoUserCommandDto;
import com.a405.gamept.member.dto.response.KakaoTokenResopnseDto;
import com.a405.gamept.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("member")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;

    @GetMapping("login/kakao")
    public ResponseEntity<?> kakaoLogin(@RequestParam String code) {
        KakaoTokenResopnseDto kakaoTokenResopnseDto = memberService.getKakaoToken(KakaoTokenCommandDto.of(code));
        return ResponseEntity.ok(memberService.kakaoLogin(KakaoUserCommandDto.from(kakaoTokenResopnseDto)));
    }
}

package com.a405.gamept.member.service;

import com.a405.gamept.member.dto.command.KakaoTokenCommandDto;
import com.a405.gamept.member.dto.command.KakaoUserCommandDto;
import com.a405.gamept.member.dto.response.KakaoTokenResopnseDto;
import com.a405.gamept.member.dto.response.KakaoUserResopnseDto;
import com.a405.gamept.member.util.exception.MemberException;

public interface MemberService {
    KakaoTokenResopnseDto getKakaoToken(KakaoTokenCommandDto kakaoTokenCommandDto) throws MemberException;
    KakaoUserResopnseDto kakaoLogin(KakaoUserCommandDto kakaoUserCommandDto) throws MemberException;
}

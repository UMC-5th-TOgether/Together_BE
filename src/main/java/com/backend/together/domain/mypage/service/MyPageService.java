package com.backend.together.domain.mypage.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.mypage.dto.MyInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MyPageService {
    @Autowired
    MemberRepository memberRepository;

    public MyInfoDto getMyInfo(Long id) {
        MemberEntity entity = memberRepository.findById(id).get();
        MyInfoDto myInfoDto = MyInfoDto.builder().nickname(entity.getNickname()).age(entity.getAge()).image(entity.getImage()).build();
        return myInfoDto;
    }
}

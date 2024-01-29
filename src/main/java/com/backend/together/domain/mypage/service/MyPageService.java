package com.backend.together.domain.mypage.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.mypage.dto.MyInfoDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyPageService {
    @Autowired
    MemberRepository memberRepository;

    public MyInfoDto getMyInfo(String memberId) {
        MemberEntity entity = memberRepository.findById(Long.parseLong(memberId)).get();
        MyInfoDto myInfoDto = MyInfoDto.builder()
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .image(entity.getImage())
                .gender(entity.getGender())
                .station(entity.getStation())
                .build();
        return myInfoDto;
    }


    // 비밀번호 확인하기
    public boolean checkPassword(String memberId, String password, final PasswordEncoder encoder) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));
        if(member.isPresent() && encoder.matches(password,member.get().getPassword())){
            return true;
        }
        else {
            throw new Exception("잘못된 비밀번호 입니다.");
        }

    }

    // 회원탈퇴
    public boolean deleteMember(String memberId) throws Exception  {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberRepository.delete(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 닉네임 변경하기
    public boolean changeNickname(String memberId, String nickname) throws Exception {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));

        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setNickname(nickname);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 프로필 이미지 변경하기
    public boolean changeImage(String memberId, String image) throws Exception {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));

        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setImage(image);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

}

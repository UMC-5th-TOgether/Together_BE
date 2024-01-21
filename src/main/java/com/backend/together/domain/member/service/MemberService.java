package com.backend.together.domain.member.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public void create(final MemberEntity memberEntity) throws Exception {
        if(memberRepository.existsByNickname(memberEntity.getNickname())){ // 닉네임 중복 불가!
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
        else if(memberRepository.existsByEmail(memberEntity.getEmail())){ // 이메일 중복 불가!
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        memberRepository.save(memberEntity);
    }

    public MemberEntity getByCredentials(final String email, final String password , final PasswordEncoder encoder){
        // 인증정보로 Member 검색.
        final Optional<MemberEntity> originalUser = memberRepository.findByEmail(email);
        if(originalUser.isPresent() && encoder.matches(password,originalUser.get().getPassword())){
            return originalUser.get();
        }
        return null;

        // return userRepository.findByEmailAndPassword(email,password);
    }

    // 닉네임으로 이메일(계정) 찾기
    public String findEmailByNickname(final String nickname) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByNickname(nickname);
        if(member.isPresent()) {
            return member.get().getEmail();
        }
        else{
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 이메일과 닉네임으로 비밀번호 찾기
    public boolean findPasswordByEmailAndNickname(final String email,final String nickname) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByEmailAndNickname(email,nickname);
        if(member.isPresent()) {
            return true;
        }
        else{
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 비밀번호 변경하기
    public boolean changePassword(final String nickname,final String email,String password) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByEmailAndNickname(email,nickname);
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setPassword(password);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

}

package com.backend.together.domain.member.service;

import com.backend.together.domain.member.entity.Member;
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

    public void create(final Member member) throws Exception {
        if(memberRepository.existsByNickname(member.getNickname())){ //중복 불가!
            throw new RuntimeException("이미 존재하는 닉네임입니다.");
        }
        else if(memberRepository.existsByEmail(member.getEmail())){ //중복 불가!
            throw new RuntimeException("이미 존재하는 아이디입니다.");
        }
        memberRepository.save(member);
    }

    public Member getByCredentials(final String email, final String password , final PasswordEncoder encoder){
        // 인증정보로 Member 검색.
        final Optional<Member> originalUser = memberRepository.findByEmail(email);
        if(originalUser.isPresent() && encoder.matches(password,originalUser.get().getPassword())){
            return originalUser.get();
        }
        return null;

        // return userRepository.findByEmailAndPassword(email,password);
    }

}

package com.backend.together.domain.post.converter;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.Optional;
@Component
public class PostMemberConverter {
    @Autowired@Lazy
    MemberRepository memberRepository;
    public MemberEntity getMember(Long memberId) {
        Optional<MemberEntity> member = memberRepository.findById(memberId);
        return member.orElse(null);
    }
}

package com.backend.together.domain.matching.service;

import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.repository.MatchingImageRepository;
import com.backend.together.domain.matching.repository.MatchingRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import com.backend.together.global.enums.MatchingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Transactional
@RequiredArgsConstructor
@Service
public class MatchingServiceImpl implements MatchingService{
    private final MemberRepository memberRepository;
    private final MatchingRepository matchingRepository;
    private final MatchingImageRepository matchingImageRepository;

    @Override
    public Matching postMatching(MatchingRequestDTO.PostMatchingDTO request, Long userId) {
        if (Objects.equals(request.getReceiverId(), userId)) {
            throw new CustomHandler(ErrorStatus.SELF_MATCHING_DECLINE);
        }

        MemberEntity sender = memberRepository.findById(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        MemberEntity receiver = memberRepository.findById(request.getReceiverId())
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        Matching newMatching = request.toEntity(receiver, sender);
        matchingRepository.save(newMatching);

        return newMatching;
    }

    @Override
    public void declineMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Matching matching = matchingRepository.findById(request.getMatchingId())
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MATCHING_NOT_FOUND));

        if (member != matching.getReceiver())
            throw new CustomHandler(ErrorStatus.INVALID_APPROACH);
        else
            matching.updateMatchingStatus(MatchingStatus.DECLINE);
    }

    @Override
    public Matching acceptMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId) {
        MemberEntity member = memberRepository.findByMemberId(userId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));
        Matching matching = matchingRepository.findById(request.getMatchingId())
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MATCHING_NOT_FOUND));

        if (member != matching.getReceiver())
            throw new CustomHandler(ErrorStatus.INVALID_APPROACH);
        else {
            matching.updateMatchingStatus(MatchingStatus.ACCEPT);
            return matching;
        }
    }

    @Override
    public Matching getMatchingDetail(Long matchingId) {
        Matching matching = matchingRepository.findById(matchingId)
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MATCHING_NOT_FOUND));

        return matching;
    }

}

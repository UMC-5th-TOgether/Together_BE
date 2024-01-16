package com.backend.together.domain.matching.service;

import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.entity.Matching;

public interface MatchingService {
    void postMatching(MatchingRequestDTO.PostMatchingDTO request, Long userId); //atk 도입시 Long senderId 추가
    void declineMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId);
    Matching acceptMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId);
    Matching getMatchingDetail(Long matchingId);
}

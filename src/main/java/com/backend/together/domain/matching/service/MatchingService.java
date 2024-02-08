package com.backend.together.domain.matching.service;

import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.entity.Matching;

public interface MatchingService {
    Matching postMatching(MatchingRequestDTO.PostMatchingDTO request, Long userId);
    void declineMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId);
    Matching acceptMatching(MatchingRequestDTO.UpdateMatchingStatusDTO request, Long userId);
    Matching getMatchingDetail(Long matchingId, Long userId);
}

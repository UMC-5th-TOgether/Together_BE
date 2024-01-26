package com.backend.together.domain.matching.controller;

import com.backend.together.domain.friend.service.FriendServiceImpl;
import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.dto.MatchingResponseDTO;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.service.MatchingServiceImpl;
import com.backend.together.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matchings")
public class MatchingController {
    private final MatchingServiceImpl matchingService;
    private final FriendServiceImpl friendService;

    @PostMapping()
    public ApiResponse<?> postMatching(@RequestBody @Valid MatchingRequestDTO.PostMatchingDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        matchingService.postMatching(request, userId);
        return ApiResponse.successWithoutResult();
    }

    @PatchMapping("/decline")
    public ApiResponse<?> declineMatching(@RequestBody @Valid MatchingRequestDTO.UpdateMatchingStatusDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        matchingService.declineMatching(request, userId);
        return ApiResponse.successWithoutResult();
    }

    @PatchMapping("/accept")
    public ApiResponse<?> acceptMatching(@RequestBody @Valid MatchingRequestDTO.UpdateMatchingStatusDTO request) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        //1. 매칭 상태를 ACCEPT로 바꾼다
        Matching matching = matchingService.acceptMatching(request, userId);
        //2. 친구를 추가한다
        friendService.addNewFriend(matching);
        return ApiResponse.successWithoutResult();
    }

    @GetMapping("/detail/{matchingId}")
    public ApiResponse<MatchingResponseDTO.GetMatchingDetailDTO> getMatchingDetail(@PathVariable(name = "matchingId") Long matchingId){
        Matching matchingDetail = matchingService.getMatchingDetail(matchingId);
        return ApiResponse.onSuccess(MatchingResponseDTO.GetMatchingDetailDTO.getMatchingDetail(matchingDetail));
    }
}

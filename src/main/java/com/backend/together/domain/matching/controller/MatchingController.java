package com.backend.together.domain.matching.controller;

import com.amazonaws.util.CollectionUtils;
import com.backend.together.domain.friend.service.FriendServiceImpl;
import com.backend.together.domain.matching.dto.MatchingRequestDTO;
import com.backend.together.domain.matching.dto.MatchingResponseDTO;
import com.backend.together.domain.matching.entity.Matching;
import com.backend.together.domain.matching.service.MatchingImageServiceImpl;
import com.backend.together.domain.matching.service.MatchingServiceImpl;
import com.backend.together.global.apiPayload.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/matching")
public class MatchingController {
    private final MatchingServiceImpl matchingService;
    private final MatchingImageServiceImpl matchingImageService;
    private final FriendServiceImpl friendService;

    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE})
    public ApiResponse<?> postMatching(@RequestPart @Valid MatchingRequestDTO.PostMatchingDTO request, @RequestPart(required = false) List<MultipartFile> requestImages) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Matching newMatching = matchingService.postMatching(request, userId);

        if (!CollectionUtils.isNullOrEmpty(requestImages)) {
            requestImages.forEach(image -> matchingImageService.postMatchingImage(image, newMatching));
        }
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
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long userId = Long.parseLong(authentication.getName());

        Matching matching = matchingService.getMatchingDetail(matchingId, userId);

        List<String> matchingImages = matchingImageService.getMatchingImages(matchingId);

        return ApiResponse.onSuccess(MatchingResponseDTO.GetMatchingDetailDTO.getMatchingDetail(matching, matchingImages));
    }
}

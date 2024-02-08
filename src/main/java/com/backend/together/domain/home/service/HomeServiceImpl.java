package com.backend.together.domain.home.service;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.block.service.BlockServiceImpl;
import com.backend.together.domain.home.repository.HomeRepository;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.Post;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import com.backend.together.global.enums.Category;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
@Service
@Transactional
@RequiredArgsConstructor
public class HomeServiceImpl implements HomeService{
    private final HomeRepository homeListRepository;
    private final BlockServiceImpl blockService;
    @Override
    public List<Post> getPostListForGuestHome(String category) {
        Category postCategory = switch (category.toUpperCase()) {
            case "HOBBY" -> Category.HOBBY;
            case "PLAY" -> Category.PLAY;
            case "EAT" -> Category.EAT;
            case "EXERCISE" -> Category.EXERCISE;
            default -> throw new CustomHandler(ErrorStatus.CATEGORY_NOT_FOUND);
        };
        return homeListRepository.findTop8ByCategoryOrderByViewDesc(postCategory);
    }

    @Override
    public List<Post> getPostListForHome(String category, Long userId) {
        Category postCategory = switch (category.toUpperCase()) {
            case "HOBBY" -> Category.HOBBY;
            case "PLAY" -> Category.PLAY;
            case "EAT" -> Category.EAT;
            case "EXERCISE" -> Category.EXERCISE;
            default -> throw new CustomHandler(ErrorStatus.CATEGORY_NOT_FOUND);
        };

        List<MemberEntity> blockedList = blockService.getBlockedMember(userId).stream().map(Block::getBlocked).toList();
        if (blockedList.isEmpty()) {
            return homeListRepository.findTop8ByCategoryOrderByViewDesc(postCategory);
        } else {
            return homeListRepository.findTop8ByCategoryAndMemberNotInOrderByViewDesc(postCategory, blockedList);
        }
    }
}

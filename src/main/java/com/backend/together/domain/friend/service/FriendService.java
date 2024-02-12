package com.backend.together.domain.friend.service;

import com.backend.together.domain.friend.entity.FriendList;
import com.backend.together.domain.matching.entity.Matching;

import java.util.List;

public interface FriendService {
    List<Matching> getFollowingList(Long userId);
    List<Matching> getFollowerList(Long userId);
    void addNewFriend(Matching matching);
    List<FriendList> getRelationshipList(Long userId);
    void deleteFriend(Long userId, Long memberId);
}

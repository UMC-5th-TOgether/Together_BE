package com.backend.together.domain.block.Repository;

import com.backend.together.domain.block.Entity.Block;
import com.backend.together.domain.member.entity.MemberEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {
    List<Block> findAllByBlocker(MemberEntity member);
}

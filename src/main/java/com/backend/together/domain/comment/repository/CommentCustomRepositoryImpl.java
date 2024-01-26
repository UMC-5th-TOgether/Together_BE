//package com.backend.together.domain.comment.repository;
//
//import com.backend.together.domain.comment.Comment;
//import com.backend.together.domain.comment.dto.CommentResponseDTO;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.PersistenceContext;
//import lombok.RequiredArgsConstructor;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.jpa.repository.query.JpaQueryMethodFactory;
//import org.springframework.stereotype.Repository;
//
//import java.util.List;
//import java.util.Optional;
//
//@RequiredArgsConstructor
//@Repository
//public class CommentCustomRepositoryImpl implements CommentCustomRepository {
//    @PersistenceContext
//    private EntityManager entityManager;
//
//
//    @Override
//    public List<CommentResponseDTO> findByPostId(Long id) {
//        JpaQueryMethodFactory queryFactory = new JpaQueryMethodFactory(entityManager);
//
//        List<Comment> comments = queryFactory.selectFrom(comment)
//                .leftJoin(comment.parent).fetchJoin()
//                .where(comment.board.id.eq(id))
//                .orderBy(comment.parent.id.asc().nullsFirst(),
//                        comment.createdAt.asc())
//                .fetch();
//    }
//
//    @Override
//    public Optional<Comment> findCommentByIdWithParent(Long id) {
//        return Optional.empty();
//    }
//}

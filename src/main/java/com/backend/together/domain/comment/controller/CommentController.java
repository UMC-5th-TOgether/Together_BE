package com.backend.together.domain.comment.controller;

import com.backend.together.domain.comment.Comment;
import com.backend.together.domain.comment.converter.CommentConverter;
import com.backend.together.domain.comment.dto.CommentRequestDTO;
import com.backend.together.domain.comment.dto.CommentResponseDTO;
import com.backend.together.domain.comment.service.CommentService;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.post.dto.PostResponseDTO;
import com.backend.together.global.apiPayload.ApiErrResponse;
import com.backend.together.global.apiPayload.ApiResponse;
import com.backend.together.global.apiPayload.code.ErrorReasonDTO;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("api/post/comment")
public class CommentController {
    @Autowired
    private CommentService service;
    @Autowired
    private CommentConverter converter;
    // get : 해당 포스트 댓글 전부 가져오기 // 시간순으로 가져와야함
    // get :
    // get :
    // get :
    // get :
    // post : 댓글 추가하기 0
    // post :
    // update : 댓글 수정하기
    // delete : 댓글 삭제하기


    @GetMapping({"/id"})
    public ResponseEntity<?> findCommentsWithParentFetch(Long id) { // post id
        List<Comment> comments = service.retrieveCommentsWithParent(id); // 댓글들 가져옴

        List<CommentResponseDTO> commentResponseDTOList = new ArrayList<>();
        Map<Long, CommentResponseDTO> commentDTOHashMap = new HashMap<>();

        comments.forEach(c -> { // 하나씩 넣어줌
            CommentResponseDTO commentResponseDTO = CommentResponseDTO.convertCommentToDTO(c);
            commentDTOHashMap.put(commentResponseDTO.getId(), commentResponseDTO);
            // 부모가 null이 아닌 경우에만 처리
            if (c.getParent() != null) {
                Long parentId = c.getParent().getId();
                CommentResponseDTO parentDTO = commentDTOHashMap.get(parentId);

                // 부모 DTO가 있는 경우에만 자식 추가
                if (parentDTO != null) {
                    parentDTO.getChildren().add(commentResponseDTO);
                }
            } else {
                commentResponseDTOList.add(commentResponseDTO);
            }
        });

        return ResponseEntity.ok().body(commentResponseDTOList);
    }

    @PostMapping
    public ResponseEntity<?> createComment(@RequestBody CommentRequestDTO d) {
        CommentRequestDTO dto = d;
        // 사용자 ID 가져오기
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long memberId = Long.parseLong(authentication.getName());
        // member어캄~~~~~~~~~~~~~~~~~~~~~~~~
        dto.setWriter(memberId);
        if (dto.getParent() == -1 ) {
            dto.setParent(-1L);
        }


        Comment comment = converter.CommentToEntity(dto);
        service.create(comment);
        CommentResponseDTO responseDTO = CommentResponseDTO.convertCommentToDTO(comment);


        if (comment == null) {
            ErrorStatus errorStatus = ErrorStatus._BAD_REQUEST;
            ErrorReasonDTO errorReason = errorStatus.getReason();
            ApiErrResponse<CommentResponseDTO> response = ApiErrResponse.onFailure(errorStatus.getCode(), errorReason.getMessage(), responseDTO);
            return ResponseEntity.status(errorStatus.getHttpStatus()).body(response);
        }

        ApiResponse<CommentResponseDTO> successResponse = ApiResponse.onSuccess(responseDTO);
        return ResponseEntity.ok().body(successResponse);
    }

    @DeleteMapping({"/id"})
    public ResponseEntity<?> deleteComment(Long id) {
        service.delete(id);

        return ResponseEntity.ok().body(true);
    }

}

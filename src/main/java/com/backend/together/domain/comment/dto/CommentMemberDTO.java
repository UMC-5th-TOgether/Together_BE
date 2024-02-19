package com.backend.together.domain.comment.dto;

import com.backend.together.domain.member.entity.MemberEntity;
import lombok.*;
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
public class CommentMemberDTO {

    private String nickname;
    private String age;
    private String gender;
    private String image;

    public CommentMemberDTO(MemberEntity writer) {
        this.nickname = writer.getNickname();
        this.age = writer.getAge();
        this.gender = writer.getGender();
        this.image = writer.getImage();
    }

}

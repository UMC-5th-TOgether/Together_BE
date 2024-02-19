package com.backend.together.domain.mypage.service;

import com.amazonaws.util.CollectionUtils;
import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.mypage.dto.MyInfoDto;
import com.backend.together.global.apiPayload.code.status.ErrorStatus;
import com.backend.together.global.apiPayload.exception.handler.CustomHandler;
import com.backend.together.global.aws.s3.AmazonS3Manager;
import com.backend.together.global.aws.s3.Uuid;
import com.backend.together.global.aws.s3.UuidRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;
import java.util.UUID;

@Service
public class MyPageService {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    private AmazonS3Manager s3Manager;
    @Autowired
    private UuidRepository uuidRepository;

    public MyInfoDto getMyInfo(String memberId) {
        MemberEntity entity = memberRepository.findById(Long.parseLong(memberId)).get();
        MyInfoDto myInfoDto = MyInfoDto.builder()
                .nickname(entity.getNickname())
                .age(entity.getAge())
                .image(entity.getImage())
                .gender(entity.getGender())
                .station(entity.getStation())
                .build();
        return myInfoDto;
    }


    // 비밀번호 확인하기
    public boolean checkPassword(String memberId, String password, final PasswordEncoder encoder) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));
        if(member.isPresent() && encoder.matches(password,member.get().getPassword())){
            return true;
        }
        else {
            throw new Exception("잘못된 비밀번호 입니다.");
        }

    }

    // 회원탈퇴
    public boolean deleteMember(String memberId) throws Exception  {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberRepository.delete(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 닉네임 변경하기
    public boolean changeNickname(String memberId, String nickname) throws Exception {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));

        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setNickname(nickname);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

    // 프로필 이미지 변경하기
    public boolean changeImage(String memberId, MultipartFile image) {
        MemberEntity member = memberRepository.findById(Long.parseLong(memberId))
                .orElseThrow(() -> new CustomHandler(ErrorStatus.MEMBER_NOT_FOUND));

        //수정: 사진 자체의 이름이 길면 db에 안 올라가서 그 부분은 지웠습니다!
        String uuid = UUID.randomUUID().toString();
        Uuid savedUuid = uuidRepository.save(Uuid.builder().uuid(uuid).build());

        //이미 프사가 있으면 s3에서 지우기
        if (member.getImage() != null){
            String fileKeyName = member.getImage().split("com/")[1];
            s3Manager.deleteFile(fileKeyName);
        }

        String imageUrl = s3Manager.uploadFile(s3Manager.generateMemberKeyName(savedUuid), image); // 이미지 업로드하기
        member.setImage(imageUrl); // 프로필 이미지 변경
        memberRepository.save(member);

        return true;
    }
    // 프로필 메세지 변경하기
    public boolean changeProfileMessage(String memberId, String profileMessage) throws Exception {
        Optional<MemberEntity> member = memberRepository.findById(Long.parseLong(memberId));

        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setProfileMessage(profileMessage);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입된 회원이 없습니다.");
        }
    }

}

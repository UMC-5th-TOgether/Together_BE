package com.backend.together.domain.member.service;

import com.backend.together.domain.member.entity.MemberEntity;
import com.backend.together.domain.member.entity.enums.Provider;
import com.backend.together.domain.member.repository.MemberRepository;
import com.backend.together.domain.member.social.*;
import com.backend.together.global.security.TokenProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.service.DefaultMessageService;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Slf4j
@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private TokenProvider tokenProvider;
    private DefaultMessageService messageService;
    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    @Autowired
    private JavaMailSender javaMailSender;


    @Value("${spring.jwt.secretKey}")
    private String secretKey;
    @Value("${spring.kakao.client_id}")
    private String kakao_client_id;
    @Value("${spring.kakao.redirect_uri}")
    private String kakao_redirect_uri;
    @Value("${spring.google.client_id}")
    private String google_client_id;
    @Value("${spring.google.client_secret}")
    private String google_client_secret;
    @Value("${spring.google.redirect_uri}")
    private String google_redirect_uri;
    @Value("${spring.naver.client_id}")
    private String naver_client_id;
    @Value("${spring.naver.client_secret}")
    private String naver_client_secret;
    @Value("${spring.naver.redirect_uri}")
    private String naver_redirect_uri;
    @Value("${spring.coolsms.key}")
    private String coolsms_api_key;
    @Value("${spring.coolsms.secret}")
    private String coolsms_api_secret;
/*
    @Autowired
    public MemberService(JavaMailSender javaMailSender){
        this.javaMailSender = javaMailSender;
    }*/

    public void create(final MemberEntity memberEntity) throws Exception {
            if(memberRepository.existsByEmail(memberEntity.getEmail())){ // 이메일 중복 불가!
            throw new RuntimeException("이미 존재하는 이메일입니다.");
        }
        memberRepository.save(memberEntity);
    }

    public MemberEntity getByCredentials(final String email, final String password , final PasswordEncoder encoder){
        // 인증정보로 Member 검색.
        final Optional<MemberEntity> originalUser = memberRepository.findByEmail(email);
        if(originalUser.isPresent() && encoder.matches(password,originalUser.get().getPassword())){
            return originalUser.get();
        }
        return null;

        // return userRepository.findByEmailAndPassword(email,password);
    }

    // 이름과 전화번호(실명인증)으로 계정 찾기
    public String findEmailByNameAndPhoneNumber(final String name, final String phoneNumber) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByNameAndPhoneNumber(name,phoneNumber);
        if(member.isPresent()) {
            return member.get().getEmail();
        }
        else{
            throw new Exception("해당 정보로 가입된 회원이 없습니다.");
        }
    }

    // 이메일로 비밀번호 찾기 *** 메일로 임시 비밀번호 보내기
    public boolean findPasswordByEmail(final String email) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();

            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            String randomString = RandomStringUtils.randomAlphabetic(10);
            String tempPassword = passwordEncoder.encode(randomString);

            memberEntity.setPassword(tempPassword);
            memberRepository.save(memberEntity);

            simpleMailMessage.setTo(email);
            simpleMailMessage.setFrom("together.umc@gmail.com");
            simpleMailMessage.setSubject("[TOgether] 비밀번호 찾기 메일입니다.");
            simpleMailMessage.setText("임시 비밀번호: "+randomString);
            javaMailSender.send(simpleMailMessage);

            return true;
        }
        else{
            throw new Exception("가입되지 않은 이메일입니다.");
        }
    }

    // 비밀번호 변경하기
    public boolean changePassword(final String email,String password) throws Exception {
        final Optional<MemberEntity> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            MemberEntity memberEntity = member.get();
            memberEntity.setPassword(password);
            memberRepository.save(memberEntity);
            return true;
        }
        else {
            throw new Exception("가입되지 않은 이메일입니다.");
        }
    }

    // 구글 로그인
    public GoogleToken getGoogleToken(String code) {
        RestTemplate rt = new RestTemplate();
        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //바디 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", google_client_id);
        params.add("redirect_uri", google_redirect_uri);
        params.add("code", code);
        params.add("client_secret", google_client_secret);

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> googleTokenRequest =
                new HttpEntity<>(params, headers);
        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://oauth2.googleapis.com/token",
                HttpMethod.POST,
                googleTokenRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleToken googleToken = null;
        try {
            googleToken = objectMapper.readValue(response.getBody(), GoogleToken.class);
        }catch (JsonMappingException e) {
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return googleToken;
    }

    public GoogleProfile getGoogleProfile(GoogleToken googleToken) {
        RestTemplate rt = new RestTemplate();

        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+googleToken.access_token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> googleProfileRequest =
                new HttpEntity<>(headers);

        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://openidconnect.googleapis.com/v1/userinfo", // ******버전확인하기
                HttpMethod.GET,
                googleProfileRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        GoogleProfile googleProfile = null;
        try {
            googleProfile = objectMapper.readValue(response.getBody(), GoogleProfile.class);
        }catch (JsonMappingException e) {
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return googleProfile;
    }

    public String googleLogin(MemberEntity memberEntity) throws Exception{
        Optional<MemberEntity> findMember = memberRepository.findByEmail(memberEntity.getEmail());
        if (findMember.isEmpty()) { //해당 이메일의 유저가 없다면 회원가입 하기
            memberRepository.save(memberEntity);
            String token = tokenProvider.create(memberEntity);

            return token;
        } else if(findMember.get().getProvider().equals(Provider.GOOGLE)) {
            // 구글로 이미 가입한 경우 로그인 하기
            MemberEntity member = findMember.get();
            String token = tokenProvider.create(member);

            return token;
        } else { // 구글이 아닌 계정으로 해당 이메일이 존재하는 경우
            throw new Exception("이미 존재하는 이메일입니다.");
        }

    }

    // 카카오 로그인
    public KakaoToken getKakaoToken(String code) {
        RestTemplate rt = new RestTemplate();
        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //바디 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", kakao_client_id);
        params.add("redirect_uri", kakao_redirect_uri);
        params.add("code", code);
        //params.add("client_secret", google_client_secret);

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest =
                new HttpEntity<>(params, headers);
        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://kauth.kakao.com/oauth/token",
                HttpMethod.POST,
                kakaoTokenRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoToken kakaoToken = null;
        try {
            kakaoToken = objectMapper.readValue(response.getBody(), KakaoToken.class);
        }catch (JsonMappingException e) {
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return kakaoToken;
    }

    public KakaoProfile getKakaoProfile(KakaoToken kakaoToken) {
        RestTemplate rt = new RestTemplate();

        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+kakaoToken.getAccess_token());
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> kakaoProfileRequest =
                new HttpEntity<>(headers);

        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://kapi.kakao.com/v2/user/me",
                HttpMethod.POST,
                kakaoProfileRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        KakaoProfile kakaoProfile = null;
        try {
            kakaoProfile = objectMapper.readValue(response.getBody(), KakaoProfile.class);
        }catch (JsonMappingException e) {
            System.out.println("응답값 : " + response);
            e.printStackTrace(); // 여기서 오류 발생
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }

        return kakaoProfile;
    }

    public String kakaoLogin(MemberEntity memberEntity) throws Exception {
        Optional<MemberEntity> findMember = memberRepository.findByEmail(memberEntity.getEmail());

        if (findMember.isEmpty()) { //해당 이메일의 유저가 없다면 회원가입 하기
            memberRepository.save(memberEntity);
            String token = tokenProvider.create(memberEntity);

            return token;
        } else if(findMember.get().getProvider().equals(Provider.KAKAO)) {
            // 카카오로 이미 가입한 경우 로그인 하기
            MemberEntity member = findMember.get();
            String token = tokenProvider.create(member);

            return token;
        } else { // 카카오가 아닌 계정으로 해당 이메일이 가입되어 있는 경우
            throw new Exception("이미 존재하는 이메일입니다.");
        }
    }

    // 네이버 로그인
    public NaverToken getNaverToken(String code) {
        RestTemplate rt = new RestTemplate();
        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //바디 오브젝트 생성
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", naver_client_id);
        params.add("client_secret", naver_client_secret);
        params.add("code", code);
        params.add("state", "together"); // 임시값

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> naverTokenRequest =
                new HttpEntity<>(params, headers);
        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://nid.naver.com/oauth2.0/token",
                HttpMethod.POST,
                naverTokenRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        NaverToken naverToken = null;
        try {
            naverToken = objectMapper.readValue(response.getBody(), NaverToken.class);
            //System.out.println("응답값 : " + response);
        }catch (JsonMappingException e) {
            e.printStackTrace();
            System.out.println("응답값 : " + response);
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return naverToken;
    }

    public NaverProfile getNaverProfile(NaverToken naverToken) {
        RestTemplate rt = new RestTemplate();

        //헤더 오브젝트 생성
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer "+naverToken.access_token);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        //헤더와 바디 합쳐서 오브젝트 생성
        HttpEntity<MultiValueMap<String, String>> naverProfileRequest =
                new HttpEntity<>(headers);

        //요청 보내기
        ResponseEntity<String> response = rt.exchange(
                "https://openapi.naver.com/v1/nid/me",
                HttpMethod.GET,
                naverProfileRequest,
                String.class  //응답받을 타입
        );

        ObjectMapper objectMapper = new ObjectMapper();
        NaverProfile naverProfile = null;
        try {
            naverProfile = objectMapper.readValue(response.getBody(), NaverProfile.class);
        }catch (JsonMappingException e) {
            System.out.println("응답값 : " + response);
            e.printStackTrace();
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return naverProfile;
    }

    public String naverLogin(MemberEntity memberEntity) throws Exception{
        Optional<MemberEntity> findMember = memberRepository.findByEmail(memberEntity.getEmail());
        if (findMember.isEmpty()) { //해당 이메일의 유저가 없다면 회원가입 하기
            memberRepository.save(memberEntity);
            String token = tokenProvider.create(memberEntity);

            return token;
        } else if(findMember.get().getProvider().equals(Provider.NAVER)) {
            // 네이버로 이미 가입한 경우 로그인 하기
            MemberEntity member = findMember.get();
            String token = tokenProvider.create(member);

            return token;
        } else { // 네이버가 아닌 계정으로 해당 이메일이 존재하는 경우
            throw new Exception("이미 존재하는 이메일입니다.");
        }

    }

    @PostConstruct
    private void init(){
        this.messageService = NurigoApp.INSTANCE.initialize(coolsms_api_key, coolsms_api_secret, "https://api.coolsms.co.kr");
    }
    // 단일 메시지 발송 예제
    public SingleMessageSentResponse sendOne(String to, String verificationCode) {

        Message message = new Message();
        // 발신번호 및 수신번호는 반드시 01012345678 형태로 입력되어야 합니다.
        message.setFrom("01025925840"); // 발신번호
        message.setTo(to); // 수신번호
        message.setText("[TOgether] 아래의 인증번호를 입력해주세요\n" + verificationCode);

        SingleMessageSentResponse response = messageService.sendOne(new SingleMessageSendingRequest(message));
        return response;
    }

    // 이메일 중복
    public boolean checkEmail(String email) {
        final Optional<MemberEntity> member = memberRepository.findByEmail(email);
        if(member.isPresent()) {
            return false;
        }
        else {
            return true;
        }
    }
    
}

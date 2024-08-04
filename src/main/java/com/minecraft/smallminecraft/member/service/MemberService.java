package com.minecraft.smallminecraft.member.service;


import com.minecraft.smallminecraft.member.dtos.*;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import com.minecraft.smallminecraft.response.ErrorResponse;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class MemberService {

    private final MemberRepository memberRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final SendService sendService;

    public MemberService(MemberRepository memberRepository, BCryptPasswordEncoder bCryptPasswordEncoder, SendService sendService) {
        this.memberRepository = memberRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.sendService = sendService;
    }

    /*
     * 회원가입
     * 이미 존재하는 아이디면 400 전달
     * 성공하면 200 전달
     */
    @Transactional
    public ResponseEntity<Object> join(JoinDTO dto) {

        Member member = memberRepository.findByUsername(dto.getUsername());

        if (member != null) {
            log.info("이미 존재하는 아이디: {}", dto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 아이디입니다."));
        }

        member = memberRepository.findByEmail(dto.getEmail());

        if (member != null) {
            log.info("이미 존재하는 이메일: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 이메일입니다."));
        }

        Member joinMember = new Member();
        joinMember.setUsername(dto.getUsername());
        joinMember.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        joinMember.setEmail(dto.getEmail());
        joinMember.setRole("user");

        joinMember = memberRepository.save(joinMember);

        log.info("회원가입 성공: {}", joinMember.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /*
     * 비밀번호 찾기
     * 존재하지 않는 아이디면 400전달
     * 존재한다면 회원정보에 있는 이메일로 임시 비밀번호 전달
     */
    @Transactional
    public ResponseEntity<Object> findPW(PwRequestDTO dto) {
        Member member = memberRepository.findByUsername(dto.getUsername());
        if (member == null) {
            log.info("존재하지 않는 아이디: {}", dto.getUsername());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("존재하지 않는 아이디입니다."));
        } else {
            return sendEmail(dto, member.getEmail());
        }
    }

    /*
     * 임시 비밀번호 이메일 전송
     */
    @Transactional
    public ResponseEntity<Object> sendEmail(PwRequestDTO dto, String email) {
        MailDTO mailDTO = sendService.createMailAndChargePassword(dto, email);
        sendService.mailSend(mailDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /*
     * 비밀번호 변경
     * 존재하지 않는 회원이면 400전달
     * 기존 비밀번호와 일치하지 않으면 400전달
     * 비밀번호 변경 완료하면 200전달
     */
    @Transactional
    public ResponseEntity<Object> setPassword(SetPasswordDTO dto, String username) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            log.info("존재하지 않는 회원: {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("존재하지 않는 회원입니다."));
        }
        if (bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(dto.getNew_password()));
            log.info("비밀번호 변경 완료");
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        } else {
            log.info("비밀번호가 일치하지 않음: {}", username);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("비밀번호가 틀렸습니다."));
        }
    }

    /*
    * 아이디 찾기
    * 존재하지 않는 회원이면 400전달
    * 존재하면 200과 함께 아이디 전달
    */
    @Transactional
    public ResponseEntity<Object> findUsername(IDRequestDTO dto) {
        Member member = memberRepository.findByEmail(dto.getEmail());
        if (member == null) {
            log.info("존재하지 않는 회원: {}", dto.getEmail());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("존재하지 않는 회원입니다."));
        }
        IDResponseDTO responseDTO = new IDResponseDTO();
        responseDTO.setUsername(member.getUsername());
        log.info("아이디 찾기 성공: {}", member.getUsername());
        return ResponseEntity.status(HttpStatus.OK)
                .body(responseDTO);
    }

    /*
    * 아이디 중복 체크
    * 존재하는 아이디면 400 전달
    * 존재하지 않으면 200
    * */
    @Transactional
    public ResponseEntity<Object> uesrnameCheck(String username) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            log.info("이미 존재하는 아이디: {}", username);
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 아이디입니다."));
        }
    }

    /*
     * 이메일 중복 체크
     * 존재하는 이메일이면 400 전달
     * 존재하지 않으면 200
     * */
    @Transactional
    public ResponseEntity<Object> emilCheck(String email) {
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        }
        else {
            log.info("이미 존재하는 이메일: {}", email);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 이메일입니다."));
        }
    }
}

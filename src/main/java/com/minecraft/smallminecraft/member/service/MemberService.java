package com.minecraft.smallminecraft.member.service;


import com.minecraft.smallminecraft.member.dtos.JoinDTO;
import com.minecraft.smallminecraft.member.dtos.MailDTO;
import com.minecraft.smallminecraft.member.dtos.PwRequestDTO;
import com.minecraft.smallminecraft.member.dtos.SetPasswordDTO;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.ErrorResponse;

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
     * 이미 존재하는 아이디면 404 전달
     * 성공하면 200 전달
     */
    public ResponseEntity<Object> join(JoinDTO dto) {

        Member member = memberRepository.findByUsername(dto.getUsername());

        if (member != null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("이미 존재하는 아이디입니다."));
        }

        Member joinMember = new Member();
        joinMember.setUsername(dto.getUsername());
        joinMember.setPassword(bCryptPasswordEncoder.encode(dto.getPassword()));
        joinMember.setIp(dto.getIp());
        joinMember.setEmail(dto.getEmail());
        joinMember.setRole("user");

        joinMember = memberRepository.save(joinMember);

        log.info("join complete = {}", joinMember.getUsername());

        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /*
     * 비밀번호 찾기
     */
    public ResponseEntity<Object> findPW(PwRequestDTO dto) {
        Member member = memberRepository.findByUsername(dto.getUsername());
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 아이디입니다."));
        } else {
            return sendEmail(dto, member.getEmail());
        }
    }

    /*
     * 임시 비밀번호 이메일 전송
     */
    public ResponseEntity<Object> sendEmail(PwRequestDTO dto, String email) {
        MailDTO mailDTO = sendService.createMailAndChargePassword(dto, email);
        sendService.mailSend(mailDTO);
        return ResponseEntity.status(HttpStatus.OK)
                .build();
    }

    /*
     * 비밀번호 변경
     */
    @Transactional
    public ResponseEntity<Object> setPassword(SetPasswordDTO dto, String username) {
        Member member = memberRepository.findByUsername(username);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("존재하지 않는 회원입니다."));
        }
        if (bCryptPasswordEncoder.matches(dto.getPassword(), member.getPassword())) {
            member.setPassword(bCryptPasswordEncoder.encode(dto.getNew_password()));
            log.info("비밀번호 변경 완료");
            return ResponseEntity.status(HttpStatus.OK)
                    .build();
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("비밀번호가 틀렸습니다."));
        }
    }
}

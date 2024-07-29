package com.minecraft.smallminecraft.member.service;


import com.minecraft.smallminecraft.member.dtos.MailDTO;
import com.minecraft.smallminecraft.member.dtos.PwRequestDTO;
import com.minecraft.smallminecraft.member.entity.Member;
import com.minecraft.smallminecraft.member.repository.MemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SendService {

    private JavaMailSender mailSender;
    private static final String FROM_ADDRESS = "yjinlee99@gmail.com";
    private final BCryptPasswordEncoder passwordEncoder;
    private final MemberRepository memberRepository;

    public SendService(JavaMailSender mailSender, BCryptPasswordEncoder passwordEncoder, MemberRepository memberRepository) {
        this.mailSender = mailSender;
        this.passwordEncoder = passwordEncoder;
        this.memberRepository = memberRepository;
    }


    @Transactional
    public MailDTO createMailAndChargePassword(PwRequestDTO requestDto, String email) {
        String str = getTempPassword();
        MailDTO dto = new MailDTO();
        dto.setAddress(email);
        dto.setTitle(requestDto.getUsername() + "님의 임시비밀번호 안내 이메일 입니다.");
        dto.setMessage("안녕하세요. 임시비밀번호 안내 관련 메일 입니다." + "[" + requestDto.getUsername() + "]" + "님의 임시 비밀번호는 "
                + str + " 입니다.");
        updatePassword(str, requestDto);
        return dto;
    }

    @Transactional
    public void updatePassword(String str, PwRequestDTO requestDto) {
        String pw = passwordEncoder.encode(str);

        Member member = memberRepository.findByUsername(requestDto.getUsername());
        member.setPassword(pw);
    }

    public String getTempPassword() {
        char[] charSet = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F',
                'G', 'H', 'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'};
        String str = "";

        int idx = 0;
        for (int i = 0; i < 10; i++) {
            idx = (int) (charSet.length * Math.random());
            str += charSet[idx];
        }
        return str;
    }

    public void mailSend(MailDTO mailDto) {

        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(mailDto.getAddress());
        message.setFrom(FROM_ADDRESS);
        message.setSubject(mailDto.getTitle());
        message.setText(mailDto.getMessage());

        mailSender.send(message);

        log.info("임시 비밀번호 이메일 전송 완료");
    }
}

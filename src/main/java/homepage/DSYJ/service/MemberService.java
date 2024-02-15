package homepage.DSYJ.service;


import homepage.DSYJ.domain.Member;
import homepage.DSYJ.dto.CustomUserDetails;
import homepage.DSYJ.repository.SpringDataJpaMemberRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Transactional
@Slf4j
public class MemberService implements UserDetailsService {
    private final SpringDataJpaMemberRepository memberRepository;
    private final MailService mailService;
    private final AuthCodeService authCodeService;

    @Autowired @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberService(SpringDataJpaMemberRepository memberRepository,
                         MailService mailService,
                         AuthCodeService authCodeService) {
        this.memberRepository = memberRepository;
        this.mailService = mailService;
        this.authCodeService = authCodeService;
    }

    public void join(Member member) {
        validateDuplicateMember(member); //중복 회원 검증

        member.setPassword(bCryptPasswordEncoder.encode(member.getPassword()));
        member.setRole("ROLE_USER");
        if(member.getUserId().equals("DSYJ")){
            member.setRole("ROLE_ADMIN");
        }

        memberRepository.save(member);
    }

    public boolean isPasswordMatching(String password, String password2) {
        return password.equals(password2);
    }

    private void validateDuplicateMember(Member member) {
        memberRepository.findByUserId(member.getUserId())
                .ifPresent(m -> {
                    throw new IllegalStateException("이미 존재하는 아이디입니다.");
                });
    }

    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Member member = memberRepository.findByUserId(userId)
                .orElseThrow(() -> new UsernameNotFoundException("사용자를 찾을 수 없습니다."));

        return new CustomUserDetails(member);
    }

    public List<Member> findMember() {
        return memberRepository.findAll();
    }

    public Optional<Member> findOne(String memberId) {
        return memberRepository.findByUserId(memberId);
    }

    public Member findByUserIdAndPassword(String userId, String password) {
        return memberRepository.findByUserIdAndPassword(userId, password)
                .orElse(null); // orElse를 사용하여 Optional에서 Member 객체를 꺼냅니다.
    }

    @Transactional
    public void updateMember(Member member) {
        Member existingMember = memberRepository.findById(member.getId())
                .orElseThrow(() -> new IllegalArgumentException("사용자가 존재하지 않습니다."));

        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setPassword(member.getPassword());

        memberRepository.save(existingMember);
    }

    public void sendCodeToEmail(String toEmail) {
        this.checkDuplicatedEmail(toEmail);
        String title = "Data Science 홈페이지 이메일 인증 번호";
        String authCode = this.createCode();
        mailService.sendEmail(toEmail, title, authCode);
        // 이메일 인증 요청 시 인증 번호 authCode 저장 ( key = "AuthCode " + Email / value = AuthCode )
        authCodeService.saveAuthCode(toEmail, authCode);
    }

    private void checkDuplicatedEmail(String email) {
        Optional<Member> member = memberRepository.findByEmail(email);
        if (member.isPresent()) {
            log.debug("MemberServiceImpl.checkDuplicatedEmail exception occur email: {}", email);
            throw new IllegalArgumentException("Invalid argument");
        }
    }

    private String createCode() {
        int length = 6;
        try {
            Random random = SecureRandom.getInstanceStrong();
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < length; i++) {
                builder.append(random.nextInt(10));
            }
            return builder.toString();
        } catch (NoSuchAlgorithmException e) {
            log.debug("MemberService.createCode() exception occur");
            throw new IllegalArgumentException("Invalid argument");
        }
    }

    public boolean verifiedCode(String email, String authCode) {
        this.checkDuplicatedEmail(email);
        String redisAuthCode = authCodeService.getAuthCode(email);

        return authCodeService.checkExistsValue(redisAuthCode) && redisAuthCode.equals(authCode);
    }
}


package homepage.DSYJ.service;


import homepage.DSYJ.domain.Member;
import homepage.DSYJ.dto.CustomUserDetails;
import homepage.DSYJ.repository.SpringDataJpaMemberRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService implements UserDetailsService {
    private final SpringDataJpaMemberRepository memberRepository;

    @Autowired @Lazy
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public MemberService(SpringDataJpaMemberRepository memberRepository) {
        this.memberRepository = memberRepository;
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
}


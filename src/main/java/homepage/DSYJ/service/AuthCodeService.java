package homepage.DSYJ.service;

import homepage.DSYJ.repository.AuthCodeRepository;

public class AuthCodeService {
    private final AuthCodeRepository authCodeRepository;

    public AuthCodeService(AuthCodeRepository authCodeRepository) {
        this.authCodeRepository = authCodeRepository;
    }

    public void saveAuthCode(String email, String authCode) {
        String key = "AuthCode " + email;
        authCodeRepository.save(key, authCode);
    }

    public String getAuthCode(String email) {
        String key = "AuthCode " + email;
        return authCodeRepository.findById(key).orElse(null);
    }

    public boolean checkExistsValue(String authCode) {
        // authCode가 null이 아니고 비어있지 않으면 Redis에 값이 존재한다고 판단
        return authCode != null && !authCode.isEmpty();
    }
}

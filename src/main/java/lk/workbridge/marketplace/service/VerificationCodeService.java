package lk.workbridge.marketplace.service;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service

public class VerificationCodeService {

    private final Map<String, CodeEntry> codeStore = new ConcurrentHashMap<>();

    public void saveVerificationCode(String email, String code, int expiryMinutes) {
        CodeEntry entry = new CodeEntry(code,
                LocalDateTime.now().plusMinutes(expiryMinutes));
        codeStore.put(email, entry);
    }

    public boolean validateCode(String email, String code) {
        CodeEntry entry = codeStore.get(email);
        if (entry == null) return false;

        // Check if expired
        if (entry.expiryTime.isBefore(LocalDateTime.now())) {
            codeStore.remove(email);
            return false;
        }

        return entry.code.equals(code);
    }

    @Data
    @AllArgsConstructor
    private static class CodeEntry {
        private String code;
        private LocalDateTime expiryTime;
    }
}

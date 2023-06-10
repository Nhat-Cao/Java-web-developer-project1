package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final UserService userService;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, UserService userService, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.userService = userService;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(){
        Integer userId = userService.getCurrentUser().getUserId();
        return credentialMapper.getCredentials(userId);
    }

    public int updateCredential(Credential credential){
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), encodedKey);

        if (credential.getCredentialId() != null){
            return credentialMapper.update(
                    new Credential(
                            credential.getCredentialId(),
                            credential.getUrl(),
                            credential.getUsername(),
                            encodedKey,
                            encryptedPassword,
                            credential.getUserId()
                    )
            );
        } else {
            Integer userId = userService.getCurrentUser().getUserId();
            return credentialMapper.insert(
                    new Credential(
                            null,
                            credential.getUrl(),
                            credential.getUsername(),
                            encodedKey,
                            encryptedPassword,
                            userId
                    )
            );
        }
    }

    public int deleteCredential(int credentialId) {
        return credentialMapper.delete(credentialId);
    }
}

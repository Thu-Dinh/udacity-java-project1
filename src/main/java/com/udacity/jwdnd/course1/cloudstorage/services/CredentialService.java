package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper, EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    public List<Credential> getCredentials(int userId) {
        return credentialMapper.getCredentialsByUserId(userId);
    }

    public Credential getDetailCredential(int credentialId) {
        Credential credential = credentialMapper.getCredential(credentialId);
        String decodePwd = this.encryptionService.decryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(decodePwd);
        return credential;
    }

    public int addCredential(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = this.encryptionService.encryptValue(credential.getPassword(), encodedSalt);
        credential.setPassword(hashedPassword);
        credential.setKey(encodedSalt);
        return credentialMapper.addCredential(credential);
    }

    public boolean deleteCredentail(int credentialId) {
        return credentialMapper.deleteById(credentialId);
    }

    public boolean updateCredential(Credential credential) {
        Credential oldCredential = credentialMapper.getCredential(credential.getCredentialId());
        String hashedPassword = this.encryptionService.encryptValue(credential.getPassword(), oldCredential.getKey());
        credential.setPassword(hashedPassword);
        return credentialMapper.updateCredential(credential);
    }
}

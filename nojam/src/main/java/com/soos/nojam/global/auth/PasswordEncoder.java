package com.soos.nojam.global.auth;

import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;

public class PasswordEncoder {

    private final int ITERATIONS = 10;
    private final int MEMORY = 65536;
    private final int PARALLELISM = 1;
    private final int HASH_LENGTH = 64;
    private final byte[] SALT;

    public PasswordEncoder(){
        this.SALT = generateSalt16Byte();
    }
    public String encodePassword(String password){
        Argon2Parameters.Builder builder = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withVersion(Argon2Parameters.ARGON2_VERSION_13)
                .withSalt(SALT)
                .withIterations(ITERATIONS)
                .withMemoryAsKB(MEMORY)
                .withParallelism(PARALLELISM);

        Argon2Parameters parameters = builder.build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);

        byte[] result = new byte[HASH_LENGTH];
        generator.generateBytes(password.getBytes(StandardCharsets.UTF_8),
                result, 0, result.length);

        return new String(Hex.encode(result));
    }
    public boolean verifyPassword(String password, String encodedPassword){
        byte[] decodedPassword = Hex.decode(encodedPassword);
        String newEncodedPassword = encodePassword(password);
        return newEncodedPassword.equals(encodedPassword);
    }

    private byte[] generateSalt16Byte() {
        SecureRandom secureRandom = new SecureRandom();
        byte[] salt = new byte[16];
        secureRandom.nextBytes(salt);
        return salt;
    }
}

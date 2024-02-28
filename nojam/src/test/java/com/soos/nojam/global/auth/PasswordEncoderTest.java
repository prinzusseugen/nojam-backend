package com.soos.nojam.global.auth;

import org.bouncycastle.util.encoders.Hex;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;


class PasswordEncoderTest {
    PasswordEncoder passwordEncoder = new PasswordEncoder();
    public boolean verifyPassword(String password, String encodedPassword){
        byte[] decodedPassword = Hex.decode(encodedPassword);
        String newEncodedPassword = passwordEncoder.encodePassword(password);
        return newEncodedPassword.equals(encodedPassword);
    }
    @Test
    void passwordEncode(){
        String password = "1q2w3e4r";
        String encodedPassword = passwordEncoder.encodePassword(password);
        System.out.println(encodedPassword);
        assertTrue(verifyPassword(password, encodedPassword));

    }

}
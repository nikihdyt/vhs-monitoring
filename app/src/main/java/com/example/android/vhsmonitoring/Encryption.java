package com.example.android.vhsmonitoring;
import java.util.Base64;

public class Encryption {
    String encodedString, decodedString;
    public String encrypt(String data) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            encodedString = Base64.getEncoder().encodeToString(data.getBytes());
        }
        return encodedString;
    }

    public String decrypt(String data) {
        byte[] decodedBytes = new byte[0];
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            decodedBytes = Base64.getDecoder().decode(data);
        }
        decodedString = new String(decodedBytes);
        return decodedString;
    }
}

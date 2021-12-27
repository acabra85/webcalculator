package com.acabra.mmind.utils;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class B64Helper {
    private static final Base64.Encoder B64_ENC = Base64.getEncoder();
    private static final Base64.Decoder B64_DEC = Base64.getDecoder();

    public static String encode(String text) {
        return B64_ENC.encodeToString(text.getBytes());
    }

    public static String decode(String encodedText) {
        return new String(B64_DEC.decode(encodedText), StandardCharsets.UTF_8);
    }
}

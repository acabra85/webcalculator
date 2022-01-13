package com.acabra.fsands.core;

import java.util.Locale;
import java.util.Random;

public class FixSpikePasswords {

    public static final int ROOM_PWD_LEN = 6;
    private static final String ALPHA_BET = "ABCDEFGHJKMNPQRSTUVWXYZ";
    private static final char[] SET = (ALPHA_BET + ALPHA_BET.toLowerCase(Locale.ENGLISH) + "oiL012345678$#@=").toCharArray();
    private static final int setLen = SET.length;

    public static String generateRandomPassword(int passwordLen) {
        Random r = new Random();
        if(passwordLen < 1) throw new IllegalArgumentException("must be larger than zero given: " + passwordLen);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < passwordLen; i++) {
            sb.append(SET[r.nextInt(setLen)]);
        }
        return sb.toString();
    }
}

package com.cmmr.permission.utils;

import java.util.Random;

public class PasswordUtil {

    public static final String[] words = {
            "a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n",
            "o", "p", "q", "r", "s", "t", "u", "v", "w", "x", "y", "z", "A", "B",
            "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P",
            "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"
    };

    public static final String[] nums = {"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    public static final String[] others = {"!", "@", "#", "$", "%", "^", "&", "*"};

    public static String randomPassword() {
        StringBuffer password = new StringBuffer();
        Random random = new Random(System.currentTimeMillis());
        boolean flag1 = true, flag2 = true;
        int length = random.nextInt(3) + 8; //随机生成的密码为8-11位
        for(int i=0; i<length; i++) {
            int j = random.nextInt(3) + 1;
            if(j == 1) {
                password.append(words[random.nextInt(words.length)]);
            }
            if(j == 2) {
                password.append(nums[random.nextInt(nums.length)]);
            }
            if(j ==3) {
                password.append(others[random.nextInt(others.length)]);
            }
        }
        return password.toString();
    }

}

package com.metahive.transport.server.common;

import org.springframework.stereotype.Component;

@Component
public class RandomGenerator {
    public String randomPassword(Integer length) {
        int f;
        char[] pwd = new char[length];
        for (int i = 0; i < length; i++) {
            //随机生成0，1，2三个数中的一个f，作为判断条件
            f = (int) (Math.random() * 3);
            //System.out.println(f);
            if (f == 0) {
                //生成0-9之间的随机数字
                pwd[i] = (char) ('0' + Math.random() * 10);
            } else if (f == 1) {
                //随机生成一个小写字母
                pwd[i] = (char) ('a' + Math.random() * 26);
            } else
                //随机生成一个大写字母
                pwd[i] = (char) ('A' + Math.random() * 26);

        }
        return new String(pwd);
    }
}

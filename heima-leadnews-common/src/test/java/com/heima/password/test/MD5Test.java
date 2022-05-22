package com.heima.password.test;

import com.heima.utils.common.BCrypt;
import com.heima.utils.common.MD5Utils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.util.DigestUtils;

public class MD5Test {

    public static void main(String[] args) {
        /**
         * md5加密  DegestUtils：spring框架提供的工具类
         */
        String md5Str = DigestUtils.md5DigestAsHex("abc".getBytes());
        System.out.println(md5Str);//900150983cd24fb0d6963f7d28e17f72     不管生成多少次都一样


        /**
         * md5 + salt(盐)方式
         */
        //uername:zhangsan  password:123   salt:随时字符串
        String salt = RandomStringUtils.randomAlphanumeric(10); //生成长度为10的随机字符串
        System.out.println(salt);   // kjevV4TRa7

        String pswd = "abc"+salt;
        String saltPswd = DigestUtils.md5DigestAsHex(pswd.getBytes());//进行加密
        System.out.println(saltPswd);  // d63b784c07d80388ff02d35e3a4facbd    每次返回的都不一样


        /**
         * BCrypt工具类方式
         */
        String gensalt = BCrypt.gensalt();//这个是盐  29个字符，随机生成
        System.out.println(gensalt);   // $2a$10$1nO1TkJeWi6wr5jfnJs49.
        String password = BCrypt.hashpw("abc", gensalt);  //根据盐对密码进行加密
        System.out.println(password);//加密后的字符串前29位就是盐     $2a$10$1nO1TkJeWi6wr5jfnJs49.2GoX5G3qcRORbbSolVQaQqb0eu2zLhS


        boolean abc = BCrypt.checkpw("abc", "$2a$10$1nO1TkJeWi6wr5jfnJs49.2GoX5G3qcRORbbSolVQaQqb0eu2zLhS");
        System.out.println(abc);



        String aaa = "xiaoliu"+"1234";
        String bbb = DigestUtils.md5DigestAsHex(aaa.getBytes());
        System.out.println(bbb);  // d63b784c07d80388ff02d35e3a4facbd    每次返回的都不一样
    }
}

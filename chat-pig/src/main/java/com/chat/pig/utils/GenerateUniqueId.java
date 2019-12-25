package com.chat.pig.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述: 成为16位唯一id
 *
 * @author li
 * @date 2019/12/25
 */
public class GenerateUniqueId {

    /**
     * 生成16位不重复id
     * @return id
     */
    public static String generate16Id(){
        String date = new SimpleDateFormat("yyMMdd").format(new Date());
        char[] bases = new char[]{'1','2','3','4','5','6','7','8','9'};
        //前六位
        List<Character> chars = date.chars().mapToObj(e -> (char)e).collect(Collectors.toList());
        Collections.shuffle(chars);
        String prefix6 = chars.stream().map(Object::toString).collect(Collectors.joining());
        //后10位
        String suffix10 = RandomStringUtils.random(1,bases) + RandomStringUtils.randomNumeric(9);
        return prefix6 + suffix10;
    }

}

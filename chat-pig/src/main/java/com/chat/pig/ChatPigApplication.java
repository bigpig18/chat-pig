package com.chat.pig;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import tk.mybatis.spring.annotation.MapperScan;

/**
 * 描述: 猪猪聊天启动类
 *
 * @author li
 * @date 2019/12/24
 */
@SpringBootApplication
@MapperScan(basePackages = "com.chat.pig.mapper")
public class ChatPigApplication {

    @Bean
    public SpringUtil getSpringUtil(){
        return new SpringUtil();
    }

    public static void main(String[] args) {
        SpringApplication.run(ChatPigApplication.class, args);
    }

}

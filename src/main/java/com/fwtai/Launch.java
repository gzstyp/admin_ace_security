package com.fwtai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement //开启事务控制
public class Launch{

    public static void main(String[] args){
        SpringApplication.run(Launch.class,args);
    }
}

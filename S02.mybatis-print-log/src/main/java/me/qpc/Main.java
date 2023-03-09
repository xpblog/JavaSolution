package me.qpc;


import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("me.qpc.mapper")
public class Main {
  public static void main(String[] args) {
    SpringApplication.run(Main.class, args);
  }
}
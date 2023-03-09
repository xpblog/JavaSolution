package me.qpc.controller;

import me.qpc.entity.Blog;
import me.qpc.mapper.BlogMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class HelloContorlller {

  @Resource
  private BlogMapper blogMapper;

  @GetMapping("/hello")
  public String hello() {
    List<Blog> select = blogMapper.select(1);
    return select.toString();
  }
}

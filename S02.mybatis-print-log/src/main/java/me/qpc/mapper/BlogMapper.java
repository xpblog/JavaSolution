package me.qpc.mapper;

import me.qpc.entity.Blog;

import java.util.List;

public interface BlogMapper {
  List<Blog> select(Integer id);
}

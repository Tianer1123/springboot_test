package com.xdclass.springboot_test.mapper;
/*
 * @Author: tianer1123
 * @Description: UserMapper
 * @CreateTime: 13:49 2019-05-28
 */

import com.xdclass.springboot_test.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Options;

public interface UserMapper {
    @Insert("INSERT INTO user(NAME, PHONE, CREATE_TIME, AGE) VALUES (#{name}, #{phone}, #{create_time}, #{age})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int inseart(User user);
}

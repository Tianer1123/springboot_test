package com.xdclass.springboot_test.mapper;
/*
 * @Author: tianer1123
 * @Description: UserMapper
 * @CreateTime: 13:49 2019-05-28
 */

import com.xdclass.springboot_test.domain.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface UserMapper {
    // 使用#{},不要使用${},因为存在注入风险
    @Insert("INSERT INTO user(NAME, PHONE, CREATE_TIME, AGE) VALUES (#{name}, #{phone}, #{createTime}, #{age})")
    @Options(useGeneratedKeys = true, keyColumn = "id")
    int inseart(User user);
}

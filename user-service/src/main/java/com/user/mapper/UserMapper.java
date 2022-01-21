package com.user.mapper;



import com.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;


/*****
 * @Author:
 * @Description:
 ****/

public interface UserMapper extends JpaRepository<User, String> {
}

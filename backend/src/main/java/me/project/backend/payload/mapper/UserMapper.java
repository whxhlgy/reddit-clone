package me.project.backend.payload.mapper;

import me.project.backend.domain.User;
import me.project.backend.payload.UserDTO;

public class UserMapper {
    static public UserDTO map(User user) {
        return UserDTO.builder().username(user.getUsername()).build();
    }
}

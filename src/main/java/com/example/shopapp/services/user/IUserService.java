package com.example.shopapp.services.user;

import com.example.shopapp.dtos.UpdateUserDTO;
import com.example.shopapp.dtos.UserDTO;
import com.example.shopapp.dtos.UserLoginDTO;
import com.example.shopapp.exceptions.DataNotFoundException;
import com.example.shopapp.exceptions.InvalidPasswordException;
import com.example.shopapp.models.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IUserService {
    User createUser(UserDTO userDTO) throws Exception;
    
    String login(UserLoginDTO userLoginDT) throws Exception;
    
    User getUserDetailsFromToken(String token) throws Exception;
    
    User getUserDetailsFromRefreshToken(String token) throws Exception;
    
    User updateUser(Long userId, UpdateUserDTO updatedUserDTO) throws Exception;

    Page<User> findAll(String keyword, Pageable pageable) throws Exception;
    
    void resetPassword(Long userId, String newPassword)
            throws InvalidPasswordException, DataNotFoundException;
    
    void blockOrEnable(Long userId, Boolean active) throws DataNotFoundException;
    
    void changeProfileImage(Long userId, String imageName) throws Exception;
}

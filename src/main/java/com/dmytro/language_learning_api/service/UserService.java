package com.dmytro.language_learning_api.service;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.requests.getRequests.GetUserDataDTO;
import com.dmytro.language_learning_api.dto.response.PageResponse;

public interface UserService {

    PageResponse<UsersDTO> getAllUsers(int pageNo, int pageSize);
    //List<UsersDTO> getAllUsers(int pageNo, int pageSize);
    GetUserDataDTO getUserByEmail(String email);

    UsersDTO createUser(UsersDTO dto);

    UsersDTO updateEmail(String oldEmail, String newEmail);

    UsersDTO updateUsernameByEmail(String currentEmail, String newUsername);

    void updatePasswordByEmail(String currentEmail,String oldPassword, String newPassword);

    void deleteUserByEmail(String email);

}

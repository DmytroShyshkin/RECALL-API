package com.dmytro.language_learning_api.service;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.requests.getRequests.GetUserDataDTO;

public interface UserService {

    GetUserDataDTO getUserByEmail(String email);

    UsersDTO updateEmail(String oldEmail, String newEmail);

    UsersDTO updateUsernameByEmail(String currentEmail, String newUsername);

    void updatePasswordByEmail(String currentEmail,String oldPassword, String newPassword);

    void deleteUserByEmail(String email);

}

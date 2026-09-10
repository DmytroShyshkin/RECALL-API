package com.dmytro.language_learning_api.controller;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.requests.getRequests.GetUserDataDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateEmailRequestDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdatePasswordRequestDTO;
import com.dmytro.language_learning_api.dto.requests.updateRequests.UpdateUsernameRequestDTO;
import com.dmytro.language_learning_api.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Validated
public class UsersController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<GetUserDataDTO> getUserByEmail(
            Authentication authentication
    ) {
        return ResponseEntity.ok(userService.getUserByEmail(authentication.getName()));
    }

    @PutMapping("/me/email")
    public ResponseEntity<UsersDTO> updateEmail(
            @Valid @RequestBody UpdateEmailRequestDTO request
            , Authentication authentication)
    {
        return ResponseEntity.ok(userService.updateEmail(authentication.getName(), request.email()));
    }

    @PutMapping("/me/username")
    public ResponseEntity<UsersDTO> updateUsername(
            @Valid @RequestBody UpdateUsernameRequestDTO request
            , Authentication authentication)
    {
        return ResponseEntity.ok(userService.updateUsernameByEmail(authentication.getName(), request.username()));
    }

    @PutMapping("/me/password")
    public ResponseEntity<Void> updatePassword(
            @RequestBody UpdatePasswordRequestDTO passwordRequest
            , Authentication authentication)
    {
        userService.updatePasswordByEmail(
                authentication.getName()
                , passwordRequest.oldPassword()
                , passwordRequest.newPassword()
        );

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/me")
    public ResponseEntity<Void> deleteUser(Authentication authentication) {
        userService.deleteUserByEmail(authentication.getName());

        return ResponseEntity.noContent().build();
    }

}

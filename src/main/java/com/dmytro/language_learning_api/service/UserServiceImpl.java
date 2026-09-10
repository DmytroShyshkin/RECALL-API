package com.dmytro.language_learning_api.service;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.dmytro.language_learning_api.dto.UsersDTO;
import com.dmytro.language_learning_api.dto.requests.getRequests.GetUserDataDTO;
import com.dmytro.language_learning_api.event.UserDeletedDomainEvent;
import com.dmytro.language_learning_api.exception.ConflictException.ConflictException;
import com.dmytro.language_learning_api.exception.ConflictException.EmailAlreadyExistsException;
import com.dmytro.language_learning_api.exception.ConflictException.UsernameAlreadyExistsException;
import com.dmytro.language_learning_api.exception.NotFoundException.NotFoundException;
import com.dmytro.language_learning_api.exception.NotFoundException.UserNotFoundException;
import com.dmytro.language_learning_api.mapper.UsersMapper;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.WordsRepository;
import com.dmytro.language_learning_api.repository.statistics.UserActivityRepository;
import com.dmytro.language_learning_api.repository.statistics.WordReviewLogRepository;
import com.dmytro.language_learning_api.repository.statistics.WordStatisticsRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UsersRepository usersRepository;
    private final WordsRepository wordsRepository;
    private final UserActivityRepository userActivityRepository;
    private final WordReviewLogRepository  wordReviewLogRepository;
    private final WordStatisticsRepository wordStatisticsRepository;

    private final ApplicationEventPublisher eventPublisher;

    private final UsersMapper usersMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public GetUserDataDTO getUserByEmail(String email) {
        Users user = getUserOrThrow(email);


        return new GetUserDataDTO(user.getUsername(), user.getEmail());
        //return usersMapper.toDto(user);
    }

    @Override
    public UsersDTO updateEmail(String oldEmail, String newEmail) {
        Users user = usersRepository.findByEmail(oldEmail).orElseThrow(
                ()-> new UserNotFoundException("User by email '" + oldEmail + "' not found")
        );

        if (usersRepository.existsByEmail(newEmail)) {
            throw new EmailAlreadyExistsException("Email: " + newEmail + " already in use");
        }

        user.setEmail(newEmail);
        usersRepository.save(user);

        return usersMapper.toDto(user);
    }

    @Override
    public UsersDTO updateUsernameByEmail(String currentEmail, String newUsername) {
        Users user = usersRepository.findByEmail(currentEmail).orElseThrow(
                ()-> new UserNotFoundException("User not found by email '" + currentEmail + "'"));

        if (usersRepository.existsByUsername(newUsername)) {
            throw new UsernameAlreadyExistsException("Username: " + newUsername + " already in use");
        }

        user.setUsername(newUsername);
        usersRepository.save(user);

        return usersMapper.toDto(user);
    }

    @Override
    public void updatePasswordByEmail(
            String currentEmail, String oldPassword, String newPassword
    ) {
        Users user = usersRepository.findByEmail(currentEmail).orElseThrow(
                ()-> new UserNotFoundException("User not found by email '" + currentEmail + "'"));

        if(oldPassword.equals(newPassword)) throw new ConflictException("You introduced same password");

        user.setPassword(passwordEncoder.encode(newPassword));
        usersRepository.save(user);
    }

    @Transactional
    public void deleteUserByEmail(String email) {
        Users user = usersRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found."));

        eventPublisher.publishEvent(new UserDeletedDomainEvent(email));

        wordStatisticsRepository.deleteByUserId(user.getId());
        wordReviewLogRepository.deleteByUserId(user.getId());
        userActivityRepository.deleteByUserId(user.getId());
        wordsRepository.deleteByOwnerId(user.getId());
        usersRepository.delete(user);
    }

    // Clases auxiliares
    private Users getUserOrThrow(String email) {
        return usersRepository.findByEmail(email)
                .orElseThrow(() -> new UserNotFoundException("User User with id " + email + " not found"));
    }
}

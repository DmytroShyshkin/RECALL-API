package com.dmytro.language_learning_api.controller;

import com.dmytro.language_learning_api.dto.statistics.UserStatsSummaryResponse;
import com.dmytro.language_learning_api.dto.statistics.WordStatisticsResponse;
import com.dmytro.language_learning_api.model.Users;
import com.dmytro.language_learning_api.model.statistics.WordStatistics;
import com.dmytro.language_learning_api.repository.UsersRepository;
import com.dmytro.language_learning_api.repository.statistics.WordStatisticsRepository;
import com.dmytro.language_learning_api.security.jwt.JwtUtil;
import com.dmytro.language_learning_api.service.statistics.StreakService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final WordStatisticsRepository statisticsRepository;
    private final StreakService streakService;
    private final JwtUtil jwtUtil;

    @GetMapping("/words")
    public List<WordStatisticsResponse> getWordStatistics() {
        Users user = jwtUtil.getCurrentUser();

        return statisticsRepository.findByUserId(user.getId()).stream()
                .map(s -> new WordStatisticsResponse(
                        s.getWordId(),
                        s.getTotalReviews(),
                        s.getCorrectReviews(),
                        s.getLapses(),
                        s.getLastReviewedAt()
                ))
                .toList();
    }

    @GetMapping("/summary")
    public UserStatsSummaryResponse getSummary() {
        Users user = jwtUtil.getCurrentUser();

        int streak = streakService.calculateCurrentStreak(user.getId());
        var stats = statisticsRepository.findByUserId(user.getId());

        int totalReviews = stats.stream().mapToInt(WordStatistics::getTotalReviews).sum();

        return new UserStatsSummaryResponse(streak, stats.size(), totalReviews);
    }
}
package com.dmytro.language_learning_api.service.statistics;

import com.dmytro.language_learning_api.model.statistics.UserActivity;
import com.dmytro.language_learning_api.repository.statistics.UserActivityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StreakService {

    private final UserActivityRepository userActivityRepository;

    public int calculateCurrentStreak(UUID userId) {
        List<UserActivity> activities = userActivityRepository
                .findByUserIdOrderByActivityDateDesc(userId);

        if (activities.isEmpty()) {
            return 0;
        }

        LocalDate today = LocalDate.now();
        LocalDate expectedDate = activities.get(0).getActivityDate();

        // If your last activity wasn't today or yesterday, your streak is broken
        if (!expectedDate.equals(today) && !expectedDate.equals(today.minusDays(1))) {
            return 0;
        }

        int streak = 0;
        for (UserActivity activity : activities) {
            if (activity.getActivityDate().equals(expectedDate)) {
                streak++;
                expectedDate = expectedDate.minusDays(1);
            } else {
                break;
            }
        }

        return streak;
    }
}
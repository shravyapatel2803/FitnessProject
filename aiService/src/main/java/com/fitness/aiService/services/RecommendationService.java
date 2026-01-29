package com.fitness.aiService.services;

import com.fitness.aiService.model.Recommendation;
import com.fitness.aiService.repo.RecommendationRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RecommendationService {
    private final RecommendationRepo repo;

    public List<Recommendation> getRecommendationsByUserId(String userId) {
        return repo.findAllByUserId(userId);
    }

    public Recommendation getRecommendationsByActivityId(String activityId) {
        return repo.findAllByActivityId(activityId).orElseThrow( () -> new RuntimeException("Activity not found "));
    }
}

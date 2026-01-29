package com.fitness.aiService.repo;

import com.fitness.aiService.model.Recommendation;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RecommendationRepo extends MongoRepository<Recommendation, String> {
    List<Recommendation> findAllByUserId(String userId);

    Optional<Recommendation> findAllByActivityId(String activityId);
}

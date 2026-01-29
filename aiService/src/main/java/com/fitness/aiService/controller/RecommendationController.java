package com.fitness.aiService.controller;

import com.fitness.aiService.model.Recommendation;
import com.fitness.aiService.services.RecommendationService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/recommendations")
@RequiredArgsConstructor
public class RecommendationController {
    private final RecommendationService service;
    @GetMapping("user/{userId}")
    public ResponseEntity<List<Recommendation>> getRecommendationsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(service.getRecommendationsByUserId(userId));
    }
    @GetMapping("activity/{activityId}")
    public ResponseEntity<Recommendation> getRecommendationsByActivityId(@PathVariable String activityId) {
        return ResponseEntity.ok(service.getRecommendationsByActivityId(activityId));
    }


}

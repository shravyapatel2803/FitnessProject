package com.fitness.activityService.service;


import com.fitness.activityService.dto.ActivityRequest;
import com.fitness.activityService.dto.ActivityResponse;
import com.fitness.activityService.model.Activity;
import com.fitness.activityService.repo.ActivityRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.Nullable;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ActivityService {

    @Autowired
    private final ActivityRepo activityRepo;

    @Autowired
    private final UserValidateService UserValidateService;

    private final RabbitTemplate rabbitTemplate;

    @Value("${rabbitmq.exchange.name}")
    private String exchange;
    @Value("${rabbitmq.routing.key}")
    private String routingKey;



    public ActivityResponse trackActivity(ActivityRequest request) {

        boolean isValidUser = UserValidateService.validateUser(request.getUserId());
        if (!isValidUser) {
            throw new RuntimeException("User not found");
        }

        Activity activity = Activity.builder()
                .userId(request.getUserId())
                .type(request.getType())
                .duration(request.getDuration())
                .caloriesBurned(request.getCaloriesBurned())
                .startAt(request.getStartAt())
                .additionalMetrics(request.getAdditionalMetrics())
                .build();

        Activity savedActivity = activityRepo.save(activity);
        //Publish to RabbitMQ for AI Processing
        try {
            rabbitTemplate.convertAndSend(exchange, routingKey, savedActivity);

        }catch(Exception e){
            log.error("Failed to publish the activity to rabbitmq" + e);
        }

        return getActivityResponse(savedActivity);

    }

    private static ActivityResponse getActivityResponse(Activity savedActivity) {
        ActivityResponse activityResponse = new ActivityResponse();
        activityResponse.setId(savedActivity.getId());
        activityResponse.setUserId(savedActivity.getUserId());
        activityResponse.setType(savedActivity.getType());
        activityResponse.setDuration(savedActivity.getDuration());
        activityResponse.setCaloriesBurned(savedActivity.getCaloriesBurned());
        activityResponse.setStartAt(savedActivity.getStartAt());
        activityResponse.setAdditionalMetrics(savedActivity.getAdditionalMetrics());
        activityResponse.setCreatedAt(savedActivity.getCreatedAt());
        activityResponse.setUpdatedAt(savedActivity.getUpdatedAt());
        return activityResponse;
    }

    public List<ActivityResponse> getActivityByUserID(String userId) {
        List<Activity> activity = activityRepo.findByUserId(userId);
        if(!UserValidateService.validateUser(userId)){
            throw new RuntimeException("User not found");
        }
        if (activity == null) {
            return null;
        }
        return activity.stream()
                .map(ActivityService::getActivityResponse)
                .collect(Collectors.toList());

    }

    public @Nullable Activity getActivityById(String activityId) {
        return activityRepo.findById(activityId)
                .orElseThrow(() -> new RuntimeException("Activity not found\nActivity ID: " + activityId + "\n"));
    }
}

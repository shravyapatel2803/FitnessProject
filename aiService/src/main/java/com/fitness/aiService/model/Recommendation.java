package com.fitness.aiService.model;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Document(collection = "aidatabase")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Recommendation {
    @Id
    private String id;
    private String activityId;
    private String userId;
    private String activityType;
    private String recommendation;
    @Field("improvements")
    private List<String> improvements;
    @Field("suggestion")
    private List<String> suggestion;
    @Field("safety")
    private List<String> safety;
    @CreatedDate
    private LocalDateTime createdAt;
}

package com.fitness.userService.repo;

import com.fitness.userService.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepo extends JpaRepository<User, String> {
    boolean existsByEmail(@NotBlank(message = "Email required") @Email(message = "Invalid email format") String email);

//    boolean existsByUserId(String userId);
}

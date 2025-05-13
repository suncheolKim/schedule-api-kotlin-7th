package net.sckim.scheduleapi.user;

import net.sckim.scheduleapi.user.entity.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findById(Long userId);

    int update(User user);
}

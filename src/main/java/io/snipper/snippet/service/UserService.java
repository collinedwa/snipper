package io.snipper.snippet.service;

import io.snipper.snippet.model.User;
import io.snipper.snippet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    final UserRepository userRepository;

    @Autowired
    public UserService(final UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User getUserById(final Long id) {
        try {
            final User response = userRepository.findById(id).orElseThrow(() -> new RuntimeException("user not found"));
            return response;
        } catch (Exception ex) {
            return null;
        }
    }

    public User getUserByEmail(final String email) {
        try {
            final User response = userRepository.findByEmail(email).orElseThrow(() -> new RuntimeException("user not found"));
            return response;
        } catch (Exception ex) {
            return null;
        }
    }

    public List<User> getAll() {
        return userRepository.findAll();
    }

    public void createUser(final User user) {
        userRepository.save(user);
    }

    public void deleteUser(final User user) {
        userRepository.delete(user);
    }
}

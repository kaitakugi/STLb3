package com.example.demo.service;

import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // Lấy tất cả User
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // Lấy User theo Id
    public User getUserById(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    // Tạo mới User
    public User createUser(User user) {
        return userRepository.save(user);
    }

    // Cập nhật User
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setPhone(userDetails.getPhone());
            user.setCompany(userDetails.getCompany());
            return userRepository.save(user);
        }
        return null;
    }

    // Xóa User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}

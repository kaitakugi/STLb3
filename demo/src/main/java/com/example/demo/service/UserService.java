package com.example.demo.service;

import com.example.demo.model.Company;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepository;
import com.example.demo.repository.CompanyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CompanyRepository companyRepository;

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
        if (user.getCompany() != null) {
            // Nếu Company đã có ID, lấy từ database, nếu không thì tạo mới
            if (user.getCompany().getId() != null) {
                Company existingCompany = companyRepository.findById(user.getCompany().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Company không tồn tại với ID: " + user.getCompany().getId()));
                user.setCompany(existingCompany);
            } else {
                // Lưu Company mới nếu chưa có ID
                user.setCompany(companyRepository.save(user.getCompany()));
            }
        }
        return userRepository.save(user);
    }

    // Cập nhật User
    public User updateUser(Long id, User userDetails) {
        User user = getUserById(id);
        if (user != null) {
            user.setUsername(userDetails.getUsername());
            user.setPhone(userDetails.getPhone());
            if (userDetails.getCompany() != null && userDetails.getCompany().getId() != null) {
                Company company = companyRepository.findById(userDetails.getCompany().getId())
                        .orElseThrow(() -> new IllegalArgumentException("Company không tồn tại với ID: " + userDetails.getCompany().getId()));
                user.setCompany(company);
            }
            return userRepository.save(user);
        }
        return null;
    }

    // Xóa User
    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }

    // Lưu User
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    // Lấy danh sách User theo Company ID
    public List<User> getUsersByCompanyId(Long companyId) {
        return userRepository.findByCompanyId(companyId);
    }
}
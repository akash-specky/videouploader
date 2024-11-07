package com.example.videouploader.serviceImpl;

import com.example.videouploader.model.User;
import com.example.videouploader.repo.AdminUserRepository;
import com.example.videouploader.service.AdminUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
@Service
public class AdminUserServiceImpl implements AdminUserService {


    @Autowired
    private AdminUserRepository adminRepository;

    @Override
    public User createUser(User user) {
        return adminRepository.save(user);
    }

    @Override
    public Optional<User> updateUser(String id, User userDetails) {
        Optional<User> optionalUser = adminRepository.findById(id);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            user.setName(userDetails.getName());
            user.setOccupation(userDetails.getOccupation());
            user.setEmail(userDetails.getEmail());
            user.setMobileNo(userDetails.getMobileNo());
            user.setAddress(userDetails.getAddress());
            user.setCountry(userDetails.getCountry());
            user.setPincode(userDetails.getPincode());
            user.setPassword(userDetails.getPassword());
            user.setSsoId(userDetails.getSsoId());
            return Optional.of(adminRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public void deleteUser(String id) {
        Optional<User> optionalUser = adminRepository.findById(id);
        optionalUser.ifPresent(user -> adminRepository.deleteById(id));
    }

    @Override
    public Optional<User> findUserById(String id) {
        return adminRepository.findById(id);
    }

    @Override
    public long countActiveUsers() {
        return adminRepository.countBySsoIdNotNull();
    }

    @Override
    public List<User> findUsersByName(String name) {
        return adminRepository.findByNameContainingIgnoreCase(name);
    }
}

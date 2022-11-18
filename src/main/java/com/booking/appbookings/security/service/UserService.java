package com.booking.appbookings.security.service;

import com.booking.appbookings.security.entity.User;
import com.booking.appbookings.security.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@Transactional
public class UserService {
    @Autowired
    UserRepository userRepository;
    public Optional<User> findByUserName(String userName){
        return userRepository.findByUserName(userName);
    }
    public boolean existsByUsername(String userName){
        return userRepository.existsByUserName(userName);
    }
    public boolean existsByEmail(String email){
        return userRepository.existsByEmail(email);
    }
    public void save(User user){
        userRepository.save(user);
    }
}

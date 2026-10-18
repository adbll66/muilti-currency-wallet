package com.wallet.mmulticurrencywallet.service;

import com.wallet.mmulticurrencywallet.model.Users;
import com.wallet.mmulticurrencywallet.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public Users registerUser(Users user) {

        // Потом надо поменять на деф только во время разработки
        if (user.getPasswordHash() == null || user.getPasswordHash().isEmpty()) {
            user.setPasswordHash("DEFAULT_PASSWORD_HASH");
        }
        return userRepository.save(user);
    }
    public Optional<Users>getUserById(Long id){
        return userRepository.findById(id);
    }
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }
}

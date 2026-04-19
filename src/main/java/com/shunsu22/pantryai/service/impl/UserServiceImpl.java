package com.shunsu22.pantryai.service.impl;

import com.shunsu22.pantryai.entity.User;
import com.shunsu22.pantryai.repository.UserRepository;
import com.shunsu22.pantryai.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(String username, String password) {

        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("このユーザー名はすでに使われています");
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password));

        return userRepository.save(user);
    }
}
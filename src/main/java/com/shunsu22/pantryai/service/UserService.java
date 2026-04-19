package com.shunsu22.pantryai.service;

import com.shunsu22.pantryai.entity.User;

public interface UserService {

    User register(String username, String password);

}
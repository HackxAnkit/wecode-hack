package com.wecode.bookit.dao;

import com.wecode.bookit.model.Users;

public interface LoginDAO {
    Users authenticate(String email, String password);
    void resetCredits(Users user);
}

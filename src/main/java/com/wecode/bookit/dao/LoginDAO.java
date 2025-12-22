package com.wecode.bookit.dao;

import com.wecode.bookit.domain.Users;

public interface LoginDAO {
    Users authenticate(String username, String password);
    void resetCredits(Users user);
}

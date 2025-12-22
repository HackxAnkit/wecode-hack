package com.wecode.bookit.services;

import com.wecode.bookit.domain.Users;

public interface LoginService {
    public Users login(String username, String password);
    public void resetCredits(Users user);
}

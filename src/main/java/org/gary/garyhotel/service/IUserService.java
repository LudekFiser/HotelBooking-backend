package org.gary.garyhotel.service;

import org.gary.garyhotel.model.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface IUserService {

    User registerUser(User user);
    List<User> getUsers();
    void deleteUser(String email);
    User getUser(String email);
}

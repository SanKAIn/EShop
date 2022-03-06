package com.kon.EShop.repository.impl;

import com.kon.EShop.model.userPack.User;
import com.kon.EShop.repository.userPack.UserRepository;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserImpl {
    private final UserRepository repository;

    public UserImpl(UserRepository repository) {
        this.repository = repository;
    }

    private static final Sort SORT_NAME_EMAIL = Sort.by(Sort.Direction.ASC, "name", "email");

    public User save(User user) {
        return repository.save(user);
    }

    public boolean delete(long id) {
        return repository.delete(id) != 0;
    }

    public User get(Long id) {
        return repository.findById(id).orElse(null);
    }

    public User getByEmail(String email) {
        return repository.getByEmail(email);
    }

    public List<User> getAll() {
        return repository.findAll(SORT_NAME_EMAIL);
    }
}

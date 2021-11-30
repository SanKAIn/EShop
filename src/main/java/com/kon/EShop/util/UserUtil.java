package com.kon.EShop.util;

import com.kon.EShop.model.Role;
import com.kon.EShop.model.User;
import com.kon.EShop.to.UserTo;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;

public class UserUtil {

    public static User createNewFromTo(UserTo userTo) {
        return new User(null, userTo.getName(), userTo.getEmail().toLowerCase(), userTo.getPassword(), userTo.getPhone(), Role.USER);
    }

    public static UserTo asTo(User user) {
        return new UserTo(user.getId(), user.getName(), user.getEmail(), user.getPassword(), user.getPhone());
    }

    public static User updateFromTo(User user, UserTo userTo) {
        user.setName(userTo.getName());
        user.setEmail(userTo.getEmail().toLowerCase());
        user.setPhone(userTo.getPhone());
        user.setPassword(userTo.getPassword());
        return user;
    }

    public static User updateFromUser(User user, User updated) {
        user.setName(updated.getName());
        user.setEmail(updated.getEmail().toLowerCase());
        user.setPhone(updated.getPhone());
        if (updated.getPassword() != null) user.setPassword(updated.getPassword());
        user.setRoles(updated.getRoles());
        return user;
    }

    public static User prepareToSave(User user, PasswordEncoder passwordEncoder) {
        String password = user.getPassword();
        user.setPassword(passwordEncoder.encode(password));
        user.setEmail(user.getEmail().toLowerCase());
        return user;
    }
}
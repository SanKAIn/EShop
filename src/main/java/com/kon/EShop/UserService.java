package com.kon.EShop;

import com.kon.EShop.model.User;
import com.kon.EShop.repository.UserRepository;
import com.kon.EShop.to.UserTo;
import com.kon.EShop.util.UserUtil;
import com.kon.EShop.util.exception.NotFoundException;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import java.util.List;

import static com.kon.EShop.util.UserUtil.prepareToSave;
import static com.kon.EShop.util.UserUtil.updateFromTo;
import static com.kon.EShop.util.ValidationUtil.*;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Log4j2
public class UserService implements UserDetailsService {

    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository repository, PasswordEncoder passwordEncoder) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public void delete(Long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
    }

    public User get(Long id) throws NotFoundException {
        return checkNotFoundWithId(repository.findById(id).get(), id);
    }

    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.findAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    public User update(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo, long id) throws NotFoundException {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        User user = get(userTo.getId());
        prepareAndSave(updateFromTo(user, userTo));   // !! need only for JDBC implementation
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) throws NotFoundException {
        User user = checkNotFoundWithId(get(id), id);
        user.setEnabled(enabled);
        repository.save(user);  // !! need only for JDBC implementation
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }

}

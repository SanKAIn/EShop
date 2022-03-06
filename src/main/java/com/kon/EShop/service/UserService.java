package com.kon.EShop.service;

import com.kon.EShop.AuthorizedUser;
import com.kon.EShop.model.cartPack.Cart;
import com.kon.EShop.model.cartPack.CartProduct;
import com.kon.EShop.model.userPack.User;
import com.kon.EShop.repository.CartRepository;
import com.kon.EShop.repository.impl.UserImpl;
import com.kon.EShop.to.UserTo;
import com.kon.EShop.util.EntityUtil;
import com.kon.EShop.util.exception.NotFoundException;
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

import static com.kon.EShop.util.EntityUtil.setToSession;
import static com.kon.EShop.util.UserUtil.*;
import static com.kon.EShop.util.ValidationUtil.*;

@Service
@Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
@Log4j2
public class UserService implements UserDetailsService {

    private final UserImpl repository;
    private final PasswordEncoder passwordEncoder;
    private final CartRepository cartRepository;

    public UserService(UserImpl repository, PasswordEncoder passwordEncoder, CartRepository cartRepository) {
        this.repository = repository;
        this.passwordEncoder = passwordEncoder;
        this.cartRepository = cartRepository;
    }

    @CacheEvict(value = "users", allEntries = true)
    public User create(User user) {
        Assert.notNull(user, "user must not be null");
        return prepareAndSave(user);
    }

    @CacheEvict(value = "users", allEntries = true)
    public Integer delete(Long id) throws NotFoundException {
        checkNotFoundWithId(repository.delete(id), id);
        return 1;
    }

    public User get(Long id) throws NotFoundException {
        return checkNotFoundWithId(repository.get(id), id);
    }

    public User getByEmail(String email) throws NotFoundException {
        Assert.notNull(email, "email must not be null");
        return checkNotFound(repository.getByEmail(email), "email=" + email);
    }

    @Cacheable("users")
    public List<User> getAll() {
        return repository.getAll();
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(UserTo userTo, long id) throws NotFoundException {
        log.info("update {} with id={}", userTo, id);
        assureIdConsistent(userTo, id);
        User user = get(userTo.getId());
        repository.save(updateFromTo(user, userTo));
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void update(User user, long id) throws NotFoundException {
        log.info("update {} with id={}", user, id);
        assureIdConsistent(user, id);
        User userC = get(user.getId());
        updateFromUser(userC, user);
        if (user.getPassword() != null)
            prepareAndSave(userC);
        else
            repository.save(userC);
    }

    @CacheEvict(value = "users", allEntries = true)
    @Transactional
    public void enable(long id, boolean enabled) throws NotFoundException {
        User user = checkNotFoundWithId(get(id), id);
        user.setEnabled(enabled);
        repository.save(user);
    }

    @Override
    public AuthorizedUser loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = repository.getByEmail(email.toLowerCase());
        if (user == null) {
            throw new UsernameNotFoundException("User " + email + " is not found");
        }
        setToSession("userId", user.getId());
        return new AuthorizedUser(user);
    }

    private User prepareAndSave(User user) {
        return repository.save(prepareToSave(user, passwordEncoder));
    }

    private void prepareCart(Long id) {
//        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
//        HttpSession session = attr.getRequest().getSession(true);
        Cart dbCart = cartRepository.findByUserId(id);
//        Cart curCart = EntityUtil.getCartFromSession(session);
        Cart curCart = EntityUtil.getCartFromSession();
        if (dbCart != null) {
            List<CartProduct> newList = dbCart.getCartProducts();
            newList.removeAll(curCart.getCartProducts());
            newList.addAll(curCart.getCartProducts());
            curCart.setCartProducts(newList);
            curCart.setUser_id(dbCart.getUser_id());
            curCart.setOrdered(false);
            curCart.setId(dbCart.getId());
        }
        setToSession("cart", curCart);
    }
}






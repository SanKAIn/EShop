package com.kon.EShop.repository.impl;

import com.kon.EShop.model.userPack.UserActive;
import com.kon.EShop.model.userPack.UserActiveName;
import com.kon.EShop.repository.userPack.UserActiveNameRepository;
import com.kon.EShop.repository.userPack.UserActiveRepository;
import com.kon.EShop.to.PageTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.getPageable;

@Repository
public class UserActiveImpl {

    private final UserActiveRepository rep;
    private final UserActiveNameRepository repository;

    public UserActiveImpl(UserActiveRepository rep, UserActiveNameRepository repository) {
        this.rep = rep;
        this.repository = repository;
    }

    public void save(UserActive userActive) {
        rep.save(userActive);
    }

    public UserActive get(Long id) {
        return rep.findById(id).orElse(null);
    }

    public UserActive getByProduct(Long id) {
        return rep.getByProductId(id, LocalDate.now());
    }

    public PageTo<UserActiveName> page(LocalDate start, LocalDate end, Integer page, Integer limit){
        List<UserActiveName> all = repository.findAll(start, end, limit, (page - 1) * limit);
        Integer count = repository.count(start, end);
        return new PageTo<>(all, getPageable(page, limit, Sort.by("productId")), count);
    }

    public PageTo<UserActiveName> pageId(LocalDate start, LocalDate end, Long id){
        List<UserActiveName> id1 = repository.findId(start, end, id);
        return new PageTo<>(id1, getPageable(1, 1, Sort.by("productId")), 1);
    }
}







package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Category;
import com.kon.EShop.repository.CategoryRepository;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.List;

import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class CategoryImpl {
    private final CategoryRepository repository;

    public CategoryImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    public List<Category> getAll() {
        return sort(repository.findAll(Sort.by("name")), null, 1);
    }

    public Category get(Long id) throws NotFoundException {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public Category create(Category category) {
        Assert.notNull(category, "category must not be null");
        checkNew(category);
        return repository.save(category);
    }

    public Category update(Category category) throws NotFoundException {
        Assert.notNull(category, "category must not be null");
        return checkNotFoundWithId(repository.save(category), category.getId());
    }

    public int delete(Long id) {
        return repository.delete(id);
    }

    public List<Category> getTree(Integer id) {
        return repository.getTree(id);
    }

    public List<Category> getDirectChild(Integer id) {
        return repository.getDirectChild(id);
    }

    private List<Category> sort(List<Category> list, Long parent, int level) {
        List<Category> list2 = new ArrayList<>();
        if (list.size() > 0)
            for (int i = 0; i < list.size(); i++) {
                if (list.get(i).getParent().equals(parent)) {
                    Category current = list.get(i);
                    List<Category> count = sort(list, current.getId(), level + 1);
                    current.setChildren(count.size());
                    current.setLevel(level);
                    list2.add(current);
                    list2.addAll(count);
                }
            }
        return list2;
    }
}

package com.kon.EShop.repository.impl;

import com.kon.EShop.model.filtersPack.Category;
import com.kon.EShop.repository.filtersPack.CategoryRepository;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;
import org.springframework.util.Assert;

import java.io.IOException;
import java.util.*;

import static com.kon.EShop.util.EntityUtil.checkNameUA;
import static com.kon.EShop.util.ValidationUtil.checkNew;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class CategoryImpl {

    private final CategoryRepository repository;
    private final FileManager fileManager;

    public CategoryImpl(CategoryRepository repository, FileManager fileManager) {
        this.repository = repository;
        this.fileManager = fileManager;
    }

    public List<Category> getAll() {
        List<Category> name = sort(repository.findAll(Sort.by("name")), null, 1);
        return name;
    }

    public Category get(Long id) throws NotFoundException {
        return checkNotFoundWithId(repository.findById(id).orElse(null), id);
    }

    public Category create(Category category) {
        Assert.notNull(category, "category must not be null");
        checkNew(category);
        if (category.getLabel() == null) category.setLabel("favicon.ico");
        checkNameUA(category);
        return repository.save(category);
    }

    public Category update(Category category) throws NotFoundException {
        Assert.notNull(category, "category must not be null");
        return checkNotFoundWithId(repository.save(category), category.getId());
    }

    public int delete(Long id) throws IOException {
        Category category = repository.findById(id).orElse(null);
        fileManager.delLabel(category);
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
        if (list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (Objects.equals(list.get(i).getParent(), parent)) {
                    Category current = list.get(i);
                    List<Category> count = sort(list, current.getId(), level + 1);
                    current.setChildren(count.size());
                    current.setLevel(level);
                    list2.add(current);
                    list2.addAll(count);
                }
            }
        }
        if (list2.size() > 0) {
            if (list2.get(0).getName().endsWith("Ач"))
                list2.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getName().substring(0, o.getName().length() - 2))));
            if (isDouble(list2.get(0).getName()))
                list2.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getName())));
            if (list2.get(0).getName().startsWith("М") && list2.get(0).getName().length() <= 3)
                list2.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName().substring(1))));
            if (list2.get(0).getName().endsWith("л") && isDouble(list2.get(0).getName().substring(list2.get(0).getName().length() - 2,list2.get(0).getName().length() - 1)))
                list2.sort(Comparator.comparingDouble(o -> Double.parseDouble(o.getName().substring(0, o.getName().length() - 1).replace(",", "."))));
            if (list2.get(0).getName().contains("w") && list2.get(0).getName().length() <= 5)
                list2.sort(Comparator.comparingInt(o -> Integer.parseInt(o.getName().substring(0, o.getName().indexOf("w")))));
        }
        return list2;
    }

    private boolean isDouble(String s) {
        try {
            Double.parseDouble(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

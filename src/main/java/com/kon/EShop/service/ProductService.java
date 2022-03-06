package com.kon.EShop.service;

import com.kon.EShop.model.Rating;
import com.kon.EShop.model.productPack.*;
import com.kon.EShop.model.userPack.UserActive;
import com.kon.EShop.repository.PhotoRepository;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.repository.impl.UserActiveImpl;
import com.kon.EShop.to.FiltersTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.EntityUtil;
import com.kon.EShop.util.FileManager;
import lombok.extern.java.Log;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

import static com.kon.EShop.util.EntityUtil.*;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Log
@Service
public class ProductService {

    private final ProductImpl productIml;
    private final PhotoRepository photoRepository;
    private final FileManager fileManager;
    private final UserActiveImpl implUA;

    public ProductService(ProductImpl productIml, PhotoRepository photoRepository, FileManager fileManager, UserActiveImpl implUA) {
        this.productIml = productIml;
        this.photoRepository = photoRepository;
        this.fileManager = fileManager;
        this.implUA = implUA;
    }

    public Set<String> getBigPhoto(Long id) {
        Date date = new Date();
        Set<String> photoTo = getPhotoTo(photoRepository.getByProductId(id));
        log.info("----> Get big photo took " + (new Date().getTime() - date.getTime()) + "ms");
        return photoTo;
    }

    public Page<Analog> allAnalogsByName(String name) {
        Date date = new Date();
        Page<Analog> analogs = productIml.getToAdminAnalogs(name);
        log.info("----> Get analogs took " + (new Date().getTime() - date.getTime()) + "ms");
        return analogs;
    }

    public Page<Analog> allAnalogById(Long id) {
        return productIml.getAnalogById(id);
    }

    public Product getOne(Long id) {
        return productIml.findById(id);
    }

    public FiltersTo countFilters(String[] brands, String[] applic, String[] category, String[] manufacture, HttpSession session) {
        Long mainId = (Long) session.getAttribute("mainCategory");
        Date date = new Date();
        FiltersTo count = productIml.getCount(brands, applic, category, manufacture, mainId);
        log.info("----> Count filters took " + (new Date().getTime() - date.getTime()) + "ms");
        return count;
    }

    public Page<Catalog> filter(Pageable pageable, String[] brands, String[] applic, String[] category, String[] manufacture, HttpSession session) {
        Date date = new Date();
        Long mainC = (Long) session.getAttribute("mainCategory");
        Page<Catalog> products = productIml.filterProducts(pageable, brands, category, applic, manufacture, mainC);
        log.info("----> Filter for user took " + (new Date().getTime() - date.getTime()) + "ms");
        return products;
    }

    public Page<Catalog> find(Pageable pageable, String findBy, String key, HttpSession session) {
        Date date = new Date();
        Long mainC = (Long) session.getAttribute("mainCategory");
        Page<Catalog> product = productIml.findProduct(findBy, key, pageable, mainC);
        log.info("----> Find for user took " + (new Date().getTime() - date.getTime()) + "ms");
        return product;
    }

    public String vote(Long id, Integer vote, HttpSession session) {
        Set<Long> idVotes = (Set<Long>) session.getAttribute("voted");
        if (idVotes == null)
            idVotes = new HashSet<>();
        if (!idVotes.contains(id)) {

            Product product = productIml.findById(id);
            if (product.getRating() == null) product.addRating(new Rating(0, 0));
            int bal = product.getRating().getVotes() * product.getRating().getRating() + vote * 100;
            product.getRating().setVotes(product.getRating().getVotes() + 1);
            product.getRating().setRating(bal / product.getRating().getVotes());
            productIml.save(product);

            idVotes.add(id);
            session.setAttribute("voted", idVotes);
            return "Ok";
        }
        return "Что то не так!";
    }

    @Caching(evict = {
            @CacheEvict(value = "productC", allEntries = true),
            @CacheEvict(value = "countC", allEntries = true)
    })
    public void save(ProductTo product) {
        Product fromTo = productFromTo(product);
        Set<ProductPhoto> photos = product.getPhotos();
        if (photos != null) photos.forEach(f -> f.setProduct(fromTo));
        checkNameUA(fromTo);
        productIml.save(fromTo);
    }

    @Caching(evict = {
            @CacheEvict(value = "productC", allEntries = true),
            @CacheEvict(value = "countC", allEntries = true)
    })
    public void save(Product product) {
        Set<ProductPhoto> photos = product.getPhotos();
        if (photos != null) photos.forEach(f -> f.setProduct(product));
        checkNameUA(product);
        productIml.save(product);
    }

    @Caching(evict = {
            @CacheEvict(value = "productC", allEntries = true),
            @CacheEvict(value = "countC", allEntries = true)
    })
    public void delete(Long id) {
        Product product = productIml.getOneAdmin(id);
        if (product != null) {
            product.getPhotos().forEach(f -> {
                if (!f.getUrl().equals("temp.jpg")){
                    try {
                        fileManager.delete(f.getUrl(), "big");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }});
            productIml.delete(product);
        }
    }

    public Page<AdminCatalog> getAllToAdmin(Pageable pageable, String[] brand, String[] applic, String[] category, Long mainCategory, Long manufacture) {
        Date date = new Date();
        Page<AdminCatalog> forAdmin = productIml.findForAdmin(pageable, brand, applic, category, mainCategory, manufacture);
        log.info("----> Filter products to admin took " + (new Date().getTime() - date.getTime()) + "ms");
        return forAdmin;
    }

    public Page<AdminCatalog> findToAdmin(String[] brand, String[] applic, String[] category, Long manufacture, Long mainCategory, String key, Pageable pageable) {
        Date date = new Date();
        Page<AdminCatalog> products = productIml.searchAdmin(brand, applic, category, manufacture, mainCategory, key, pageable);
        log.info("----> Find products to admin took " + (new Date().getTime() - date.getTime()) + "ms");
        return products;
    }

    public void postCSV(MultipartFile file) {
        try {
            productIml.updateByCSV(file);
        } catch (IOException e) {
            System.out.println("не получилось CSV");
            e.printStackTrace();
        }
    }

    @Caching(evict = {
            @CacheEvict(value = "productC", allEntries = true),
            @CacheEvict(value = "countC", allEntries = true)
    })
    public void enable(Long id, boolean enabled) {
        Date date = new Date();
        Product product = checkNotFoundWithId(findById(id), id);
        product.setPopular(enabled);
        productIml.save(product);
        log.info("----> Product controller enable took " + (new Date().getTime() - date.getTime()) + "ms");
    }

    public ProductTo getOneToAdmin(Long id) {
        Date date = new Date();
        ProductTo productTo = EntityUtil.productToFromProduct(productIml.getOneAdmin(id));
        log.info("----> Get product to manager took " + (new Date().getTime() - date.getTime()) + "ms");
        return productTo;
    }

    public Page<Catalog> allC(Pageable pageable, Long mainCatId) {
        return productIml.allC(pageable, mainCatId);
    }

    public Product byId(Long id) {
        Product withId = productIml.findById(id);
        List<Long> ids = (List<Long>) getFromSession("lookingProducts", List.class);
        if (ids == null) ids = new ArrayList<>();
        if (!ids.contains(id)) {
            UserActive u = implUA.getByProduct(id);
            if (u == null) {
                LocalDateTime now = LocalDateTime.now();
                u = new UserActive(null, id, now, 1L);
            } else {
                u.incrementCount();
            }
            implUA.save(u);
            ids.add(id);
            setToSession("lookingProducts", ids);
        }
        return withId;
    }

    public Product findById(Long id) {
        return productIml.findById(id);
    }

    public List<Product> listProductsForCart(List<Long> ids) {
        return productIml.listProductsForCart(ids);
    }

    public Page<Catalog> findProduct(String findBy, String key, Pageable pageable, Long mainCategory) {
        int page = pageable.getPageNumber() * pageable.getPageSize();
        int pageSize = pageable.getPageNumber();
        return productIml.findProduct(findBy, key, pageable, mainCategory);
    }

    public Page<Catalog> filterProducts(Pageable pageable, String[] brands, String[] categories, String[] appl, String[] manufacturer, Long mainCatId) {
        return productIml.filterProducts(pageable, brands, categories, appl, manufacturer, mainCatId);
    }

    @Cacheable(value = "countC", condition = "#brands == null && #applic == null && #categories == null && #manufacture == null")
    public FiltersTo getCount(String[] brands, String[] applic, String[] categories, String[] manufacture, Long mainId) {
        return productIml.getCount(brands, applic, categories, manufacture, mainId);
    }
}

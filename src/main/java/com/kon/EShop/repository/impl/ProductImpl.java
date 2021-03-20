package com.kon.EShop.repository.impl;

import com.kon.EShop.model.MyFile;
import com.kon.EShop.model.MyUploadForm;
import com.kon.EShop.model.Product;
import com.kon.EShop.model.Rating;
import com.kon.EShop.repository.ProductRepository;
import com.kon.EShop.to.BigTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.FileManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

import static com.kon.EShop.util.EntityUtil.*;

@Repository
public class ProductImpl {

    private final EntityManager entityManager;
    private final ProductRepository repository;
    private final FileManager fileManager;

    public ProductImpl(EntityManager entityManager, ProductRepository repository, FileManager fileManager) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.fileManager = fileManager;
    }

    public BigTo allA(HttpServletRequest request) {
        return getBigTo(repository.findAllA(getPageable(request)));
    }

    public BigTo getInvisible(HttpServletRequest request) {
        Page<Product> list = null;
        String findBy = request.getParameter("findBy");
        String key = request.getParameter("key");
        if (findBy.equals("vendor"))
            list = repository.getProductsByVendorContainsAndVisibilityFalse(key, getPageable(request));
        if (findBy.equals("name"))
            list = repository.getProductsByNameContainsAndVisibilityFalse(key, getPageable(request));
        if (findBy.equals("all"))
            list = repository.getProductsByNameContainsAndVisibilityFalseOrVendorContainsAndVisibilityFalse(key, key, getPageable(request));
        return getBigTo(list);
    }

    public BigTo getAllInvisible(HttpServletRequest request) {
        return getBigTo(repository.getProductsByVisibilityIsFalse(getPageable(request)));
    }

    public Product save(Product product){
        product.getPhotos().forEach(f->f.setProduct(product));
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void updateByCSV(MyUploadForm form) throws IOException {
        for (MultipartFile f : form.getFileDataS()) {
            if (f.getOriginalFilename().length() > 0) {
                try {
                    MyFile file = fileManager.getMyFile(f, "table.csv", "csv");
                    repository.queryWith(file.getColumnNames(), file.getTableName());
                }finally {
                    fileManager.delete("table.csv", "csv");
                }
            }
        }
    }



    public List<ProductTo> findFive(){
        List<Long> collect = new Random().longs(12, 1, 15).distinct().boxed().collect(Collectors.toList());
        List<Product> products = entityManager.createQuery("SELECT p " +
                "FROM Product p " +
                "left join fetch p.rating " +
                "left join fetch p.photos " +
                "left join fetch p.brand " +
                "WHERE p.id " +
                "IN :list", Product.class)
                .setParameter("list", collect)
                .getResultList();
        return productInProductTo(products);
    }

    public BigTo allU() {
        return getBigTo(repository.findAllU(PageRequest.of(0, 12)));
    }

    public Product findById(long id){
        return repository.findById(id).get();
    }

    public List<Product> listProductsForCart(List<Long> ids){
        return repository.findList(ids);
    }

    public BigTo findProduct(HttpServletRequest request) {
        Page<Product> list = null;
        String findBy = request.getParameter("findBy");
        String key = request.getParameter("key");
            if (findBy.equals("vendor"))
                list = repository.getProductsByVendorContainsAndVisibilityTrue(key, getPageable(request));
            if (findBy.equals("contains"))
                list = repository.findAllByName(key, getPageable(request));
            if (findBy.equals("all"))
                list = repository.getProductsByVendorContainsAndVisibilityTrueOrNameContainsAndVisibilityTrue(key, key, getPageable(request));
        return getBigTo(list);
    }

    public BigTo filterProducts(HttpServletRequest request) {
        Page<Product> list;
        String[] brands1 = request.getParameterValues("brands");
        String[] categories1 = request.getParameterValues("category");
        List<Long> brands = new ArrayList<>();
        if (brands1 != null)
            brands = toLong(brands1);
        List<Long> categories = new ArrayList<>();
        if (categories1 != null)
            categories = toLong(categories1);
        if (categories.size() > 0 && brands.size() == 0)
            list = repository.findByCategory(categories, getPageable(request));
        else if (categories.size() == 0 && brands.size() > 0)
            list = repository.findByBrands(brands, getPageable(request));
        else if (categories.size() > 0 && brands.size() > 0)
            list = repository.findAllByBrandAndCategory(brands, categories, getPageable(request));
        else
            list = repository.findAll(getPageable(request));
        return getBigTo(list);
    }

    public Integer getCount(String[] brands, String[] categories) {
        Integer otv;
        if (categories == null && brands != null) otv = repository.countByBrandIdInAndVisibilityTrue(toLong(brands));
        else if (categories != null && brands == null) otv = repository.countAllByCategoryIn(toLong(categories));
        else if (categories != null && brands != null) otv = repository.countAllByCategoryIdInAndBrandIdInAndVisibilityTrue(toLong(categories), toLong(brands));
        else otv = repository.countAllByVisibilityTrue();
        return otv;
    }

    public void vote(Long id, Integer vote) {
        Product product = repository.findById(id).get();
        if (product.getRating() == null) product.addRating(new Rating(0, 0));
        int bal = product.getRating().getVotes() * product.getRating().getRating() + vote * 100;
        product.getRating().setVotes(product.getRating().getVotes() + 1);
        product.getRating().setRating(bal / product.getRating().getVotes());
        repository.save(product);
    }

    public List<ProductTo> getPassingGoods(Long id) {
        return productInProductTo(repository.getRelated(id));
    }

    public List<Long> toLong(String[] mas) {
        return Arrays.stream(mas)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

}











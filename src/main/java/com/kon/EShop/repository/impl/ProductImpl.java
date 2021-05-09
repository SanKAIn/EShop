package com.kon.EShop.repository.impl;

import com.kon.EShop.model.*;
import com.kon.EShop.repository.FiltersRepository;
import com.kon.EShop.repository.ProductRepository;
import com.kon.EShop.to.FiltersTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.FileManager;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

import static com.kon.EShop.util.EntityUtil.*;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ProductImpl {

    private final EntityManager entityManager;
    private final ProductRepository repository;
    private final FileManager fileManager;
    private final FiltersRepository filters;

    public ProductImpl(EntityManager entityManager, ProductRepository repository, FileManager fileManager, FiltersRepository filters) {
        this.entityManager = entityManager;
        this.repository = repository;
        this.fileManager = fileManager;
        this.filters = filters;
    }

    public Page<Product> findForAdmin(Pageable pageable, Long brandId, Long categoryId, Long mainCategoryId, Long manufacturerId) {
        return repository.findToAdmin(brandId, categoryId, manufacturerId, mainCategoryId, pageable);
    }

    public Page<Product> searchAdmin(Long brandId, Long categoryId, Long manufacturerId, Long mainCatId, String text, Pageable pageable) {
//        Page<Product> list = null;
//        if (findBy.equals("vendor"))
//            list = repository.getProductsByVendorContains(key, pageable);
//        if (findBy.equals("name"))
//            list = repository.getProductsByNameContains(key, pageable);
//        if (findBy.equals("all"))
//            list = repository.getProductsByNameContainsOrVendorContains(key, key, pageable);
        return repository.searchAll(brandId, categoryId, manufacturerId, mainCatId, text, pageable);
    }

    public Page<Product> getAllInvisible(Pageable pageable) {
        return repository.getProductsByPopularIsFalse(pageable);
    }

    public Product save(Product product) {
        product.getPhotos().forEach(f -> f.setProduct(product));
        return repository.save(product);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public void updateByCSV(MyUploadForm form) throws IOException {
        for (MultipartFile f : form.getFileDataS()) {
            String name = f.getOriginalFilename();
            if (name != null && name.length() > 0) {
                try {
                    MyFile file = fileManager.getMyFile(f, "table.csv", "csv");
                    repository.queryWith(file.getColumnNames(), file.getTableName());
                } finally {
                    fileManager.delete("table.csv", "csv");
                }
            }
        }
    }


    public List<ProductTo> findFive() {
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

    public Page<Product> allU(Pageable pageable, Long mainCatId) {
        return repository.findAllU(pageable, mainCatId);
    }

    public Product findById(long id) {
        return repository.findById(id).orElse(null);
    }

    public List<Product> listProductsForCart(List<Long> ids) {
        return repository.findList(ids);
    }

    public Page<Product> findProduct(String findBy, String key, Pageable pageable) {
        Page<Product> list = null;
        if (findBy.equals("vendor"))
            list = repository.getProductsByVendorContainsAndPopularTrue(key, pageable);
        if (findBy.equals("contains"))
            list = repository.findAllByName(key, pageable);
        if (findBy.equals("all"))
            list = repository.getProductsByVendorContainsAndPopularTrueOrNameContainsAndPopularTrue(key, key, pageable);
        return list;
    }

    public Page<Product> filterProducts(Pageable pageable, String[] brands, String[] categories, String[] manufacturer, Long mainCatId) {
        return repository.findToUser(toLong(brands), toLong(categories), toLong(manufacturer), mainCatId, pageable);
    }

    public FiltersTo getCount(String[] brands, String[] categories, String[] manufacture, Long mainId) {
        FiltersTo otv = new FiltersTo(filters.countByAl(toLong(categories), toLong(brands), toLong(manufacture), mainId));
        /*if (categories == null && brands != null && manufacture != null)
            otv = new FiltersTo(filters.countNotCategory(toLong(brands), toLong(manufacture), mainId));
        else if (categories != null && brands == null && manufacture != null)
            otv = new FiltersTo(filters.countNotBrand(toLong(categories), toLong(manufacture), mainId));
        else if (categories != null && brands != null && manufacture == null)
            otv = new FiltersTo(filters.countNotManufacture(toLong(categories), toLong(brands), mainId));
        else if (categories == null && brands == null && manufacture != null)
            otv = new FiltersTo(filters.countByManufacture(toLong(manufacture), mainId));
        else if (categories == null && brands != null && manufacture == null)
            otv = new FiltersTo(filters.countByBrand(toLong(brands), mainId));
        else if (categories != null && brands == null && manufacture == null)
            otv = new FiltersTo(filters.countByCategory(toLong(categories), mainId));
        else if (categories != null && brands != null && manufacture != null)
            otv = new FiltersTo(filters.countByAll(toLong(categories), toLong(brands), toLong(manufacture), mainId));
        else
            otv = new FiltersTo(filters.countByNone(mainId));*/
        return otv;
    }

    public void vote(Long id, Integer vote) {
        Product product = repository.findById(id).orElse(null);
        assert product != null;
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
        if (mas == null) return null;
        return Arrays.stream(mas)
                .map(Long::parseLong)
                .collect(Collectors.toList());
    }

    public void enable(long id, boolean enabled) throws NotFoundException {
        Product product = checkNotFoundWithId(findById(id), id);
        product.setPopular(enabled);
        repository.save(product);
    }

    public Product getOneAdmin(Long id) {
        return repository.getOneAdmin(id);
    }

}











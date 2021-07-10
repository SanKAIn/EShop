package com.kon.EShop.repository.impl;

import com.kon.EShop.model.MyFile;
import com.kon.EShop.model.Product;
import com.kon.EShop.model.Rating;
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

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.kon.EShop.util.EntityUtil.productInProductTo;
import static com.kon.EShop.util.ValidationUtil.checkNotFoundWithId;

@Repository
public class ProductImpl {

    private final ProductRepository repository;
    private final FileManager fileManager;
    private final FiltersRepository filters;

    public ProductImpl(ProductRepository repository, FileManager fileManager, FiltersRepository filters) {
        this.repository = repository;
        this.fileManager = fileManager;
        this.filters = filters;
    }

    public Page<Product> findForAdmin(Pageable pageable, String[] brands, String[] categories, Long mainCategoryId, Long manufacturerId) {
        return repository.findToAdmin(toLong(brands), toLong(categories), manufacturerId, mainCategoryId, pageable);
    }

    public Page<Product> searchAdmin(String[] brands, String[] categories, Long manufacturerId, Long mainCatId, String text, Pageable pageable) {
        return repository.searchAll(toLong(brands), toLong(categories), manufacturerId, mainCatId, text, pageable);
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

    public void updateByCSV(MultipartFile csv) throws IOException {
        String name = csv.getOriginalFilename();
        if (name != null && name.length() > 0) {
            try {
                MyFile file = fileManager.getMyFile(csv, "table.csv", "csv");
                repository.queryWith(file.getColumnNames(), file.getTableName());
            } finally {
                fileManager.delete("table.csv", "csv");
            }
        }
    }

    public Page<Product> allU(Pageable pageable, Long mainCatId) {
        return repository.findAllU(pageable, mainCatId);
    }

    public Product findById(long id) {
        return repository.findWithId(id);
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
            list = repository.findProductK(key, pageable);
        return list;
    }

    public Page<Product> filterProducts(Pageable pageable, String[] brands, String[] categories, String[] manufacturer, Long mainCatId) {
        return repository.findToUser(toLong(brands), toLong(categories), toLong(manufacturer), mainCatId, pageable);
    }

    public FiltersTo getCount(String[] brands, String[] categories, String[] manufacture, Long mainId) {
        return new FiltersTo(filters.countByAl(toLong(categories), toLong(brands), toLong(manufacture), mainId));
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

    public void enable(long id, boolean enabled) throws NotFoundException {
        Product product = checkNotFoundWithId(findById(id), id);
        product.setPopular(enabled);
        repository.save(product);
    }

    public Product getOneAdmin(Long id) {
        return repository.getOneAdmin(id);
    }

    public List<Long> toLong(String[] mas) {
        if (mas == null) return null;
        return Arrays.stream(mas)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

}











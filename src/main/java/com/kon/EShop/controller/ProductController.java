package com.kon.EShop.controller;

import com.kon.EShop.model.productPack.AdminCatalog;
import com.kon.EShop.model.productPack.Analog;
import com.kon.EShop.model.productPack.Catalog;
import com.kon.EShop.model.productPack.Product;
import com.kon.EShop.service.ProductService;
import com.kon.EShop.to.FiltersTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.Set;

@RestController
@RequestMapping("/goods")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @GetMapping("/photos/{id}")
    public Set<String> getBigPhoto(@PathVariable Long id) {
        return service.getBigPhoto(id);
    }

    @GetMapping("/analogs")
    public Page<Analog> allAnalogsByName(@RequestParam(defaultValue = "") String key) {
        return service.allAnalogsByName(key);
    }

    @GetMapping("/analogs/{id}")
    public Page<Analog> getAnalogById(@PathVariable Long id) {
        return service.allAnalogById(id);
    }

    @GetMapping("/count")
    public FiltersTo countFilter(@RequestParam(required = false) String[] brands,
                                 @RequestParam(required = false) String[] applic,
                                 @RequestParam(required = false) String[] category,
                                 @RequestParam(required = false) String[] manufacture,
                                 HttpSession session) {
        return service.countFilters(brands, applic, category, manufacture, session);
    }

    @GetMapping("/filter")
    public Page<Catalog> filter(Pageable pageable,
                                @RequestParam(required = false) String[] brands,
                                @RequestParam(required = false) String[] applic,
                                @RequestParam(required = false) String[] category,
                                @RequestParam(required = false) String[] manufacture,
                                HttpSession session) {
        return service.filter(pageable, brands, applic, category, manufacture, session);
    }

    @GetMapping("/find")
    public Page<Catalog> find(Pageable pageable,
                              @RequestParam(defaultValue = "all") String findBy,
                              @RequestParam(defaultValue = "") String key,
                              HttpSession session) {
        return service.find(pageable, findBy, key, session);
    }

    @GetMapping("/user/{id}")
    public Product get(@PathVariable Long id) {
        return service.getOne(id);
    }

    @GetMapping("/profile/vote/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public String vote(@PathVariable Long id, @RequestParam Integer vote, HttpSession session) {
        return service.vote(id, vote, session);
    }

    @PostMapping("/admin")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void newProduct(@RequestBody @Valid ProductTo product) {
        service.save(product);
    }

    @DeleteMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteProduct(@PathVariable Long id) {
        service.delete(id);
    }

    @GetMapping("/admin/all")
    public Page<AdminCatalog> geToAdmin(Pageable pageable,
                                   @RequestParam(required = false) String[] category,
                                   @RequestParam(required = false) String[] brand,
                                   @RequestParam(required = false) String[] applic,
                                   @RequestParam(required = false) Long manufacture,
                                   @RequestParam(required = false) Long mainCategory) {
        return service.getAllToAdmin(pageable, brand, applic, category, mainCategory, manufacture);
    }

    @GetMapping("/admin/find")
    public Page<AdminCatalog> findA(@PageableDefault(page = 0, size = 5) Pageable pageable,
                                    @RequestParam(defaultValue = "") String key,
                                    @RequestParam(required = false) String[] category,
                                    @RequestParam(required = false) String[] brand,
                                    @RequestParam(required = false) String[] applic,
                                    @RequestParam(required = false) Long manufacture,
                                    @RequestParam(required = false) Long mainCategory) {
        return service.findToAdmin(brand, applic, category, manufacture, mainCategory, key, pageable);
    }

    @PostMapping("/admin/loadCSV")
    public void postCSV(@RequestParam MultipartFile file) {
        service.postCSV(file);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable Long id, @RequestParam boolean enabled) throws NotFoundException {
        service.enable(id, enabled);
    }

    @GetMapping("/manager/{id}")
    public ProductTo getManager(@PathVariable Long id) {
        return service.getOneToAdmin(id);
    }

    @GetMapping("/admin/{id}")
    public ProductTo getToAdmin(@PathVariable Long id) {
        return service.getOneToAdmin(id);
    }
}
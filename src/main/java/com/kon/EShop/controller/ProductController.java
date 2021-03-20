package com.kon.EShop.controller;

import com.kon.EShop.model.MyUploadForm;
import com.kon.EShop.model.Product;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.BigTo;
import com.kon.EShop.to.ProductTo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.List;

import static com.kon.EShop.util.EntityUtil.productInProductTo;

@RestController
@RequestMapping("/goods")
public class ProductController {

    private final ProductImpl productIml;

    public ProductController(ProductImpl productIml) {
        this.productIml = productIml;
    }

    @GetMapping("/five")
    public List<ProductTo> five() {
        return productIml.findFive();
    }

    @GetMapping("/get/{id}")
    public ProductTo getProduct(@PathVariable Long id) {
        return productInProductTo(productIml.findById(id));
    }

    @GetMapping("/filter")
    public BigTo filter(HttpServletRequest request) {
        return productIml.filterProducts(request);
    }

    @GetMapping("/count")
    public Integer countFilter(HttpServletRequest request) {
        String[] brands = request.getParameterValues("brands");
        String[] categories = request.getParameterValues("category");
        return productIml.getCount(brands, categories);
    }

    @GetMapping("/find")
    public BigTo find(HttpServletRequest request) {
        return productIml.findProduct(request);
    }

    @PostMapping("/admin")
    public Product newProduct(@RequestBody Product product) {
        return productIml.save(product);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productIml.delete(id);
    }

    @GetMapping("/profile/vote/{id}")
    public void vote(@PathVariable Long id, @RequestParam Integer vote) {
        productIml.vote(id, vote);
    }

    @GetMapping("/admin/all")
    public BigTo geToAdmin(HttpServletRequest request) {
        return productIml.allA(request);
    }

    @GetMapping("/admin/find")
    public BigTo findA(HttpServletRequest request) {
        return productIml.getInvisible(request);
    }

    @GetMapping("/admin/invisible")
    public BigTo invisible(HttpServletRequest request) {
        return productIml.getAllInvisible(request);
    }

    @PostMapping("/admin/loadProductsCSV")
    public void postCSV(@ModelAttribute("myUploadForm") MyUploadForm myUploadForm) {
        try {
            productIml.updateByCSV(myUploadForm);
        } catch (IOException e) {
            System.out.println("не получилось удалить");
            e.printStackTrace();
        }
    }

    @GetMapping("/passing/{id}")
    public List<ProductTo> getPassing(@PathVariable Long id) {
        List<ProductTo> passingGoods = productIml.getPassingGoods(id);
        return passingGoods;
    }
}

package com.kon.EShop.controller;

import com.kon.EShop.model.Product;
import com.kon.EShop.repository.impl.BrandImpl;
import com.kon.EShop.repository.impl.CategoryImpl;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.BigTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.EntityUtil;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

import static com.kon.EShop.util.EntityUtil.productInProductTo;

@Controller
public class MainController {

    private final ProductImpl productImpl;
    private final BrandImpl brandImpl;
    private final CategoryImpl catImpl;

    public MainController(ProductImpl productImpl, BrandImpl brandImpl, CategoryImpl catImpl) {
        this.productImpl = productImpl;
        this.brandImpl = brandImpl;
        this.catImpl = catImpl;
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void fav() {
        System.out.println("favwaw");
    }

    @GetMapping("/")
    public String getRoot(Model model){
        model.addAttribute("brands", brandImpl.getAll());
        model.addAttribute("categories", catImpl.getAll());
        model.addAttribute("text", "<span class=\"badge badge-secondary user-select-none\"");
        model.addAttribute("text2", "</span>");
        model.addAttribute("cards", productImpl.findFive());
        return "index";
    }

    @GetMapping("/one/{id}")
    public String getOne(@PathVariable Long id, Model model){
        Product one = productImpl.findById(id);
        if (one != null){
            ProductTo to = productInProductTo(one);
            model.addAttribute("id", id);
            model.addAttribute("product", to);
        }
        return "single";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers() {
        return "users";
    }

}

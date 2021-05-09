package com.kon.EShop.controller;

import com.kon.EShop.model.Order;
import com.kon.EShop.model.Product;
import com.kon.EShop.repository.impl.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kon.EShop.util.EntityUtil.getPageable;
import static com.kon.EShop.util.EntityUtil.productInProductTo;

@Controller
public class MainController {

    private final ProductImpl productImpl;
    private final BrandImpl brandImpl;
    private final CategoryImpl catImpl;
    private final MainCategoryImpl mainCategoryImpl;
    private final ManufactureImpl manufacture;

    public MainController(ProductImpl productImpl, BrandImpl brandImpl, CategoryImpl catImpl, MainCategoryImpl mainCategoryImpl, ManufactureImpl manufacture) {
        this.productImpl = productImpl;
        this.brandImpl = brandImpl;
        this.catImpl = catImpl;
        this.mainCategoryImpl = mainCategoryImpl;
        this.manufacture = manufacture;
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void fav() {
        System.out.println("favwaw");
    }

    @GetMapping("/")
    public String getRoot(Model model) {
        model.addAttribute("cards", mainCategoryImpl.getAll());
        return "main";
    }

    @GetMapping("/catalog")
    public String getCatalog(Model model,
                             HttpSession session,
                             @RequestParam(required = false) Long id,
                             @RequestParam(required = false) String name) {
        if (id != null) session.setAttribute("mainCategory", id);
        Page<Product> products = productImpl.allU(getPageable(1, 12, Sort.by("name")), (Long) session.getAttribute("mainCategory"));
        model.addAttribute("name", name);
        model.addAttribute("brands", brandImpl.getAll());
        model.addAttribute("categories", catImpl.getAll());
        model.addAttribute("manuf", manufacture.getAll());
        model.addAttribute("cards", products);
        model.addAttribute("startEl", products.getNumber() * products.getSize());
        model.addAttribute("size", products.getSize());
        model.addAttribute("totalEl", products.getTotalElements());
        int totalPages = products.getTotalPages();
        model.addAttribute("totalP", totalPages);
        if (totalPages > 0) {
            if (totalPages > 5) totalPages = 5;
            List<Integer> pageNumbers = IntStream.rangeClosed(1, totalPages)
                    .boxed()
                    .collect(Collectors.toList());
            model.addAttribute("pageNumbers", pageNumbers);
        }
        return "catalog";
    }

    @GetMapping("/order")
    public String getOrder(Model model) {
        Order order = new Order();
        model.addAttribute("order", order);
        return "order";
    }

    @GetMapping("/one/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        Product one = productImpl.findById(id);
        if (one != null) {
            model.addAttribute("product", productInProductTo(one));
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

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminka")
    public String getAdminku() {
        return "adminka";
    }

}

package com.kon.EShop.controller;

import com.kon.EShop.model.cartPack.Orders;
import com.kon.EShop.model.productPack.Catalog;
import com.kon.EShop.model.productPack.Product;
import com.kon.EShop.model.userPack.Role;
import com.kon.EShop.repository.impl.*;
import com.kon.EShop.service.BrandService;
import com.kon.EShop.service.ProductService;
import com.kon.EShop.util.EntityUtil;
import lombok.extern.java.Log;
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
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.kon.EShop.util.EntityUtil.getPageable;
@Log
@Controller
public class MainController {

    private final ProductService productService;
    private final BrandService brandServ;
    private final CategoryImpl catImpl;
    private final MainCategoryImpl mainCategoryImpl;
    private final ManufactureImpl manufacture;
    private final OrderImpl orderS;
    private final ShopImpl shopImpl;
    private final ApplicabilityIml applicabilityIml;

    public MainController(ProductService productService, BrandService brandServ, CategoryImpl catImpl,
                          MainCategoryImpl mainCategoryImpl, ManufactureImpl manufacture, OrderImpl orderS, ShopImpl shopImpl, ApplicabilityIml applicabilityIml) {
        this.productService = productService;
        this.brandServ = brandServ;
        this.catImpl = catImpl;
        this.mainCategoryImpl = mainCategoryImpl;
        this.manufacture = manufacture;
        this.orderS = orderS;
        this.shopImpl = shopImpl;
        this.applicabilityIml = applicabilityIml;
    }

    @GetMapping("/favicon.ico")
    @ResponseBody
    public void fav() {
        System.out.println("favwaw");
    }

    @GetMapping("/")
    public String getRoot(Model model) {
        Date date = new Date();
        model.addAttribute("cards", mainCategoryImpl.getAll());
        log.info("---->  End loading index.html " + (new Date().getTime() - date.getTime()) + "ms");
        return "index";
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    @GetMapping("/orders")
    public String getOrders() {
        return "orders";
    }

    @GetMapping("/orderSecc")
    public String order(HttpSession session, Model model) {
        Orders order = orderS.get((Long) session.getAttribute("orderId"));
        model.addAttribute("order", order);
        return "successOrder";
    }

    @GetMapping("/catalog")
    public String getCatalog(Model model,
                             HttpSession session,
                             @RequestParam(required = false) Long id,
                             @RequestParam(required = false) String name) {
        if (id != null) session.setAttribute("mainCategory", id);
        Date date = new Date();
        Page<Catalog> products = productService.allC(getPageable(1, 12, Sort.by("p2.name")), (Long) session.getAttribute("mainCategory"));

        model.addAttribute("name", name);
        model.addAttribute("brands", brandServ.getAll(id));
        model.addAttribute("categories", catImpl.getAll());
        model.addAttribute("manuf", manufacture.getAll(id));
        model.addAttribute("primen", applicabilityIml.getAll(id));
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
        log.info("---->  End loading catalog.html " + (new Date().getTime() - date.getTime()) + "ms");
        return "catalog";
    }

    @GetMapping("/order")
    public String getOrder(Model model) {
        model.addAttribute("order", new Orders());
        model.addAttribute("shop", shopImpl.getAll());
        return "order";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/analytic")
    public String getAnalytic() {
        return "analytical";
    }

    @GetMapping("/one/{id}")
    public String getOne(@PathVariable Long id, Model model) {
        Date date = new Date();
        Product one = productService.byId(id);
        if (one != null) model.addAttribute("product", EntityUtil.productToFromProduct(one));
        log.info("---->  End loading single.html " + (new Date().getTime() - date.getTime()) + "ms");
        return "single";
    }

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/users")
    public String getUsers(Model model) {
        model.addAttribute("roles", Role.values());
        return "users";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/adminka")
    public String getAdminku() {
        return "adminka";
    }

}

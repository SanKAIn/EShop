package com.kon.EShop.controller;

import com.kon.EShop.model.CartProduct;
import com.kon.EShop.model.Product;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.to.FiltersTo;
import com.kon.EShop.to.ProductTo;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.kon.EShop.util.EntityUtil.productFromTo;
import static com.kon.EShop.util.EntityUtil.productInProductTo;

@RestController
@RequestMapping("/goods")
public class ProductController {

    private final ProductImpl productIml;

    public ProductController(ProductImpl productIml) {
        this.productIml = productIml;
    }

    @GetMapping("/allU")
    public Page<Product> allU(Pageable page, HttpSession session) {
        Long mainC = (Long) session.getAttribute("mainCategory");
        return productIml.allU(page, mainC);
    }

    @GetMapping("/user/{id}")
    public Product get(@PathVariable Long id) {
        return productIml.findById(id);
    }

    @GetMapping("/count")
    public FiltersTo countFilter(@RequestParam(required = false) String[] brands,
                                 @RequestParam(required = false) String[] category,
                                 @RequestParam(required = false) String[] manufacture,
                                 HttpSession session) {
        Long mainC = (Long) session.getAttribute("mainCategory");
        return productIml.getCount(brands, category, manufacture, mainC);
    }

    @GetMapping("/filter")
    public Page<Product> filter(Pageable pageable,
                                @RequestParam(required = false) String[] brands,
                                @RequestParam(required = false) String[] category,
                                @RequestParam(required = false) String[] manufacture,
                                HttpSession session) {
        Long mainC = (Long) session.getAttribute("mainCategory");
        return productIml.filterProducts(pageable, brands, category, manufacture, mainC);
    }

    @GetMapping("/find")
    public Page<Product> find(Pageable pageable,
                              @RequestParam(defaultValue = "all") String findBy,
                              @RequestParam(defaultValue = "") String key) {
        return productIml.findProduct(findBy, key, pageable);
    }

    @GetMapping("/profile/vote/{id}")
    public String vote(@PathVariable Long id, @RequestParam Integer vote, HttpSession session) {
        Set<Long> idVotes = (Set<Long>) session.getAttribute("voted");
        if (idVotes == null)
            idVotes = new HashSet<>();
        if (!idVotes.contains(id)) {
            productIml.vote(id, vote);
            idVotes.add(id);
            session.setAttribute("voted", idVotes);
            return "Ok";
        }
        return "Что то не так!";
    }

    @PostMapping("/admin")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void newProduct(@RequestBody @Valid ProductTo product) {
        productIml.save(productFromTo(product));
    }

    @DeleteMapping("/admin/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productIml.delete(id);
    }

    @GetMapping("/admin/all")
    public Page<Product> geToAdmin(Pageable pageable,
                                   @RequestParam(required = false) String[] category,
                                   @RequestParam(required = false) String[] brand,
                                   @RequestParam(required = false) Long manufacture,
                                   @RequestParam(required = false) Long mainCategory) {
        return productIml.findForAdmin(pageable, brand, category, mainCategory, manufacture);
    }

    @GetMapping("/admin/find")
    public Page<Product> findA(Pageable pageable,
                               @RequestParam(defaultValue = "") String key,
                               @RequestParam(required = false) String[] category,
                               @RequestParam(required = false) String[] brand,
                               @RequestParam(required = false) Long manufacture,
                               @RequestParam(required = false) Long mainCategory) {
        return productIml.searchAdmin(brand, category, manufacture, mainCategory, key, pageable);
    }

    @GetMapping("/admin/invisible")
    public Page<Product> invisible(Pageable pageable) {
        return productIml.getAllInvisible(pageable);
    }

    @PostMapping("/admin/loadCSV")
    public void postCSV(@RequestParam MultipartFile file) {
        try {
            productIml.updateByCSV(file);
        } catch (IOException e) {
            System.out.println("не получилось CSV");
            e.printStackTrace();
        }
    }

    @GetMapping("/passing/{id}")
    public List<ProductTo> getPassing(@PathVariable Long id) {
        return productIml.getPassingGoods(id);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
        productIml.enable(id, enabled);
    }

    @GetMapping("/manager/{id}")
    public ProductTo getAdmin(@PathVariable Long id) {
        return productInProductTo(productIml.getOneAdmin(id));
    }

    @GetMapping("/admin/{id}")
    public ProductTo getToAdmin(@PathVariable Long id) {
        return productInProductTo(productIml.getOneAdmin(id));
    }

    @PutMapping("/manager")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void sellProduct(@RequestBody List<CartProduct> list) {
        for (CartProduct cp : list) {
            Product product = productIml.findById(cp.getProductId());
            Integer amount = product.getAmount();
            product.setAmount(amount - cp.getAmount());
            productIml.save(product);
        }
    }
}








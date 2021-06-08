package com.kon.EShop.controller;

import com.kon.EShop.model.Shop;
import com.kon.EShop.repository.impl.ShopImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shops")
public class ShopController {

    private final ShopImpl shopImpl;

    public ShopController(ShopImpl shopImpl) {
        this.shopImpl = shopImpl;
    }

    @GetMapping
    public List<Shop> getAll() {
        return shopImpl.getAll();
    }

    @GetMapping("/admin/{id}")
    public Shop getShop(@PathVariable Long id) {
        return shopImpl.getShop(id);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) {
        shopImpl.delete(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void save(@RequestBody Shop shop) {
        if (shop.isNew())
            shopImpl.save(shop);
        else
            shopImpl.update(shop);
    }

}

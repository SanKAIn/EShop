package com.kon.EShop.controller;

import com.kon.EShop.model.Product;
import com.kon.EShop.model.Rating;
import com.kon.EShop.repository.impl.ProductImpl;
import com.kon.EShop.repository.impl.RatingImpl;
import com.kon.EShop.to.ProductTo;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/rating")
public class RatingController {

    private final ProductImpl productImpl;

    public RatingController(ProductImpl product) {
        this.productImpl = product;
    }

    @PutMapping("/{id}")
    public Rating voteP(@PathVariable Long id, @RequestParam Integer vote) {
        Product p = productImpl.findById(id);
        if (p.getRating() == null)
            p.setRating(new Rating(0,0, p));
        int bal = p.getRating().getVotes() * p.getRating().getRating() + vote * 100;
        p.getRating().setVotes(p.getRating().getVotes() + 1);
        p.getRating().setRating(bal / p.getRating().getVotes());
        productImpl.save(p);
        return p.getRating();
    }
}

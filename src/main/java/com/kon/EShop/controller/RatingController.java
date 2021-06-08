package com.kon.EShop.controller;

import com.kon.EShop.model.Product;
import com.kon.EShop.model.Rating;
import com.kon.EShop.repository.impl.ProductImpl;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/profile/rating")
public class RatingController {

    private final ProductImpl productImpl;

    public RatingController(ProductImpl product) {
        this.productImpl = product;
    }

    @PutMapping("/{id}")
    public Rating voteP(@PathVariable Long id, @RequestParam Integer vote, HttpSession session) {
        List<Long> list = (List<Long>) session.getAttribute("votes");
        Product p = productImpl.findById(id);
        if (list == null || !list.contains(id)) {
            if (list == null) list = new ArrayList<>();
            if (p.getRating() == null) p.setRating(new Rating(0, 0, p));
            int bal = p.getRating().getVotes() * p.getRating().getRating() + vote * 100;
            p.getRating().setVotes(p.getRating().getVotes() + 1);
            p.getRating().setRating(bal / p.getRating().getVotes());
            p.getRating().setMessage("Голос защитан");
            productImpl.save(p);
            list.add(id);
            session.setAttribute("votes", list);
            return p.getRating();
        }
        p.getRating().setMessage("Голосовать можно только 1 раз за каждый товар");
        return p.getRating();
    }
}

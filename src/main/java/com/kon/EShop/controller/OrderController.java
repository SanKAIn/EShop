package com.kon.EShop.controller;

import com.kon.EShop.model.cartPack.Orders;
import com.kon.EShop.model.cartPack.State;
import com.kon.EShop.service.OrderService;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/order")
public class OrderController {

    private final OrderService service;

    public OrderController(OrderService service) {
        this.service = service;
    }

    @GetMapping("/manager")
    public List<Orders> getAll(@RequestParam(defaultValue = "NEW") State state) {
        return service.getAll(state);
    }

    @GetMapping("/manager/{id}")
    public Orders getOrder(@PathVariable Long id) {
        return service.getOrder(id);
    }

    @DeleteMapping("/manager/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) throws NotFoundException {
        service.delete(id);
    }

    @PostMapping
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void saveOrder(@RequestBody @Valid Orders order, HttpSession session) {
        Long cartId = (Long) session.getAttribute("cartId");
        service.save(order, cartId);
    }

    @PostMapping("/manager")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void adminUpdate(@RequestBody Orders order) {
        service.aUpdate(order);
    }

}

package com.kon.EShop.controller;

import com.kon.EShop.model.userPack.User;
import com.kon.EShop.service.UserService;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class AdminController {

   private final UserService service;

   public AdminController(UserService service) {
      this.service = service;
   }

   @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
   public List<User> getAll() {
      return service.getAll();
   }

   @GetMapping(value = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
   public User get(@PathVariable long id) throws NotFoundException {
      return service.get(id);
   }

   @DeleteMapping("/{id}")
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public Integer delete(@PathVariable long id) throws NotFoundException {
      return service.delete(id);
   }

   @PostMapping
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void createOrUpdate(@Valid @RequestBody User user) throws NotFoundException {
      if (user.isNew()) {
         service.create(user);
      }else {
         service.update(user, user.id());
      }
   }

   @PostMapping("/{id}")
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
      service.enable(id, enabled);
   }
}

package com.kon.EShop.controller;

import com.kon.EShop.service.UserService;
import com.kon.EShop.model.User;
import com.kon.EShop.to.UserTo;
import com.kon.EShop.util.UserUtil;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.SpringValidatorAdapter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.ValidationException;
import java.util.List;

import static com.kon.EShop.util.ValidationUtil.assureIdConsistent;

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
   public void delete(@PathVariable long id) throws NotFoundException {
      service.delete(id);
   }

   @PostMapping
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void createOrUpdate(@Valid @RequestBody User user) throws NotFoundException {
      if (user.isNew()) {
         if (user.getPassword() == null) throw new ValidationException("пароль должен содержать от 5 до 100");
         service.create(user);
      }
      else {
         service.update(user, user.id());
      }
   }

   @PostMapping("/{id}")
   @ResponseStatus(value = HttpStatus.NO_CONTENT)
   public void enable(@PathVariable int id, @RequestParam boolean enabled) throws NotFoundException {
      service.enable(id, enabled);
   }
}

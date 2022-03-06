package com.kon.EShop.controller;

import com.kon.EShop.model.filtersPack.Applicability;
import com.kon.EShop.repository.impl.ApplicabilityIml;
import com.kon.EShop.util.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/appl")
public class ApplicabilityController {

    private final ApplicabilityIml rep;

    public ApplicabilityController(ApplicabilityIml rep) {
        this.rep = rep;
    }

    @GetMapping
    public List<Applicability> getAll() {
        return rep.getAll();
    }

    @GetMapping("/admin/{id}")
    public Applicability get(@PathVariable Long id) throws NotFoundException {
        Applicability applicability = rep.getApplicability(id);
        if (applicability == null)
            throw new NotFoundException("id=" + id);
        return applicability;
    }

    @PostMapping("/admin")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid Applicability applicability) throws NotFoundException {
        if (applicability.isNew())
            rep.save(applicability);
        else
            rep.update(applicability);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<Integer> delete(@PathVariable Long id) throws NotFoundException, IOException {
        Integer deleted = rep.delete(id);
        if (deleted == null)
            throw new NotFoundException("delete applicability not found");
        return new ResponseEntity<>(deleted, HttpStatus.OK);
    }

    @PostMapping("/admin/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public Integer enable(@PathVariable Long id, @RequestParam boolean enabled) throws NotFoundException {
        return rep.enable(id, enabled);
    }
}

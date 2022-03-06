package com.kon.EShop.controller;

import com.kon.EShop.model.filtersPack.Unit;
import com.kon.EShop.repository.impl.UnitImpl;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/unit")
public class UnitController {

    private final UnitImpl unitImpl;

    public UnitController(UnitImpl unitImpl) {
        this.unitImpl = unitImpl;
    }

    @GetMapping
    public List<Unit> getAll() {
        return unitImpl.getAll();
    }

    @GetMapping("/admin/{id}")
    public Unit get(@PathVariable Long id) {
        return unitImpl.getUnit(id);
    }

    @PostMapping("/admin")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void save(@RequestBody @Valid Unit unit) {
        if (unit.isNew())
            unitImpl.save(unit);
        else
            unitImpl.update(unit);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) {
        unitImpl.delete(id);
    }
}

package com.kon.EShop.to;

import com.kon.EShop.model.filtersPack.FiltersCount;
import lombok.Data;

import java.util.*;

@Data
public class FiltersTo {
    private Map<Long, Integer> brands = new HashMap<>();
    private Map<Long, Integer> categories = new HashMap<>();
    private Map<Long, Integer> manufactures = new HashMap<>();
    private Map<Long, Integer> applicability = new HashMap<>();
    private Integer summa;

    public FiltersTo(List<FiltersCount> filters) {
        Set<Long> su = new HashSet<>();
        filters.forEach(f -> su.add(f.getProduct_id()));
        this.summa = su.size();
        for (Long pr: su) {
            Set<Long> br = new HashSet<>();
            Set<Long> ca = new HashSet<>();
            Set<Long> ma = new HashSet<>();
            Set<Long> ap = new HashSet<>();
            for (FiltersCount fc: filters) {
                if (fc.getProduct_id().equals(pr)) {
                    br.add(fc.getBrand_id());
                    ca.add(fc.getCategory_id());
                    ma.add(fc.getManufacture_id());
                    ap.add(fc.getApplicability_id());
                }
            }
            br.forEach(f -> this.brands.merge(f, 1, (a, b) -> a+=b));
            ca.forEach(f -> this.categories.merge(f, 1, (a, b) -> a+=b));
            ma.forEach(f -> this.manufactures.merge(f, 1, (a, b) -> a+=b));
            ap.forEach(f -> this.applicability.merge(f, 1, (a, b) -> a+=b));
        }
    }
}

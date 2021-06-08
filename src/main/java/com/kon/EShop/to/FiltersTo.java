package com.kon.EShop.to;

import com.kon.EShop.model.FiltersCount;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Data
public class FiltersTo {
    private Map<Long, Long> brands = new HashMap<>();
    private Map<Long, Long> categories = new HashMap<>();
    private Map<Long, Long> manufactures = new HashMap<>();
    private Long summa = 0L;

    public FiltersTo(List<FiltersCount> filters) {
        for (FiltersCount fc: filters) {
            this.brands.merge(fc.getBrand_id(), fc.getCount(), (a, b) -> a+=b);
            this.categories.merge(fc.getCategory_id(), fc.getCount(), (a, b) -> a+=b);
            this.manufactures.merge(fc.getManufacture_id(), fc.getCount(), (a, b) -> a+=b);
            this.summa+=fc.getCount();
        }
    }

    public void setSumma(List<FiltersCount> f) {
        this.summa = f.stream().mapToLong(FiltersCount::getCount).sum();
    }

    public void setBrands(List<FiltersCount> f){
        this.brands = f.stream()
                .collect(Collectors.groupingBy(FiltersCount::getBrand_id, Collectors.summingLong(FiltersCount::getCount)));
    }

    public void setCategories(List<FiltersCount> f) {
        this.categories = f.stream()
                .collect(Collectors.groupingBy(FiltersCount::getCategory_id, Collectors.summingLong(FiltersCount::getCount)));
    }

    public void setManufactures(List<FiltersCount> f) {
        this.manufactures = f.stream()
                .collect(Collectors.groupingBy(FiltersCount::getManufacture_id, Collectors.summingLong(FiltersCount::getCount)));
    }
}

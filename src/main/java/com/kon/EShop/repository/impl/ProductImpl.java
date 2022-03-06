package com.kon.EShop.repository.impl;

import com.kon.EShop.model.MyFile;
import com.kon.EShop.model.filtersPack.FiltersCount;
import com.kon.EShop.model.filtersPack.Unit;
import com.kon.EShop.model.productPack.*;
import com.kon.EShop.repository.ProductRepository;
import com.kon.EShop.repository.filtersPack.FiltersRepository;
import com.kon.EShop.repository.productPack.AdminCatalogRepository;
import com.kon.EShop.repository.productPack.AnalogRepository;
import com.kon.EShop.repository.productPack.CatalogRepository;
import com.kon.EShop.repository.productPack.IdRepository;
import com.kon.EShop.to.FiltersTo;
import com.kon.EShop.util.FileManager;
import org.jetbrains.annotations.NotNull;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.io.IOException;
import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

import static com.kon.EShop.util.EntityUtil.*;

@Repository
public class ProductImpl {

    private final ProductRepository repository;
    private final FileManager fileManager;
    private final FiltersRepository filters;
    private final CatalogRepository catalogRepository;
    private final AdminCatalogRepository repositoryAdm;
    private final AnalogRepository repositoryAn;
    private final IdRepository idRepository;
    @PersistenceContext
    private EntityManager manager;

    public ProductImpl(ProductRepository repository, FileManager fileManager, FiltersRepository filters, CatalogRepository catalogRepository, AdminCatalogRepository repositoryAdm, AnalogRepository repositoryAn, IdRepository idRepository) {
        this.repository = repository;
        this.fileManager = fileManager;
        this.filters = filters;
        this.catalogRepository = catalogRepository;
        this.repositoryAdm = repositoryAdm;
        this.repositoryAn = repositoryAn;
        this.idRepository = idRepository;
    }

    public Page<Analog> getToAdminAnalogs(String name) {
        return repositoryAn.findName(getPageable(1, 40, Sort.by("name")), name.toLowerCase());
    }

    public Page<Analog> getAnalogById(Long id) {
        return repositoryAn.get(getPageable(1, 5, Sort.by("id")), id);
    }

    public Page<AdminCatalog> findForAdmin(Pageable pageable, String[] brands, String[] applic, String[] categories, Long mainCategoryId, Long manufacturerId) {
        return repositoryAdm.findAllAdm(toLong(brands), toLong(applic), toLong(categories), manufacturerId, mainCategoryId, pageable);
    }

    public Page<AdminCatalog> searchAdmin(String[] brands, String[] applic, String[] categories, Long manufacturerId, Long mainCatId, String text, Pageable pageable) {
        if (isLong(text))
            return repositoryAdm.searchAllId(toLong(brands), toLong(applic), toLong(categories), manufacturerId, mainCatId, text.toLowerCase(), pageable, Long.parseLong(text));
        else
            return repositoryAdm.searchAll(toLong(brands), toLong(applic), toLong(categories), manufacturerId, mainCatId, text.toLowerCase(), pageable);
    }

    public Product save(Product product) {
        return repository.save(product);
    }

    public void delete(Product product) {
        repository.delete(product);
    }

    public void updateByCSV(MultipartFile csv) throws IOException {
        String name = csv.getOriginalFilename();
        if (name != null && name.length() > 0) {
            try {
                MyFile file = fileManager.getMyFile(csv, "table.csv", "csv");
                repository.updCSV(file.getColumnNames(), file.getTableName());
            } finally {
                fileManager.delete("table.csv", "csv");
            }
        }
    }

    @Cacheable("productC")
    public Page<Catalog> allC(Pageable pageable, Long mainCatId) {
        Page<OnlyId> ids = idRepository.get(pageable, mainCatId);
        List<Catalog> allU = catalogRepository.findAllU(ids.getContent().stream().map(OnlyId::getId).collect(Collectors.toList()));
        return PageableExecutionUtils.getPage(allU, pageable, ids::getTotalElements);
    }

    public Product findById(Long id) {
        return repository.findWithId(id);
    }

    public List<Product> listProductsForCart(List<Long> ids) {
        return repository.findList(ids);
    }

    public Page<Catalog> findProduct(String findBy, String key, Pageable pageable, Long mainCategory) {
        Page<Catalog> list;
        Integer count = 0;
        List<Catalog> l2 = new ArrayList<>();
        int page = pageable.getPageNumber() * pageable.getPageSize();
        if (findBy.equals("vendor")) {
            l2 = catalogRepository.byVendor(addP(key), page, pageable.getPageSize(), mainCategory);
            count = catalogRepository.countVendor(addP(key), mainCategory);
        }
        if (findBy.equals("contains")){
            l2 = catalogRepository.byName(addP(key), page, pageable.getPageSize(), mainCategory);
            count = catalogRepository.countName(addP(key), mainCategory);
        }
        if (findBy.equals("all")){
            l2 = catalogRepository.byAll(addP(key), page, pageable.getPageSize(), mainCategory);
            count = catalogRepository.countAll(addP(key), mainCategory);
        }
        list = new PageImpl<>(l2, pageable, count);
        return list;
    }

    public Page<Catalog> filterProducts(Pageable pageable, String[] brands, String[] categories, String[] appl, String[] manufacturer, Long mainCatId) {
        Integer count = count(toLong(brands), toLong(categories), toLong(manufacturer), toLong(appl), mainCatId);
        List<Catalog> list = getCat(toLong(brands), toLong(categories), toLong(manufacturer), toLong(appl), mainCatId, pageable.getPageSize(), pageable.getPageNumber());
        return new PageImpl<>(list, pageable, count);
    }

    public FiltersTo getCount(String[] brands, String[] applic, String[] categories, String[] manufacture, Long mainId) {
        List<FiltersCount> filtersCounts = filters.countByAl(toLong(categories), toLong(brands), toLong(applic), toLong(manufacture), mainId);
        return new FiltersTo(filtersCounts);
    }

    public Product getOneAdmin(Long id) {
        return repository.getOneAdmin(id);
    }

    private List<Long> toLong(String[] mas) {
        if (mas == null) return null;
        return Arrays.stream(mas)
            .map(Long::parseLong)
            .collect(Collectors.toList());
    }

    private List<Catalog> getCat(List<Long> brandId, List<Long> categoryId, List<Long> manufacturerId, List<Long> applId, Long mainCatId, Integer limit, Integer offset) {
        List<Object[]> n = manager.createNativeQuery(
                "SELECT p.id, p.name, p.name_ua, p.vendor, p.amount, p.price, p.popular, " +
                         "u.id uid, u.unit, pp.id pid, pp.product_id, pp.url, pp.alt, p.manufacture_id, p.unit_id FROM products p " +
                "LEFT OUTER JOIN units u on u.id = p.unit_id " +
                "LEFT OUTER JOIN product_photos pp on p.id = pp.product_id " +
                "WHERE ((p.id IN (SELECT pm.product_id FROM prod_main pm WHERE pm.main_id = :mainCatId)) AND (:mainCatId IS NOT NULL) OR :mainCatId IS NULL) AND " +
                "((p.id IN (SELECT pf.product_id FROM prod_filters pf WHERE pf.filter_id IN (:categoryId))) AND (:categoryId IS NOT NULL) OR :categoryId IS NULL) AND " +
                "((p.id IN (SELECT pb.product_id FROM prod_brands pb WHERE pb.brand_id IN (:brandId))) AND (:brandId IS NOT NULL) OR :brandId IS NULL) AND " +
                "((p.id IN (SELECT pa.product_id FROM prod_appl pa WHERE pa.appl_id IN (:applId))) AND (:applId IS NOT NULL) OR :applId IS NULL) AND " +
                "((p.manufacture_id IN (:manufacturerId)) AND (:manufacturerId IS NOT NULL) OR :manufacturerId IS NULL) AND p.popular = true AND pp.url not like '%\\_%' " +
                "ORDER BY p.name")
                .setParameter("mainCatId", 0L)
                .setParameter("mainCatId", mainCatId)
                .setParameter("brandId", new ArrayList<>())
                .setParameter("brandId", brandId)
                .setParameter("categoryId", new ArrayList<>())
                .setParameter("categoryId", categoryId)
                .setParameter("applId", new ArrayList<>())
                .setParameter("applId", applId)
                .setParameter("manufacturerId", new ArrayList<>())
                .setParameter("manufacturerId", manufacturerId)
                .setMaxResults(limit)
                .setFirstResult(limit * offset)
                .getResultList();
        Map<Long, Catalog> otv = new HashMap<>();
        for (Object[] cat : n) {
            long id = ((Integer)cat[0]).longValue();
            String name = (String) cat[1];
            String name_ua = (String) cat[2];
            String vendor = (String) cat[3];
            Integer amount = (Integer) cat[4];
            Integer price = (Integer) cat[5];
            Boolean popular = (Boolean) cat[6];
            long unitId = ((Integer)cat[7]).longValue();
            String unit = (String) cat[8];
            long ph_id = ((Integer)cat[9]).longValue();
            String url = (String) cat[11];
            String alt = (String) cat[12];
            Catalog c = new Catalog(id, name, name_ua, vendor, amount, price, popular, Set.of(new ProductPhoto(ph_id, url, alt, null)),
                                    new Unit(unitId, unit, null), null);
            otv.put(id, c);
        }

        return new ArrayList<>(otv.values());
    }

    public Integer count(List<Long> brandId, List<Long> categoryId, List<Long> manufacturerId, List<Long> applId, Long mainCatId) {
        ArrayList<Long> list = new ArrayList<>();
        Query query = manager.createNativeQuery("SELECT count(p.id) FROM products p WHERE " +
                        "((p.id IN (SELECT pm.product_id FROM prod_main pm WHERE pm.main_id = :mainCatId)) AND (:mainCatId IS NOT NULL) OR :mainCatId IS NULL) AND " +
                        "((p.id IN (SELECT pf.product_id FROM prod_filters pf WHERE pf.filter_id IN (:categoryId))) AND (:categoryId IS NOT NULL) OR :categoryId IS NULL) AND " +
                        "((p.id IN (SELECT pb.product_id FROM prod_brands pb WHERE pb.brand_id IN (:brandId))) AND (:brandId IS NOT NULL) OR :brandId IS NULL) AND " +
                        "((p.id IN (SELECT pa.product_id FROM prod_appl pa WHERE pa.appl_id IN (:applId))) AND (:applId IS NOT NULL) OR :applId IS NULL) AND " +
                        "((p.manufacture_id IN (:manufacturerId)) AND (:manufacturerId IS NOT NULL) OR :manufacturerId IS NULL) AND p.popular = true")
                .setParameter("mainCatId", 0L)
                .setParameter("mainCatId", mainCatId)
                .setParameter("brandId", list)
                .setParameter("brandId", brandId)
                .setParameter("categoryId", list)
                .setParameter("categoryId", categoryId)
                .setParameter("applId", list)
                .setParameter("applId", applId)
                .setParameter("manufacturerId", list)
                .setParameter("manufacturerId", manufacturerId);
        return ((BigInteger)query.getSingleResult()).intValue();
    }

    @NotNull
    private String addP(String key) {
        return "%" + key + "%";
    }
}
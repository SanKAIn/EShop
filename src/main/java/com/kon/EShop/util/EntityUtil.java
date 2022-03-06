package com.kon.EShop.util;

import com.kon.EShop.model.BaseEntity;
import com.kon.EShop.model.Comment;
import com.kon.EShop.model.cartPack.Cart;
import com.kon.EShop.model.productPack.PhotoOnlyURL;
import com.kon.EShop.model.productPack.Product;
import com.kon.EShop.repository.impl.CartImpl;
import com.kon.EShop.to.CommentTo;
import com.kon.EShop.to.ProductTo;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.kon.EShop.util.SecurityUtil.idIfAuthUser;

public class EntityUtil {
    public EntityUtil() {
    }

    public static void checkNameUA(BaseEntity<?> entity) {
        if (entity.getNameUa() == null) entity.setNameUa(entity.getName());
    }

    public static Set<String> getPhotoTo(Set<PhotoOnlyURL> list) {
        Set<String> urls = new HashSet<>();
        for (PhotoOnlyURL f: list) {
            urls.add(f.getUrl());
        }
        return urls;
    }

    public static Product productFromTo(ProductTo pT) {
        return new Product(
                pT.getId(),
                pT.getName(),
                pT.getNameUa(),
                pT.getVendor(),
                pT.getPopular(),
                pT.getDescription(),
                pT.getDescriptionUa(),
                pT.getSearch().toLowerCase(),
                pT.getAmount(),
                pT.getPrice(),
                pT.getPhotos(),
                pT.getBrand(),
                pT.getRating(),
                pT.getCategory(),
                pT.getMainCategory(),
                pT.getManufacture(),
                pT.getUnit(),
                pT.getAnalogs(),
                pT.getPassing(),
                pT.getApplicability());
    }

    public static List<ProductTo> productToFromProduct(List<Product> products) {
        return products
                .stream()
                .map(EntityUtil::productToFromProduct)
                .collect(Collectors.toList());
    }

    public static ProductTo productToFromProduct(Product product) {
        ProductTo to = new ProductTo();
        to.setId(product.getId());
        to.setName(product.getName());
        to.setNameUa(product.getNameUa());
        to.setVendor(product.getVendor());
        to.setPopular(product.getPopular());
        to.setDescription(product.getDescription());
        to.setDescriptionUa(product.getDescriptionUa());
        to.setSearch(product.getSearch());
        to.setAmount(product.getAmount());
        to.setPrice(product.getPrice());
        PersistenceUtil pu = Persistence.getPersistenceUtil();
        if (pu.isLoaded(product.getPhotos())) to.setPhotos(product.getPhotos());
        if (pu.isLoaded(product.getBrand())) to.setBrand(product.getBrand());
        if (pu.isLoaded(product.getRating())) to.setRating(product.getRating());
        if (pu.isLoaded(product.getCategory())) to.setCategory(product.getCategory());
        if (pu.isLoaded(product.getMainCategory())) to.setMainCategory(product.getMainCategory());
        if (pu.isLoaded(product.getManufacture())) to.setManufacture(product.getManufacture());
        if (pu.isLoaded(product.getUnit())) to.setUnit(product.getUnit());
        if (pu.isLoaded(product.getAnalog())) to.setAnalogs(product.getAnalog());
        if (pu.isLoaded(product.getPassing())) to.setPassing(product.getPassing());
        if (pu.isLoaded(product.getApplicability())) to.setApplicability(product.getApplicability());
        return to;
    }

    public static CommentTo getCommentTo(Comment comment) {
        CommentTo commentTo = new CommentTo();
        commentTo.setId(comment.getId());
        commentTo.setDescription(comment.getDescription());
        commentTo.setTime(comment.getTime());
        commentTo.setParent(comment.getParentId());
//        commentTo.setUserName(comment.getUser().getName());
        return commentTo;
    }

    public static List<CommentTo> getCommentTo(List<Comment> comments) {
        return comments
                .stream()
                .map(EntityUtil::getCommentTo)
                .collect(Collectors.toList());
    }

    public static void cartAmountToProductsTo(List<ProductTo> productTos, Cart cart) {
        for (ProductTo p : productTos) {
            p.setCartAmount(cart.getCartProducts()
                    .stream()
                    .filter(f->f.getProductId().equals(p.getId()))
                    .findAny()
                    .get()
                    .getAmount());
        }
    }

    public static Pageable getPageable(HttpServletRequest request){
        String page = request.getParameter("page");
        String amount = request.getParameter("limit");
        String[] sortM = request.getParameter("sort") != null ?
                request.getParameter("sort").split("_") :
                new String[]{"name", "asc"};
        Sort sort =  sortM[1].equals("asc") ? Sort.by(sortM[0]).ascending() : Sort.by(sortM[0]).descending();
        int p = page == null ? 0 : Integer.parseInt(page) - 1;
        int a = amount == null ? 12 : Integer.parseInt(amount);
        return PageRequest.of(p, a, sort);
    }

    public static Pageable getPageable(Integer page, Integer onPage, Sort sort) {
        int p = page != null ? page-1 : 0;
        int o = onPage != null ? onPage : 12;
        Sort sort1 = sort != null ? sort : Sort.by("name").ascending();
        return PageRequest.of(p, o, sort1);
    }

    public static Cart getCartFromSession() {
        Cart curCart = getFromSession("cart", Cart.class);
        if (curCart == null){
            curCart = new Cart();
            curCart.setUser_id(idIfAuthUser());
            setToSession("cart", curCart);
            return curCart;
        }
        return curCart;
    }

    public static Cart getCartFromSession(CartImpl repo) {
        Cart curCart = getFromSession("cart", Cart.class);
        if (curCart == null && idIfAuthUser() != null)
            curCart = repo.getByUser(idIfAuthUser());
        if (curCart == null){
            curCart = new Cart();
            curCart.setUser_id(idIfAuthUser());
        }
        setToSession("cart", curCart);
        return curCart;
    }

    public static <T> T getFromSession(String paramName, Class<T> clazz) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        return (T) session.getAttribute(paramName);
    }

    public static void setToSession(String name, Object object) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attr.getRequest().getSession(true);
        session.setAttribute(name, object);
    }

    public static boolean isLong(String text) {
        try {
            Long.parseLong(text);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
}

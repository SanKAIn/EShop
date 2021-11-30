package com.kon.EShop.util;

import com.kon.EShop.HasId;
import com.kon.EShop.NameUa;
import com.kon.EShop.model.Cart;
import com.kon.EShop.model.Comment;
import com.kon.EShop.model.Product;
import com.kon.EShop.repository.CartRepository;
import com.kon.EShop.repository.impl.CartImpl;
import com.kon.EShop.to.BigTo;
import com.kon.EShop.to.CommentTo;
import com.kon.EShop.to.ProductTo;
import org.hibernate.proxy.HibernateProxy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.persistence.Persistence;
import javax.persistence.PersistenceUtil;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static com.kon.EShop.util.SecurityUtil.idIfAuthUser;

public class EntityUtil {
    public EntityUtil() {
    }

    public static void checkNameUA(NameUa entity) {
        if (entity.getNameUa() == null) entity.setNameUa(entity.getName());
    }

    public static Product productFromTo(ProductTo pT) {
        Product p = new Product();
        p.setId(pT.getId());
        p.setName(pT.getName());
        p.setNameUa(pT.getNameUa());
        p.setVendor(pT.getVendor());
        p.setPopular(pT.getPopular());
        p.setDescription(pT.getDescription());
        p.setDescriptionUa(pT.getDescriptionUa());
        p.setSearch(pT.getSearch().toLowerCase());
        p.setAmount(pT.getAmount());
        p.setPrice(pT.getPrice());
        p.setPhotos(pT.getPhotos());
        p.setBrand(pT.getBrand());
        p.setRating(pT.getRating());
        p.setCategory(pT.getCategory());
        p.setMainCategory(pT.getMainCategory());
        p.setManufacture(pT.getManufacture());
        p.setUnit(pT.getUnit());
        return p;
    }

    public static List<ProductTo> productInProductTo(List<Product> products) {
        return products
                .stream()
                .map(EntityUtil::productInProductTo)
                .collect(Collectors.toList());
    }

    public static ProductTo productInProductTo(Product product) {
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

//    public static BigTo getBigTo(Page<Product> page) {
//        return new BigTo(page.getContent(), page.getTotalElements(), page.getTotalPages(), page.getNumber());
//    }

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
//        Cart curCart = (Cart) session.getAttribute("cart");
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

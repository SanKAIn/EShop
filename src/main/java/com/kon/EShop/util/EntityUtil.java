package com.kon.EShop.util;

import com.kon.EShop.model.Cart;
import com.kon.EShop.model.Comment;
import com.kon.EShop.model.Product;
import com.kon.EShop.to.BigTo;
import com.kon.EShop.to.CommentTo;
import com.kon.EShop.to.ProductTo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.stream.Collectors;

public class EntityUtil {
    public EntityUtil() {
    }

    public static List<ProductTo> productInProductTo(List<Product> products) {
        return products
                .stream()
                .map(EntityUtil::productInProductTo)
                .collect(Collectors.toList());
    }

    public static ProductTo productInProductTo(Product product) {
        ProductTo result = new ProductTo(
            product.getId(),
            product.getName(),
            product.getVendor(),
            product.getVisibility(),
            product.getDescription(),
            product.getAmount(),
            product.getPrice(),
            product.getPhotos(),
            product.getBrand(),
            product.getRating()
        );
        return result;
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
                    .filter(f->f.getProduct_id().equals(p.getId()))
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

    public static BigTo getBigTo(Page<Product> page) {
        return new BigTo(page.getContent(), page.getTotalElements(), page.getTotalPages(), page.getNumber());
    }
}

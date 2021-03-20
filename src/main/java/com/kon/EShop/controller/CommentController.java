package com.kon.EShop.controller;

import com.kon.EShop.model.Comment;
import com.kon.EShop.repository.impl.CommentImpl;
import com.kon.EShop.to.CommentTo;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.kon.EShop.util.EntityUtil.getCommentTo;

@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CommentImpl commentImpl;

    public CommentController(CommentImpl commentImpl) {
        this.commentImpl = commentImpl;
    }

    @GetMapping("/{parentId}")
    public List<CommentTo> getParent(@PathVariable Long parentId,
                                     @RequestParam Long productId,
                                     @RequestParam(defaultValue = "1") Integer page) {
        return getCommentTo(commentImpl.getAll(parentId, productId, page));
    }

    @GetMapping
    public List<CommentTo> getChild(@RequestParam Long productId,
                                    @RequestParam(defaultValue = "1") Integer page) {
        return getCommentTo(commentImpl.getAll(productId, page));
    }

    @PostMapping
    public CommentTo addComment(@RequestBody Comment comment) {
        return commentImpl.save(comment);
    }

    @DeleteMapping("/admin/{id}")
    public void delete(@PathVariable Long id) {
        commentImpl.delete(id);
    }
}

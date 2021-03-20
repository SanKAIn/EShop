package com.kon.EShop.repository.impl;

import com.kon.EShop.model.Comment;
import com.kon.EShop.repository.CommentRepository;
import com.kon.EShop.repository.UserRepository;
import com.kon.EShop.to.CommentTo;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;

import static com.kon.EShop.util.EntityUtil.getCommentTo;

@Repository
public class CommentImpl {

    private final CommentRepository repository;
    private final UserRepository userRepository;

    public CommentImpl(CommentRepository repository, UserRepository userRepository) {
        this.repository = repository;
        this.userRepository = userRepository;
    }

    public List<Comment> getAll(Long parentId, Long productId, Integer page) {
        return repository.findCommentsByParentIdAndProductId(parentId, productId, PageRequest.of(page - 1, 10));
    }

    public List<Comment> getAll(Long productId, Integer page) {
        return repository.findCommentsByParentIdAndProductId(null, productId, PageRequest.of(page - 1, 10));
    }

    public CommentTo save(Comment comment) {
//        comment.setUser(userRepository.getOne(1L));
        return getCommentTo(repository.save(comment));
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

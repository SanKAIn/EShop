package com.kon.EShop.repository;

import com.kon.EShop.model.Comment;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface CommentRepository extends PagingAndSortingRepository<Comment, Long> {

    @EntityGraph(attributePaths = {"user"})
    List<Comment> findCommentsByParentIdAndProductId(Long parentId, Long productId, Pageable pageable);
}

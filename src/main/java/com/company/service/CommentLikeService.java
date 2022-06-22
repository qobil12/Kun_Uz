package com.company.service;

import com.company.entity.*;
import com.company.enums.LikeStatus;
import com.company.exps.ItemNotFoundException;
import com.company.repository.ArticleLikeRepository;
import com.company.repository.ArticleRepository;
import com.company.repository.CommentLikeRepository;
import com.company.repository.CommentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CommentLikeService {
    @Autowired
    private CommentLikeRepository commentlikeRepository;
    @Autowired
    private CommentRepository commentRepository;

    public void commentLike(Integer commentId, Integer pId) {
        likeDislike(commentId, pId, LikeStatus.LIKE);
    }

    public void commentDisLike(Integer commentId, Integer pId) {
        likeDislike(commentId, pId, LikeStatus.DISLIKE);
    }

    private void likeDislike(Integer commentId, Integer pId, LikeStatus status) {
        Optional<CommentLikeEntity> optional = commentlikeRepository.findExists(commentId, pId);
        if (optional.isPresent()) {
            CommentLikeEntity like = optional.get();
            like.setStatus(status);
            commentlikeRepository.save(like);
            return;
        }
        boolean commentExists = commentRepository.existsById(commentId);
        if (!commentExists) {
            throw new ItemNotFoundException("Article NotFound");
        }

        CommentLikeEntity like = new CommentLikeEntity();
        like.setComment(new CommentEntity(commentId));
        like.setProfile(new ProfileEntity(pId));
        like.setStatus(status);
        commentlikeRepository.save(like);
    }

    public void removeLike(Integer commentId, Integer pId) {
       /* Optional<ArticleLikeEntity> optional = commentlikeRepository.findExists(articleId, pId);
        optional.ifPresent(articleLikeEntity -> {
            commentlikeRepository.delete(articleLikeEntity);
        });*/
        commentlikeRepository.delete(commentId, pId);
    }
}

package com.company.repository;

import com.company.entity.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface CommentLikeRepository extends CrudRepository<CommentLikeEntity, Integer> {


    Optional<CommentLikeEntity> findByCommentAndProfile (CommentEntity article, ProfileEntity profile);

    @Query("FROM CommentLikeEntity a where  a.comment.id=:commentId and a.profile.id =:profileId")
    Optional<CommentLikeEntity> findExists(Integer commentId, Integer profileId);

    @Transactional
    @Modifying
    @Query("DELETE FROM CommentLikeEntity a where  a.comment.id=:commentId and a.profile.id =:profileId")
    void delete(Integer commentId, Integer profileId);

}

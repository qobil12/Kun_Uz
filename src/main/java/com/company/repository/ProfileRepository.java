package com.company.repository;

import com.company.entity.ProfileEntity;
import com.company.enums.ProfileStatus;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface ProfileRepository extends PagingAndSortingRepository<ProfileEntity, Integer> {

    List<ProfileEntity> findAllByVisible(Boolean b);

    Optional<ProfileEntity> findByEmail(String email);

    @Modifying
    @Transactional
    @Query("Update ProfileEntity  p set p.status=?2 where p.phone=?1")
    Optional<ProfileEntity> updateStatusByPhone(String phone, ProfileStatus status);

    @Transactional
    @Modifying
    @Query("update ProfileEntity p set p.photo.id = :photo where p.id=:id")
    void changeProfileImage(@Param("photo") String photoId, @Param("id") Integer id);

    @Query("from ProfileEntity p where p.id=?1 and p.photo.id=?2")
    boolean existsByPhotoId(Integer profileId,String photoId);
}

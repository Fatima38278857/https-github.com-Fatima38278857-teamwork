package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    void deleteCommentByAdIdAndPk(Integer adId, Integer pk);

    @Query(value = "SELECT c FROM CommentEntity c WHERE c.ad.id = :adId AND c.pk = :pk")
    List<CommentEntity> findAllCommentByAdIdAndCommentId(Integer adId, Integer pk);

    @Query(value = "SELECT c FROM CommentEntity c WHERE c.ad.id = :adId")
    List<CommentEntity> findAllCommentByAdId(Integer adId);
}

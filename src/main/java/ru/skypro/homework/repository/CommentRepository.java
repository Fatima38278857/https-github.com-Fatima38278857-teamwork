package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.CommentEntity;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<CommentEntity, Integer> {
    void deleteCommentByAd_IdAndId(Integer adId, Integer id);

    @Query(value = "SELECT c FROM Comment c WHERE c.ad.id = :adId AND c.author = :idAuthor AND c.id = :id")
    List<CommentEntity> findAllCommentByAdIdAndAuthorIdAndIdComment(Integer adId, Integer idAuthor, Integer id);
}

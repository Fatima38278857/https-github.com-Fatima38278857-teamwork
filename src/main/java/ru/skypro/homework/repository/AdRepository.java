package ru.skypro.homework.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.skypro.homework.entity.Ad;
import ru.skypro.homework.entity.CommentEntity;

import java.util.Optional;

@Repository
public interface AdRepository extends JpaRepository<Ad, Integer> {
    @Override
    Optional<Ad> findById(Integer id);

    Optional<CommentEntity> findCommentById(Integer id);
}

package ru.nsu.ccfit.lopatkin.testTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends JpaRepository<Category, Long> {

    List<Category> findAllByNameContainingIgnoreCaseAndDeletedIsFalse(String name);

    List<Category> findAllByDeletedIsFalse();

    Optional<Category> findByIdAndDeletedIsFalse(Long id);

    Optional<Category> findByRequestIdAndDeletedIsFalse(String requestId);

    boolean existsCategoryByNameAndDeletedIsFalse(String name);

    boolean existsCategoryByRequestIdAndDeletedIsFalse(String name);

    boolean existsCategoryByNameAndIdNotLikeAndDeletedIsFalse(String name, Long id);

    boolean existsCategoryByRequestIdAndIdNotLikeAndDeletedIsFalse(String requestId, Long id);
}

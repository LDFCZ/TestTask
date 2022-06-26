package ru.nsu.ccfit.lopatkin.testTask.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.nsu.ccfit.lopatkin.testTask.models.Banner;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;

import java.util.List;
import java.util.Optional;

public interface BannerRepository extends JpaRepository<Banner, Long> {

    List<Banner> findAllByNameContainingIgnoreCaseAndDeletedIsFalse(String name);

    boolean existsBannerByNameAndDeletedIsFalse(String name);

    boolean existsBannerByNameAndIdNotLikeAndDeletedIsFalse (String name, Long id);

    Optional<Banner> findByIdAndDeletedIsFalse(Long id);

    List<Banner> findAllByDeletedIsFalse();

    List<Banner> findByCategoriesContainingAndDeletedIsFalseOrderByPriceDesc(Category category);
}

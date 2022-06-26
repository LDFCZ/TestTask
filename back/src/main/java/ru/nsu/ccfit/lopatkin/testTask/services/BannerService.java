package ru.nsu.ccfit.lopatkin.testTask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.lopatkin.testTask.models.Banner;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;
import ru.nsu.ccfit.lopatkin.testTask.repositories.BannerRepository;
import ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler.ErrorResponse;
import ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler.Violation;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
public class BannerService {

    private final BannerRepository bannerRepository;


    @Autowired
    public BannerService(BannerRepository bannerRepository) {
        this.bannerRepository = bannerRepository;
    }

    public ResponseEntity<List<Banner>> findNotDeletedBannerByName(String name) {
        try {
            List<Banner> banners = bannerRepository.findAllByNameContainingIgnoreCaseAndDeletedIsFalse(name);
            if (banners.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(banners, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Banner>> getAllNotDeletedBanners() {
        try {

            List<Banner> banners = bannerRepository.findAllByDeletedIsFalse();
            if (banners.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(banners, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Banner> getNotDeletedBannerById(Long id) {
        try {
            Optional<Banner> banner = bannerRepository.findByIdAndDeletedIsFalse(id);
            return banner.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createNewBanner(Banner banner) {
        try{
            if (bannerRepository.existsBannerByNameAndDeletedIsFalse(banner.getName())) {
                ErrorResponse errorResponse = new ErrorResponse();
                errorResponse.getViolations().add(new Violation("name", String.format("Banner [%s] already exist", banner.getName())));
                return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
            }

            Set<Category> categories = banner.getCategories();
            for (Category category: categories) {
                banner.addCategory(category);
                category.addBanner(banner);
            }

            return new ResponseEntity<>(bannerRepository.save(
                    new Banner(banner.getName(), banner.getPrice(), banner.getCategories(), banner.getText())),
                    HttpStatus.OK);

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> editBanner(Banner editedBanner, Long id) {
        try{
            Optional<Banner> optional = bannerRepository.findById(id);
            Set<Category> categories = editedBanner.getCategories();

            if (optional.isPresent()){
                if (bannerRepository.existsBannerByNameAndIdNotLikeAndDeletedIsFalse(editedBanner.getName(), editedBanner.getId())) {
                    ErrorResponse errorResponse = new ErrorResponse();
                    errorResponse.getViolations().add(new Violation("name", String.format("Banner [%s] already exist", editedBanner.getName())));
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }

                for (Category category : categories) {
                    editedBanner.addCategory(category);
                    category.addBanner(editedBanner);
                }
                Banner editBanner = optional.get();
                editBanner.setName(editedBanner.getName());
                editBanner.setPrice(editedBanner.getPrice());
                editBanner.setCategories(editedBanner.getCategories());
                editBanner.setText(editedBanner.getText());
                return new ResponseEntity<>(bannerRepository.save(editBanner), HttpStatus.OK);

            } else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Banner> deleteBanner(Long id) {
        try {

            Optional<Banner> banner = bannerRepository.findById(id);

            if (banner.isPresent()) {
                Banner bannerForDelete = banner.get();
                bannerForDelete.setDeleted(true);
                return new ResponseEntity<>(bannerRepository.save(bannerForDelete), HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

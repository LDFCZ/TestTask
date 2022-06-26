package ru.nsu.ccfit.lopatkin.testTask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.lopatkin.testTask.models.Banner;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;
import ru.nsu.ccfit.lopatkin.testTask.repositories.CategoryRepository;
import ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler.ErrorResponse;
import ru.nsu.ccfit.lopatkin.testTask.utils.errorHandler.Violation;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<List<Category>> findCategoryByName(String name) {
        try {
            List<Category> categories = categoryRepository.findAllByNameContainingIgnoreCaseAndDeletedIsFalse(name);
            if (categories.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<List<Category>> getAllNotDeletedCategories() {
        try {
            List<Category> categories = categoryRepository.findAllByDeletedIsFalse();
            if (categories.isEmpty())
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            return new ResponseEntity<>(categories, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<Category> getCategoryById(Long id) {
        try {
            Optional<Category> category = categoryRepository.findByIdAndDeletedIsFalse(id);
            return category.map(value -> new ResponseEntity<>(value, HttpStatus.OK)).orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> createNewCategory(Category category) {
       try {
           ErrorResponse errorResponse = new ErrorResponse();
           if (categoryRepository.existsCategoryByNameAndDeletedIsFalse(category.getName())) {
               errorResponse.getViolations().add(new Violation("Name", String.format("Category with name [%s] already exist", category.getName())));
               return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
           } else if (categoryRepository.existsCategoryByRequestIdAndDeletedIsFalse(category.getRequestId())) {
               errorResponse.getViolations().add(new Violation("ID", String.format("Category with request ID [%s] already exist", category.getRequestId())));
               return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
           } else
               return new ResponseEntity<>(categoryRepository.save(new Category(category.getName(), category.getRequestId())), HttpStatus.OK);
       } catch (Exception e) {
           e.printStackTrace();
           return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    public ResponseEntity<?> editCategory(Category editedCategory, Long id) {
        try {
            Optional<Category> optional = categoryRepository.findById(id);
            ErrorResponse errorResponse = new ErrorResponse();
            if (optional.isPresent()) {
                if (categoryRepository.existsCategoryByNameAndIdNotLikeAndDeletedIsFalse(editedCategory.getName(), id)) {
                    errorResponse.getViolations().add(new Violation("Name", String.format("Category with name [%s] already exist", editedCategory.getName())));
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
                else if (categoryRepository.existsCategoryByRequestIdAndIdNotLikeAndDeletedIsFalse(editedCategory.getRequestId(), id)) {
                    errorResponse.getViolations().add(new Violation("ID", String.format("Category with request name [%s] already exist", editedCategory.getRequestId())));
                    return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
                }
                else{
                    Category category = optional.get();
                    category.setName(editedCategory.getName());
                    category.setRequestId(editedCategory.getRequestId());
                    return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
                }
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public ResponseEntity<?> deleteCategory(Long id) {
        try {
            Optional<Category> optional = categoryRepository.findById(id);
            if (optional.isPresent()) {
                Category category = optional.get();

                for (Banner banner : category.getBanners())
                    if (!banner.isDeleted()) {
                        ErrorResponse errorResponse = new ErrorResponse();
                        errorResponse.getViolations().add(new Violation("Name",
                                String.format("Banner with name: %s is not deleted, you cannot delete this category", banner.getName())));
                        return new ResponseEntity<>(errorResponse, HttpStatus.METHOD_NOT_ALLOWED);
                    }

                category.setDeleted(true);
                return new ResponseEntity<>(categoryRepository.save(category), HttpStatus.OK);
            } else return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

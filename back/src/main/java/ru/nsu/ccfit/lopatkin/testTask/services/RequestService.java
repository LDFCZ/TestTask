package ru.nsu.ccfit.lopatkin.testTask.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.nsu.ccfit.lopatkin.testTask.models.Banner;
import ru.nsu.ccfit.lopatkin.testTask.models.Category;
import ru.nsu.ccfit.lopatkin.testTask.models.Request;
import ru.nsu.ccfit.lopatkin.testTask.repositories.BannerRepository;
import ru.nsu.ccfit.lopatkin.testTask.repositories.CategoryRepository;
import ru.nsu.ccfit.lopatkin.testTask.repositories.RequestRepository;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class RequestService {

    private final RequestRepository requestRepository;

    private final BannerRepository bannerRepository;

    private final CategoryRepository categoryRepository;

    @Autowired
    public RequestService(RequestRepository requestRepository, BannerRepository bannerRepository, CategoryRepository categoryRepository) {
        this.requestRepository = requestRepository;
        this.bannerRepository = bannerRepository;
        this.categoryRepository = categoryRepository;
    }

    public ResponseEntity<?> getBannerByCategories(String userAgent, List<String> requestIds, HttpServletRequest ipAddress) {
        try {
            TreeSet<Banner> banners = new TreeSet<>(new Banner());

            for (String requestId : requestIds) {
                Optional<Category> category = categoryRepository.findByRequestIdAndDeletedIsFalse(requestId);

                if (category.isEmpty()) continue;

                banners.addAll(bannerRepository.findByCategoriesContainingAndDeletedIsFalseOrderByPriceDesc(category.get()));
            }
            if (banners.size() == 0) {
                requestRepository.save(new Request(null, userAgent, ipAddress.getRemoteAddr(), LocalDateTime.now(), "NO BANNERS WITH THIS CATEGORIES"));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            List<Banner> sortedBanners = new ArrayList<>(banners);
            List<Request> requests = requestRepository.findAllByDateTimeGreaterThanAndUserAgentAndIpAddress(
                    LocalDateTime.now().minusDays(1),
                    userAgent,
                    ipAddress.getRemoteAddr());

            int i = 0;
            for (Request request : requests) {
                Banner b = request.getBannerId();
                if (b != null)
                    if (b.getId().equals(sortedBanners.get(i).getId())) i++;
            }

            if (i == sortedBanners.size()) {
                requestRepository.save(new Request(null, userAgent, ipAddress.getRemoteAddr(), LocalDateTime.now(), "BANNER LIMIT"));
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            requestRepository.save(new Request(sortedBanners.get(i), userAgent, ipAddress.getRemoteAddr(), LocalDateTime.now(), "OK"));
            return new ResponseEntity<>(sortedBanners.get(i).getText(), HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }
}

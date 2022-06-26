package ru.nsu.ccfit.lopatkin.testTask.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.nsu.ccfit.lopatkin.testTask.models.Banner;
import ru.nsu.ccfit.lopatkin.testTask.services.BannerService;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RolesAllowed("ROLE_ADMIN")
@RequestMapping("/")
public class BannerController {

    private final BannerService bannerService;

    @Autowired
    public BannerController(BannerService bannerService) {
        this.bannerService = bannerService;
    }

    @GetMapping("banners")
    public ResponseEntity<List<Banner>> getBanners(@RequestParam(required = false) String name) {
        try {
            if (name != null)
                return bannerService.findNotDeletedBannerByName(name);
            else
                return bannerService.getAllNotDeletedBanners();
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


    @GetMapping("banners/{id}")
    public ResponseEntity<Banner> getBanner(@PathVariable Long id) {
        try {
            return bannerService.getNotDeletedBannerById(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("banners")
    public ResponseEntity<?> createNewBanner(@Valid @RequestBody Banner banner) {
        try {
            return bannerService.createNewBanner(banner);
        } catch (Exception e) {
            return  new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("banners/{id}")
    public ResponseEntity<?> editBanner(@Valid @RequestBody Banner editedBanner, @PathVariable Long id) {
        try {
            return bannerService.editBanner(editedBanner, id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("banners/{id}")
    public ResponseEntity<Banner> deleteBanner(@PathVariable Long id) {
        try {
            return bannerService.deleteBanner(id);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

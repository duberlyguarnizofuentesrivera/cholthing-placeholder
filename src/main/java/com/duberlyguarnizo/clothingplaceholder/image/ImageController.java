package com.duberlyguarnizo.clothingplaceholder.image;

import org.springframework.web.bind.annotation.RestController;

@RestController
public class ImageController {
    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }
}

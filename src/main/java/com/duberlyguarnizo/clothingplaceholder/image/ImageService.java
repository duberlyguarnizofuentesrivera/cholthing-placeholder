/*
 * Copyright (c) 2023. Duberly Guarnizo
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the “Software”), to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED “AS IS”, WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package com.duberlyguarnizo.clothingplaceholder.image;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@Service
@Slf4j
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    Optional<ImageQueryDto> getImage(Long id, int w, int h, int p, String c, String r) {
        var possibleImage = imageRepository.findById(id);
        if (possibleImage.isPresent()) {
            Image image = possibleImage.get();
            Optional<BufferedImage> imageFromDisk = getImageFromDisk(image.getPath());
            if (imageFromDisk.isEmpty()) {
                return Optional.empty();
            }
            //TODO: Implement the resizing logic with Thumbnailator
            return Optional.of(ImageQueryDto.builder()
                    .name(possibleImage.get().getName())
                    .imageBlob(null)
                    .build());
        }
        return Optional.empty();
    }

    private Optional<BufferedImage> getImageFromDisk(String path) {
        try {
            String imageFolder = "/images/uploads/";
            String filePath = imageFolder + path;
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                return Optional.empty();
            }
            return Optional.of(ImageIO.read(imageFile));
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Could not read the image with path " + path + ". Reason: " + e.getMessage());
            return Optional.empty();
        }

    }
    // TODO: 29/07/2023 Create method for image processing
}


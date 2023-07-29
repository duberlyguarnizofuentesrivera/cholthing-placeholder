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

    Optional<Long> saveImage(ImageCreationDto imageCreationDto) {
        String uploadDir = "/images/uploads/";
        try {
            Files.createDirectories(Paths.get(uploadDir));
            String filename = imageCreationDto.name() + ".png";
            Path filePath = Paths.get(uploadDir + filename);
            Files.write(filePath, imageCreationDto.imageBlob().getBytes());
            Image savedImage = Image.builder()
                    .name(imageCreationDto.name())
                    .path(filePath.toString())
                    .build();
            return Optional.of(imageRepository.save(savedImage).getId());

        } catch (IOException e) {
            log.warn("Could not save the image with name " + imageCreationDto.name() + ". Reason: " + e.getMessage());
            return Optional.empty();
        }
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

    Optional<BufferedImage> getImageFromDisk(String path) {
        try {
            String imageFolder = "/images/uploads/";
            String filePath = imageFolder + path;
            File imageFile = new File(filePath);
            if (!imageFile.exists()) {
                return Optional.empty();
            }
            return Optional.of(ImageIO.read(imageFile));
        } catch (Exception e) {
            log.warn("Could not read the image with path " + path + ". Reason: " + e.getMessage());
            return Optional.empty();
        }

    }
}

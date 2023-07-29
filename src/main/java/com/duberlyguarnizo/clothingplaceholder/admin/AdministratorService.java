package com.duberlyguarnizo.clothingplaceholder.admin;

import com.duberlyguarnizo.clothingplaceholder.image.Image;
import com.duberlyguarnizo.clothingplaceholder.image.ImageCreationDto;
import com.duberlyguarnizo.clothingplaceholder.image.ImageRepository;
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

@Slf4j
@Service
public class AdministratorService {
    private final ImageRepository imageRepository;

    public AdministratorService(ImageRepository imageRepository) {
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
    // TODO: 29/07/2023 Create Delete image method
    // TODO: 29/07/2023 Create Update image method

}

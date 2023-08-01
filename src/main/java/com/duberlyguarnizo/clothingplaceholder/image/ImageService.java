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
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class ImageService {
    private static final int DEFAULT_HEIGHT = 1080;
    private static final int DEFAULT_WIDTH = 1920;
    private static final String IMAGE_FOLDER = "images/uploads/";
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Retrieves a page of images.
     *
     * @param pageable The information of the requested page (page, size, sort, etc.).
     * @return The page of images, or a page with no content if there are no images.
     */
    public Page<Image> getImages(Pageable pageable) {
        return imageRepository.findAll(pageable);
    }

    /**
     * Retrieves an image by its ID.
     *
     * @param id The ID of the image.
     * @return The optional image object associated with the provided ID.
     */
    public Optional<Image> getImage(Long id) {
        return imageRepository.findById(id);
    }


    /**
     * Retrieves an image by its ID and processes it based on the provided requirements.
     *
     * @param id         The ID of the image.
     * @param reqWidth   The requested width of the image.
     * @param reqHeight  The requested height of the image.
     * @param reqPadding The requested padding of the image.
     * @return The optional processed image object associated with the provided ID and requirements.
     */
    public Optional<BufferedImage> getImage(Long id, Integer reqWidth, Integer reqHeight, Integer reqPadding) {
        return imageRepository.findById(id)
                .flatMap(image -> getImageFromDisk(image.getPath())
                        .map(imageData -> getProcessedBufferedImage(reqWidth, reqHeight, imageData))
                        .map(finalImage -> {
                            if (reqPadding != null) {
                                log.warn("adding padding to image");
                                return addPaddingToImage(finalImage, reqPadding);
                            }
                            return finalImage;
                        })
                );
    }

    public Optional<BufferedImage> getRandomImage(Integer reqWidth, Integer reqHeight, Integer reqPadding) {
        long numberOfRecords = imageRepository.count();
        log.warn("Number of records: " + numberOfRecords);
        long randomId = ThreadLocalRandom.current().nextLong(numberOfRecords);
        log.warn("chosen record: {}", randomId);
        Page<Image> page = imageRepository.findAll(PageRequest.of(Math.toIntExact(randomId), 1));
        if (page.hasContent()) {
            log.warn("Page has content!");
            log.warn("Id of found content {}:", page.getContent().get(0).getId());
            return getImage(page.getContent().get(0).getId(), reqWidth, reqHeight, reqPadding);
        }
        return Optional.empty();
    }


    /**
     * Retrieves an image with the specified ID and applies resizing and padding if necessary.
     *
     * @param reqWidth      The requested width for the image.
     * @param reqHeight     The requested height for the image.
     * @param imageFromDisk The original image retrieved from the disk.
     * @return The processed BufferedImage with the requested size and format.
     */
    private BufferedImage getProcessedBufferedImage(Integer reqWidth, Integer reqHeight, BufferedImage imageFromDisk) {
        try {
            Thumbnails.Builder<BufferedImage> resizedImage = Thumbnails.of(imageFromDisk);
            assignImageSize(resizedImage, reqWidth, reqHeight);
            resizedImage = resizedImage.outputFormat("png");
            return resizedImage.asBufferedImage();
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Could not resize the image. Reason: " + e.getMessage() + " Returning default image");
            return new BufferedImage(DEFAULT_WIDTH, DEFAULT_HEIGHT, BufferedImage.TYPE_INT_ARGB);
        }
    }

    /**
     * Assigns the requested size to the given Thumbnails.Builder object based on the requested width and height.
     *
     * @param resizedImage The Thumbnails.Builder object to assign the size to.
     * @param reqWidth     The requested width for the image. If null, the width will not be modified.
     * @param reqHeight    The requested height for the image. If null, the height will not be modified.
     */
    private void assignImageSize(Thumbnails.Builder<BufferedImage> resizedImage, Integer reqWidth, Integer reqHeight) {
        if (reqWidth != null && reqHeight != null) {
            resizedImage.forceSize(reqWidth, reqHeight);
        } else if (reqWidth == null && reqHeight == null) {
            resizedImage.size(DEFAULT_WIDTH, DEFAULT_HEIGHT);
        } else {
            resizedImage = reqWidth == null ? resizedImage.height(reqHeight) : resizedImage.width(reqWidth);
        }
    }

    /**
     * Retrieves an image from disk based on the provided file path.
     *
     * @param path The relative file path to the image.
     * @return An optional containing the BufferedImage of the image if it exists, otherwise an empty optional.
     */
    private Optional<BufferedImage> getImageFromDisk(String path) {
        try {
            Path filePath = Paths.get(IMAGE_FOLDER, path);
            if (!Files.exists(filePath)) {
                log.info("Can't find path: {},User directory is: \n {}", System.getProperty("user.dir"), IMAGE_FOLDER + "/" + path);
                return Optional.empty();
            }
            return Optional.of(ImageIO.read(filePath.toFile()));
        } catch (IOException | IllegalArgumentException e) {
            log.warn("Could not read the image with path {}: {}", path, e.getMessage());
            return Optional.empty();
        }
    }

    /**
     * Adds padding to an image.
     *
     * @param originalImage The original BufferedImage to which padding needs to be added.
     * @param padding       The amount of padding to add to each side of the image.
     * @return The new BufferedImage with padding added.
     */
    private BufferedImage addPaddingToImage(BufferedImage originalImage, int padding) {
        int newWidth = originalImage.getWidth() + 2 * padding;
        int newHeight = originalImage.getHeight() + 2 * padding;

        BufferedImage paddedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
        Graphics2D g2d = paddedImage.createGraphics();

        // Fill the new image with a transparent background (optional)
        g2d.setColor(new java.awt.Color(0, 0, 0, 0));
        g2d.fillRect(0, 0, newWidth, newHeight);

        // Draw the original image onto the new image with padding
        g2d.drawImage(originalImage, padding, padding, null);
        g2d.dispose();

        return paddedImage;
    }
}


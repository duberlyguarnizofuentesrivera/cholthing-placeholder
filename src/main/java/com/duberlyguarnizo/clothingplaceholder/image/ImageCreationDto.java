package com.duberlyguarnizo.clothingplaceholder.image;

import lombok.Builder;
import org.springframework.web.multipart.MultipartFile;

@Builder
public record ImageCreationDto(String name, MultipartFile imageBlob) {
}

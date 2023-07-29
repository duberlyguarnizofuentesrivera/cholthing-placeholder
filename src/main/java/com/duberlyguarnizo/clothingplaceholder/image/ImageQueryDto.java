package com.duberlyguarnizo.clothingplaceholder.image;

import lombok.Builder;
import org.springframework.core.io.InputStreamResource;

@Builder
public record ImageQueryDto(Long id, String name, InputStreamResource imageBlob) {
}

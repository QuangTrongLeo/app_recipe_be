package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.response.ApiResponse;
import recipe_be.dto.response.ImageResponse;
import recipe_be.service.ImageService;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/images")
public class ImageController {
    private final ImageService imageService;

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping
    public ApiResponse<List<ImageResponse>> getAllImages() {
        return ApiResponse.<List<ImageResponse>>builder()
                .result(imageService.getAllImages())
                .build();
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    @GetMapping("/{id}")
    public ApiResponse<ImageResponse> getImageById(@PathVariable String id) {
        return ApiResponse.<ImageResponse>builder()
                .result(imageService.getImageById(id))
                .build();
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{id}")
    public ApiResponse<Boolean> deleteImageById(@PathVariable String id) {
        imageService.deleteById(id);
        return ApiResponse.<Boolean>builder()
                .result(true)
                .build();
    }
}


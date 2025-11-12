package recipe_be.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import recipe_be.dto.response.APIResponse;
import recipe_be.service.ImageService;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.recipe.app.url}/images")
public class ImageController {

    private final ImageService imageService;

    @GetMapping
    public APIResponse getAllImages() {
        return APIResponse.builder(imageService.getAllImages()).build();
    }

    @GetMapping("/{id}")
    public APIResponse getImageById(@PathVariable String id) {
        return APIResponse.builder(imageService.getImageById(id)).build();
    }

    @DeleteMapping("/{id}")
    public APIResponse deleteImageById(@PathVariable String id) {
        imageService.deleteById(id);
        return APIResponse.builder("Xóa ảnh thành công").build();
    }
}


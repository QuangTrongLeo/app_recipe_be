package recipe_be.mapper;

import org.mapstruct.Mapper;
import recipe_be.dto.response.ImageResponse;
import recipe_be.entity.Image;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ImageMapper {
    ImageResponse toImageResponse(Image image);
    List<ImageResponse> toImageResponseList(List<Image> images);
}

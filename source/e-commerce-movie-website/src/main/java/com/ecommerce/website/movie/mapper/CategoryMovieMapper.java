package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.category.movie.CategoryMovieDto;
import com.ecommerce.website.movie.model.CategoryMovie;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE,
        uses = CategoryMapper.class)
public interface CategoryMovieMapper {
    @Mapping(source = "category", target = "category", qualifiedByName = "toCategoryMovieDto")
    @BeanMapping(ignoreByDefault = true)
    @Named("toCategoryMovieDto")
    CategoryMovieDto toCategoryMovieDto(CategoryMovie categoryMovie);

    @IterableMapping(elementTargetType = CategoryMovieDto.class, qualifiedByName = "toCategoryMovieDto")
    @Named("toCategoryMovieDtoList")
    List<CategoryMovieDto> toCategoryMovieDtoList(List<CategoryMovie> categoryMovieList);
}

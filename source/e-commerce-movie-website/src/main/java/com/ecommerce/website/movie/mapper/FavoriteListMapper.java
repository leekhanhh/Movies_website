package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.favoritelist.FavoriteListDto;
import com.ecommerce.website.movie.dto.movie.MovieDto;
import com.ecommerce.website.movie.form.favoritelist.CreateFavoriteListForm;
import com.ecommerce.website.movie.model.FavoriteList;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface FavoriteListMapper {
    @Mapping(source = "account.id", target = "account.id")
    @Mapping(source = "movie.id", target = "movie.id")
    @Mapping(source = "movie.genres", target = "movie.genres")
    @Named("fromFavoriteItemToDto")
    FavoriteListDto toDto(FavoriteList favoriteList);

    @Mapping(source = "accountId", target = "account.id")
    @Mapping(source = "movieId", target = "movie.id")
    FavoriteList fromCreateFavoriteItem(CreateFavoriteListForm favoriteListForm);

    @Mapping(source = "movie.id", target = "id")
    @Mapping(source = "movie.title", target = "title")
    @Mapping(source = "movie.overview", target = "overview")
    @Mapping(source = "movie.imagePath", target = "imagePath")
    @Mapping(source = "movie.category", target = "category")
    @Mapping(source = "movie.genres", target = "genres")
    @BeanMapping(ignoreByDefault = true)
    @Named("fromFavoriteItemToMovieDto")
    MovieDto fromFavoriteItemToMovieDto(FavoriteList favoriteList);

    @IterableMapping(elementTargetType = MovieDto.class, qualifiedByName = "fromFavoriteItemToMovieDto")
    List<MovieDto> toMovieDtoList(List<FavoriteList> favoriteListList);


    @IterableMapping(elementTargetType = FavoriteListDto.class, qualifiedByName = "fromFavoriteItemToDto")
    List<FavoriteListDto> toDtoList(List<FavoriteList> favoriteListList);
}

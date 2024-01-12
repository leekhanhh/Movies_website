package com.ecommerce.website.movie.mapper;

import com.ecommerce.website.movie.dto.category.CategoryDto;
import com.ecommerce.website.movie.form.category.CreateCategoryForm;
import com.ecommerce.website.movie.form.category.UpdateCategoryForm;
import com.ecommerce.website.movie.model.Category;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE,
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface CategoryMapper {
    // Set thuoc tinh cho luc tao doi tuong moi
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @BeanMapping(ignoreByDefault = true)
    Category formCreateCategory(CreateCategoryForm categoryForm);

    // Response cho doi tuong muon tra ve api ntn
    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "parentCategory.id", target = "parentId")
    @Mapping(source = "createdDate", target = "createdDate")
    @Mapping(source = "modifiedDate", target = "modifiedDate")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    CategoryDto toCategoryDto(Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "kind", target = "kind")
    @Named("toCategoryMovieDto")
    @BeanMapping(ignoreByDefault = true)
    CategoryDto toCategoryMovieDto(Category category);

    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "ordering", target = "ordering")
    @BeanMapping(ignoreByDefault = true)
    void mappingUpdateForm(UpdateCategoryForm categoryForm, @MappingTarget Category category);

    @Mapping(source = "id", target = "id")
    @Mapping(source = "name", target = "name")
    @Mapping(source = "description", target = "description")
    @Mapping(source = "kind", target = "kind")
    @Mapping(source = "parentCategory.id", target = "parentId")
    @Mapping(source = "status", target = "status")
    @BeanMapping(ignoreByDefault = true)
    @Named("adminMappingDto")
    CategoryDto toCategoryAdminDtoList(Category category);

    @IterableMapping(elementTargetType = CategoryDto.class, qualifiedByName = "adminMappingDto")
    List<CategoryDto> toCategoryList(List<Category> categoryList);
}

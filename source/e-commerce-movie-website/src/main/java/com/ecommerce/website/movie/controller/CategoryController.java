package com.ecommerce.website.movie.controller;

import com.ecommerce.website.movie.constant.Constant;
import com.ecommerce.website.movie.dto.ApiResponseDto;
import com.ecommerce.website.movie.dto.ErrorCode;
import com.ecommerce.website.movie.dto.ResponseListDto;
import com.ecommerce.website.movie.dto.category.CategoryDto;
import com.ecommerce.website.movie.form.category.CreateCategoryForm;
import com.ecommerce.website.movie.form.category.UpdateCategoryForm;
import com.ecommerce.website.movie.mapper.CategoryMapper;
import com.ecommerce.website.movie.model.Category;
import com.ecommerce.website.movie.model.criteria.CategoryCriteria;
import com.ecommerce.website.movie.repository.CategoryRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/v1/category")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Slf4j
public class CategoryController {
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    CategoryMapper categoryMapper;

    @PostMapping(value = "/create", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<CategoryDto> createCategory(@Valid @RequestBody CreateCategoryForm categoryForm, BindingResult bindingResult) {
        ApiResponseDto<CategoryDto> apiResponseDto = new ApiResponseDto<>();
        Category category = categoryRepository.findFirstByName(categoryForm.getName());
        if (category != null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.DUPLICATED_CATEGORY_NAME_ERROR);
            apiResponseDto.setMessage("Duplicated Category Error");
            return apiResponseDto;
        }
        Category parent = null;
        if (categoryForm.getParentId() != null) {
            parent = categoryRepository.findById(categoryForm.getParentId()).orElse(null);
            if (parent == null) {
                apiResponseDto.setResult(false);
                apiResponseDto.setCode(ErrorCode.PARENT_CATEGORY_ERROR_NOT_FOUND);
                apiResponseDto.setMessage("Category not found");
                return apiResponseDto;
            }
        }
        category = categoryMapper.formCreateCategory(categoryForm);
        category.setParentCategory(parent);
        categoryRepository.save(category);

        apiResponseDto.setMessage("Category has been saved successfully!");
        apiResponseDto.setData(categoryMapper.toCategoryDto(category));
        return apiResponseDto;
    }

    @PutMapping(value = "/update", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<CategoryDto> updateCategory(@Valid @RequestBody UpdateCategoryForm updateCategoryForm, BindingResult bindingResult) {
        ApiResponseDto<CategoryDto> apiResponseDto = new ApiResponseDto<>();
        Category category = categoryRepository.findById(updateCategoryForm.getId()).orElse(null);
        if (category == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.CATEGORY_NOT_FOUND);
            apiResponseDto.setMessage("Category not found");
            return apiResponseDto;
        }

        categoryMapper.mappingUpdateForm(updateCategoryForm, category);
        categoryRepository.save(category);
        apiResponseDto.setData(categoryMapper.toCategoryDto(category));
        apiResponseDto.setMessage("Update category successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<CategoryDto>> listCategory(CategoryCriteria categoryCriteria, Pageable pageable) {
        ApiResponseDto<ResponseListDto<CategoryDto>> apiResponseDto = new ApiResponseDto<>();
        Page<Category> categoryPage = categoryRepository.findAll(categoryCriteria.categorySpecification(), pageable);
        ResponseListDto<CategoryDto> responseListDto = new ResponseListDto(categoryMapper.toCategoryList(categoryPage.getContent()), categoryPage.getTotalElements(), categoryPage.getTotalPages());
        apiResponseDto.setData(responseListDto);
        apiResponseDto.setMessage("Get category list successfully!");
        return apiResponseDto;
    }

    @DeleteMapping(value = "/delete/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @Transactional
    public ApiResponseDto<String> deleteCategory(@PathVariable("id") Long id) {

        ApiResponseDto<String> apiResponseDto = new ApiResponseDto<>();
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.CATEGORY_NOT_FOUND);
            apiResponseDto.setMessage("Category id not found!");
            return apiResponseDto;
        }
        categoryRepository.deleteById(id);
        apiResponseDto.setMessage("Delete category successfully!");
        return apiResponseDto;
    }

    @GetMapping(value = "/get/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<CategoryDto> getCategory(@PathVariable("id") Long id) {
        ApiResponseDto<CategoryDto> apiResponseDto = new ApiResponseDto<>();
        Category category = categoryRepository.findById(id).orElse(null);
        if (category == null) {
            apiResponseDto.setResult(false);
            apiResponseDto.setCode(ErrorCode.CATEGORY_NOT_FOUND);
            apiResponseDto.setMessage("Category id not found!");
            return apiResponseDto;
        }
        apiResponseDto.setMessage("Get a category successfully!");
        apiResponseDto.setData(categoryMapper.toCategoryDto(category));
        return apiResponseDto;
    }

    @GetMapping(value = "/auto-complete", produces = MediaType.APPLICATION_JSON_VALUE)
    public ApiResponseDto<ResponseListDto<CategoryDto>> autoCompleteListCategory(CategoryCriteria categoryCriteria, @PageableDefault(size = 20) Pageable pageable) {
        ApiResponseDto<ResponseListDto<CategoryDto>> apiMessageDto = new ApiResponseDto<>();
        categoryCriteria.setStatus(Constant.STATUS_ACTIVE);
        Page<Category> listCategory = categoryRepository.findAll(categoryCriteria.categorySpecification(), pageable);
        ResponseListDto<CategoryDto> responseListObj = new ResponseListDto(categoryMapper.fromEntityListToCategoryDtoAutoComplete(listCategory.getContent()), listCategory.getTotalElements(), listCategory.getTotalPages());
        apiMessageDto.setData(responseListObj);
        apiMessageDto.setMessage("Get auto-complete list category success.");
        return apiMessageDto;
    }
}

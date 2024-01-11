package com.ecommerce.website.movie.dto;

public class ErrorCode {
    /**
     * General error code
     */
    public static final String GENERAL_ERROR_UNAUTHORIZED = "ERROR-GENERAL-000";

    /**
     * Category error code
     */
    public static final String PARENT_CATEGORY_ERROR_NOT_FOUND = "ERROR-CATEGORY-000";
    public static final String DUPLICATED_CATEGORY_NAME_ERROR = "ERROR-CATEGORY-001";
    public static final String CATEGORY_NOT_FOUND = "ERROR-CATEGORY-002";
}
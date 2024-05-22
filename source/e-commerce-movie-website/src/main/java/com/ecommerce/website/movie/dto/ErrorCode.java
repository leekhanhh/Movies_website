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

    /**
     * Movie error code
     */
    public static final String MOVIE_NOT_FOUND = "ERROR-MOVIE-000";

    /**
     * Participant error code
     */
    public static final String PARTICIPANT_NOT_FOUND = "ERROR-PARTICIPANT-000";

    /**
     * Account error code
     */
    public static final String ACCOUNT_EMAIL_DUPLICATED = "ERROR-ACCOUNT-000";
    public static final String ACCOUNT_EMAIL_NOT_FOUND = "ERROR-ACCOUNT-001";
    public static final String ACCOUNT_NOT_FOUND = "ERROR-ACCOUNT-002";

    public static final String USER_ACCOUNT_NOT_FOUND = "ERROR-ACCOUNT-003";
    /**
     * Movie genre error code
     */
    public static final String MOVIE_GENRE_NOT_FOUND = "ERROR-GENRE-000";
}

package com.ecommerce.website.movie.dto;

public class ErrorCode {
    /**
     * General error code
     */
    public static final String GENERAL_ERROR_UNAUTHORIZED = "ERROR-GENERAL-000";
    public static final String GENERAL_ERROR_FORBIDDEN = "ERROR-GENERAL-001";
    public static final String GENERAL_ERROR_BAD_REQUEST = "ERROR-GENERAL-002";

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
    public static final String MOVIE_NOT_SERIES = "ERROR-MOVIE-001";


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
    public static final String ACCOUNT_CREATION_FAILED = "ERROR-ACCOUNT-003";

    public static final String USER_ACCOUNT_NOT_FOUND = "ERROR-ACCOUNT-003";

    /**
     * User error code
     */
    public static final String USER_CREATION_FAILED = "ERROR-USER-000";
    public static final String USER_ACCOUNT_EXIST = "ERROR-USER-001";

    /**
     * Movie genre error code
     */
    public static final String MOVIE_GENRE_NOT_FOUND = "ERROR-GENRE-000";

    /**
     * Favorite item error code
     */
    public static final String FAVORITE_ITEM_NOT_FOUND = "ERROR-FAVORITE-000";
    public static final String FAVORITE_ITEM_ALREADY_EXIST = "ERROR-FAVORITE-001";

    /**
     * Review error code
     */
    public static final String REVIEW_NOT_FOUND = "ERROR-REVIEW-000";

    /**
     * Vote movie error code
     */
    public static final String VOTE_MOVIE_EXIST = "ERROR-VOTE-MOVIE-000";
    public static final String VOTE_MOVIE_NOT_FOUND = "ERROR-VOTE-MOVIE-001";

    /**
     * Rating error code
     */
    public static final String RATING_ALREADY_EXIST = "ERROR-RATING-000";
    public static final String RATING_NOT_FOUND = "ERROR-RATING-001";

    /**
     * Video error code
     */
    public static final String VIDEO_NOT_FOUND = "ERROR-VIDEO-000";
    public static final String VIDEO_CREATION_FAILED = "ERROR-VIDEO-001";
    public static final String VIDEO_DELETE_FAILED = "ERROR-VIDEO-002";
    public static final String MOVIE_VIDEO_EXISTED = "ERROR-VIDEO-003";

    /**
     * Episode error code
     */
    public static final String EPISODE_NOT_FOUND = "ERROR-EPISODE-000";
    public static final String DUPLICATED_EPISODE_NUMBER_ERROR = "ERROR-EPISODE-001";

    /**
     * OTP error code
     */
    public static final String ACCOUNT_OTP_NOT_EXPIRED = "ERROR-OTP-000";
    public static final String ACCOUNT_OTP_NOT_FOUND = "ERROR-OTP-001";
    public static final String ACCOUNT_OTP_EXPIRED = "ERROR-OTP-002";
    public static final String ACCOUNT_OTP_INVALID = "ERROR-OTP-003";

}

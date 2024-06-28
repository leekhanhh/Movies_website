package com.ecommerce.website.movie.constant;

public class Constant {
    public static final Integer CATEGORY_KIND_MOVIE_GENRE = 1;
    public static final Integer CATEGORY_KIND_MOVIE_TYPE = 0;

    //RestClientConfig
    //for dateOfBirth
    public static final String DATE_TIME_FORMAT = "dd/MM/yyyy HH:mm:ss";
    public static final String DATE_FORMAT = "dd/MM/yyyy";

    //Person kind
    public static final Integer PERSON_KIND_ACTOR = 1;
    public static final Integer PERSON_KIND_DIRECTOR = 0;

    //Role
    public static final Integer ROLE_USER = 1;
    public static final Integer ROLE_ADMIN = 0;
    //Gender
    public static final Integer MALE = 1;
    public static final Integer FEMALE = 0;

    //Status constant
    public static final Integer STATUS_ACTIVE = 1;
    public static final Integer STATUS_PENDING = 0;

    //Review emotion
    public static final Integer EMOTION_LIKE = 1;
    public static final Integer EMOTION_LOVE = 2;
    public static final Integer EMOTION_HAHA = 3;
    public static final Integer EMOTION_WOW = 4;
    public static final Integer EMOTION_SAD = 5;
    public static final Integer EMOTION_ANGRY = 6;

    //Http
    public static final String VIDEO = "https://movies-website-tlcn-project.s3.ap-southeast-1.amazonaws.com/";

    public static final String CONTENT_TYPE = "Content-Type";
    public static final String CONTENT_LENGTH = "Content-Length";
    public static final String AWS_ENDPOINT = "https://movies-website-tlcn-project.s3.ap-southeast-1.amazonaws.com/";
    public static final String CONTENT_RANGE = "Content-Range";
    public static final String ACCEPT_RANGES = "Accept-Ranges";
    public static final String BYTES = "bytes";
    public static final int CHUNK_SIZE = 1048576;
    public static final int BYTE_RANGE = 1024;

}

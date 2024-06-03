package com.ecommerce.website.movie.dto;

import lombok.Data;
import java.io.InputStream;

@Data
public class VideoResponseDto {
    private String title;
    private InputStream stream;
}

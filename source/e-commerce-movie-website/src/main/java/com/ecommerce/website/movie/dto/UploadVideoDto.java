package com.ecommerce.website.movie.dto;

import lombok.Data;

@Data
public class UploadVideoDto {
    private String videoPath;
    private String videoName;
    private Long size;
}

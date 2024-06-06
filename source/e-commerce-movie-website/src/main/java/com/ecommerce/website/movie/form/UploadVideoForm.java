package com.ecommerce.website.movie.form;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;

@Data
public class UploadVideoForm {
    @ApiModelProperty(name = "video", required = true)
    @NotNull(message = "video cannot be null!")
    private MultipartFile video;
}

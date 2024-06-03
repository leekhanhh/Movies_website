package com.ecommerce.website.movie.config.aws;

import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.ecommerce.website.movie.constant.Constant;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AWS3Config {
    @Value("${cloud.aws.credentials.access.key}")
    private String accessKey;

    @Value("${cloud.aws.credentials.secret.key}")
    private String secretKey;

    @Bean
    AmazonS3 s3Client() {
        return AmazonS3ClientBuilder.standard()
                .withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
                .withRegion(Constant.REGION_STATIC)
                .build();
    }
}

package com.openmpy.taleswiki.support;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import software.amazon.awssdk.services.s3.S3Client;

@CustomServiceTest
public class ServiceTestSupport {

    @MockitoBean
    private S3Client s3Client;
}

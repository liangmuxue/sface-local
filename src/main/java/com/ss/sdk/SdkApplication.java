package com.ss.sdk;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableEncryptableProperties
public class SdkApplication {

    public static void main(String[] args) {
        SpringApplication sa = new SpringApplication(SdkApplication.class);
        sa.setBannerMode(Banner.Mode.OFF);
        sa.run(args);
    }

}

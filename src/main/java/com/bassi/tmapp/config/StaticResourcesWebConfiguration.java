package com.bassi.tmapp.config;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import tech.jhipster.config.JHipsterConstants;
import tech.jhipster.config.JHipsterProperties;

@Configuration
//@Profile({ JHipsterConstants.SPRING_PROFILE_PRODUCTION })
public class StaticResourcesWebConfiguration implements WebMvcConfigurer {

    @Value("${document-base-path}")
    private String baseUploadDirectory;

    protected static final String[] RESOURCE_LOCATIONS = { "classpath:/static/", "classpath:/static/content/", "classpath:/static/i18n/" };
    protected static final String[] RESOURCE_PATHS = {
        "/*.js",
        "/*.css",
        "/*.svg",
        "/*.png",
        "*.ico",
        "/content/**",
        "/i18n/*",
        "/files/**",
    };

    private final JHipsterProperties jhipsterProperties;

    public StaticResourcesWebConfiguration(JHipsterProperties jHipsterProperties) {
        this.jhipsterProperties = jHipsterProperties;
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        ResourceHandlerRegistration resourceHandlerRegistration = appendResourceHandler(registry);
        initializeResourceHandler(resourceHandlerRegistration);
    }

    protected ResourceHandlerRegistration appendResourceHandler(ResourceHandlerRegistry registry) {
        return registry.addResourceHandler(RESOURCE_PATHS);
    }

    protected void initializeResourceHandler(ResourceHandlerRegistration resourceHandlerRegistration) {
        resourceHandlerRegistration.addResourceLocations(RESOURCE_LOCATIONS).setCacheControl(getCacheControl());
        resourceHandlerRegistration
            .addResourceLocations("file:///" + Paths.get(baseUploadDirectory).toAbsolutePath().toString() + "/")
            .setCacheControl(getCacheControl());

        System.out.println("hi");
    }

    protected CacheControl getCacheControl() {
        return CacheControl.maxAge(getJHipsterHttpCacheProperty(), TimeUnit.DAYS).cachePublic();
    }

    private int getJHipsterHttpCacheProperty() {
        return jhipsterProperties.getHttp().getCache().getTimeToLiveInDays();
    }
}

package com.app.config;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@WebListener
public class CorsFilterInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        ServletContext servletContext = sce.getServletContext();
        FilterRegistration.Dynamic corsFilter = servletContext.addFilter("CorsFilter", CorsFilter.class);
        corsFilter.addMappingForUrlPatterns(null, false, "/*"); // Apply the filter to all URLs
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        // Cleanup logic, if needed
    }
}


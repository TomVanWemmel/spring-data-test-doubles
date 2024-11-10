package be.tvw.customer.web;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

@ComponentScan(basePackageClasses = WebConfig.class)
@PropertySource("classpath:app-web.properties")
public class WebConfig {
}

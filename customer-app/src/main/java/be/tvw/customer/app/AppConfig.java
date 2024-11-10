package be.tvw.customer.app;

import be.tvw.customer.DataConfig;
import be.tvw.customer.web.WebConfig;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@Import({DataConfig.class, WebConfig.class})
public class AppConfig {

}

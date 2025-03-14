package finalCDT.finalCDT.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Configuration
public class LoggingConfig {
    
    @Bean
    public Logger logger() {
        return LoggerFactory.getLogger("CDTBancolombia");
    }
}

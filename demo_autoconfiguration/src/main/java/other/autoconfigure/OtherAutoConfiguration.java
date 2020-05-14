package other.autoconfigure;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import other.bean.OtherBean;

@Configuration
public class OtherAutoConfiguration {

    @Bean
    public OtherBean otherBean() {
        return new OtherBean();
    }

}
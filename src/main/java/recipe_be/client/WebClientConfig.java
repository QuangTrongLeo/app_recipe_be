package recipe_be.client;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {
    @Value("${stripe.secret-key}")
    String stripeSecretKey;
    
    @Bean
    public WebClient stripeWebClient() {
        return WebClient.builder()
                .baseUrl("https://api.stripe.com")
                .defaultHeaders(headers -> {
                    headers.setBasicAuth(stripeSecretKey, "");
                    headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
                })
                .build();
    }
}

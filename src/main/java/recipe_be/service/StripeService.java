package recipe_be.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import recipe_be.dto.response.StripeCustomerResponse;

@Service
@AllArgsConstructor
public class StripeService {
    private final WebClient webClient;
    
    public StripeCustomerResponse createCustomer(String name, String email){
        return webClient.post()
                .uri("/v1/customers")
                .body(BodyInserters
                        .fromFormData("name", name)
                        .with("email", email))
                .retrieve()
                .bodyToMono(StripeCustomerResponse.class)
                .block();
    }
}


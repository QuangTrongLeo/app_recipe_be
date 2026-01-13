package recipe_be.dto.response;

import lombok.Data;

@Data
public class StripeCustomerResponse {
    private String id;
    private String email;
    private String name;
}
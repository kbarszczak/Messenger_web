package dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Validation {

    private String id;
    private String login;
    private String role;
    private Boolean hasValidToken;
    private String message;

}

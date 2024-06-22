package com.example.demo.request;

import com.example.demo.enums.Role;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Long id;
    private String email;
    private Role desiredRole;
    private String registrationToken;
}

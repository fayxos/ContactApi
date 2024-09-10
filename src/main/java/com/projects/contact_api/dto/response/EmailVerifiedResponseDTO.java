package com.projects.contact_api.dto.response;

import com.projects.contact_api.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EmailVerifiedResponseDTO {
    private Boolean isEmailVerified;
}

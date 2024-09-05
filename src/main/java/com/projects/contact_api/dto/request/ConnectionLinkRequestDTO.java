package com.projects.contact_api.dto.request;

import com.projects.contact_api.model.ConfigType;
import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.ExpirationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ConnectionLinkRequestDTO {
    private ExpirationType expirationType;
    private ContactConfig contactConfig;
}

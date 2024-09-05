package com.projects.contact_api.dto.request;

import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.ExpirationType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UploadImageRequestDTO {
    private MultipartFile image;
}

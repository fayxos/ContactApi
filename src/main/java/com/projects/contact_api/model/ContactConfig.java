package com.projects.contact_api.model;

import com.projects.contact_api.helper.ContactOptionListConverter;
import com.projects.contact_api.helper.IntegerListConverter;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_config")
public class ContactConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private ConfigType configType;
    private String configName;

    @Convert(converter = ContactOptionListConverter.class)
    private ArrayList<ContactOptions> contactOptions;

    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> images;
    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> phoneNumbers;
    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> emails;
    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> descriptions;
    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> websites;
    @Convert(converter = IntegerListConverter.class)
    private ArrayList<Integer> socials;

    public void updateData(ContactConfig configData) {
        if(configData.getConfigName() != null && !configData.getConfigName().isEmpty()) setConfigName(configData.getConfigName());
        if(configData.getContactOptions() != null && !configData.getContactOptions().isEmpty()) setContactOptions(configData.getContactOptions());
        if(configData.getEmails() != null) setEmails(configData.getEmails());
        if(configData.getDescriptions() != null) setDescriptions(configData.getDescriptions());
        if(configData.getImages() != null) setImages(configData.getImages());
        if(configData.getSocials() != null) setSocials(configData.getSocials());
        if(configData.getPhoneNumbers() != null) setPhoneNumbers(configData.getPhoneNumbers());
        if(configData.getWebsites() != null) setWebsites(configData.getWebsites());
    }
}

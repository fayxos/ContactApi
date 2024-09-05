package com.projects.contact_api.model;

import com.projects.contact_api.helper.HashMapConverter;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_info")
public class ContactInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    private String title;
    private String firstname;
    private String secondName;
    private String lastname;
    private String nickname;

    private String gender;
    private String pronouns;
    private String relationshipStatus;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> imageUrls;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> phoneNumbers;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> emails;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> descriptions;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> websites;

    @Convert(converter = HashMapConverter.class)
    private Map<String, List<String>> socials;

    private Integer birthYear;
    private Integer birthMonth;
    private Integer birthDay;

    private String addressCountry;
    private String addressPostalCode;
    private String addressCity;
    private String addressStreet;
    private String addressHouseNumber;
    private String addressAddition;

    private String workTitle;
    private String workDescription;
    private String workCompany;

    public void updateData(ContactInfo contactInfo) {
        if (contactInfo.getTitle() != null) setTitle(contactInfo.getTitle());
        if (contactInfo.getFirstname() != null && !contactInfo.getFirstname().isEmpty()) setFirstname(contactInfo.getFirstname());
        if (contactInfo.getSecondName() != null) setSecondName(contactInfo.getSecondName());
        if (contactInfo.getLastname() != null && !contactInfo.getLastname().isEmpty()) setLastname(contactInfo.getLastname());
        if (contactInfo.getNickname() != null) setNickname(contactInfo.getNickname());

        if (contactInfo.getGender() != null) setGender(contactInfo.getGender());
        if (contactInfo.getPronouns() != null) setPronouns(contactInfo.getPronouns());
        if (contactInfo.getRelationshipStatus() != null) setRelationshipStatus(contactInfo.getRelationshipStatus());

        if (contactInfo.getImageUrls() != null) setImageUrls(contactInfo.getImageUrls());
        if (contactInfo.getPhoneNumbers() != null) setPhoneNumbers(contactInfo.getPhoneNumbers());
        if (contactInfo.getEmails() != null) setEmails(contactInfo.getEmails());
        if (contactInfo.getDescriptions() != null) setDescriptions(contactInfo.getDescriptions());
        if (contactInfo.getWebsites() != null) setWebsites(contactInfo.getWebsites());
        if (contactInfo.getSocials() != null) setSocials(contactInfo.getSocials());

        if (contactInfo.getBirthYear() != null) setBirthYear(contactInfo.getBirthYear());
        if (contactInfo.getBirthMonth() != null) setBirthMonth(contactInfo.getBirthMonth());
        if (contactInfo.getBirthDay() != null) setBirthDay(contactInfo.getBirthDay());

        if (contactInfo.getAddressCountry() != null) setAddressCountry(contactInfo.getAddressCountry());
        if (contactInfo.getAddressPostalCode() != null) setAddressPostalCode(contactInfo.getAddressPostalCode());
        if (contactInfo.getAddressCity() != null) setAddressCity(contactInfo.getAddressCity());
        if (contactInfo.getAddressStreet() != null) setAddressStreet(contactInfo.getAddressStreet());
        if (contactInfo.getAddressHouseNumber() != null) setAddressHouseNumber(contactInfo.getAddressHouseNumber());
        if (contactInfo.getAddressAddition() != null) setAddressAddition(contactInfo.getAddressAddition());

        if (contactInfo.getWorkTitle() != null) setWorkTitle(contactInfo.getWorkTitle());
        if (contactInfo.getWorkDescription() != null) setWorkDescription(contactInfo.getWorkDescription());
        if (contactInfo.getWorkCompany() != null) setWorkCompany(contactInfo.getWorkCompany());
    }

}

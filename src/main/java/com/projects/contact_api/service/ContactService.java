package com.projects.contact_api.service;

import com.projects.contact_api.dao.ConnectionDAO;
import com.projects.contact_api.dao.ContactConfigDAO;
import com.projects.contact_api.dao.ContactInfoDAO;
import com.projects.contact_api.dao.UserDAO;
import com.projects.contact_api.helper.FileUploadUtil;
import com.projects.contact_api.model.Connection;
import com.projects.contact_api.model.ContactConfig;
import com.projects.contact_api.model.ContactInfo;
import com.projects.contact_api.model.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.core.io.InputStreamResource;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Log
public class ContactService {

    private final ContactConfigDAO contactConfigDAO;
    private final ContactInfoDAO contactInfoDAO;
    private final ConnectionDAO connectionDAO;
    private final UserDAO userDAO;

    // ContactInfo

    public ContactInfo getInfoByUserId(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        return contactInfoDAO.findByUser(user).orElseThrow();
    }

    public InputStreamResource getImage(Integer userId, String imageName) throws FileNotFoundException {
        String imagePath = "images/" + userId;

        String filePath = new File(imagePath, imageName).getAbsolutePath();

        // Create a File object
        File imageFile = new File(filePath);

        // Check if the file exists
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Image not found: " + filePath);
        }

        // Convert the file into an InputStreamResource
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        return new InputStreamResource(fileInputStream);
    }

    public ContactInfo createInfo(Integer userId, ContactInfo info) {
        User user = userDAO.findById(userId).orElseThrow();
        info.setUser(user);
        return contactInfoDAO.save(info);
    }

    public ContactInfo updateInfo(Integer userId, ContactInfo infoData) {
        User user = userDAO.findById(userId).orElseThrow();
        ContactInfo info = contactInfoDAO.findByUser(user).orElseThrow();
        info.updateData(infoData);
        return contactInfoDAO.save(info);
    }

    public ContactInfo uploadImage(Integer userId, MultipartFile multipartFile) throws IOException {
        if(multipartFile.isEmpty()) throw new RuntimeException("File is empty");

        User user = userDAO.findById(userId).orElseThrow();
        ContactInfo contactInfo = contactInfoDAO.findByUser(user).orElseThrow();

        // Only allow PNGs
        if(!"image/png".equalsIgnoreCase(multipartFile.getContentType())) throw new RuntimeException("Wrong file format");

        String fileName = StringUtils.cleanPath(Objects.requireNonNull(multipartFile.getOriginalFilename()));
        String uploadDir = "images/" + userId;

        Map<String, List<String>> imageNames = contactInfo.getImageUrls();
        if(imageNames == null) {
            imageNames = new HashMap<String, List<String>>();
        }

        if(imageNames.values().stream().map(List::getLast).toList().contains(fileName)) {
            throw new RuntimeException("There is already an image with the same name");
        }

        FileUploadUtil.saveFile(uploadDir, fileName, multipartFile);

        String imageKey;
        if(imageNames.values().isEmpty()) {
            imageKey = "0";
        } else {
            imageKey = (imageNames.keySet().stream().map(Integer::parseInt).sorted().toList().getLast() + 1) + "";
        }
        imageNames.put(imageKey, List.of("", fileName));
        contactInfo.setImageUrls(imageNames);
        return contactInfoDAO.save(contactInfo);
    }

    // Get Info From Connection With Config
    public ContactInfo getInfoFromConnection(Integer userId, Integer connectionId) throws IllegalAccessException {
        Connection connection = connectionDAO.findById(connectionId).orElseThrow();

        if(!Objects.equals(connection.getUser1().getId(), userId) && !Objects.equals(connection.getUser2().getId(), userId)) throw new AccessDeniedException("User has no access to this connection");

        User otherUser;
        ContactConfig config;
        if(Objects.equals(connection.getUser1().getId(), userId)) {
            otherUser = connection.getUser2();
            config = connection.getConfig2();
        }
        else {
            otherUser = connection.getUser1();
            config = connection.getConfig1();
        }


        ContactInfo info = contactInfoDAO.findByUser(otherUser).orElseThrow();

        Field[] fields = info.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if(config.getContactOptions().stream().noneMatch(o -> o.getDisplayName().equals(field.getName()))) {
                field.set(info, null);
            } else if(Map.class.isAssignableFrom(field.getType())) {
                if(field.get(info) == null) continue;
                Map<String, List<String>> infoList = (Map<String, List<String>>) field.get(info);

                Field[] configFields = config.getClass().getDeclaredFields();
                for (Field configField : configFields) {
                    configField.setAccessible(true);
                    if(configField.getName().equals(field.getName()) && configField.get(config) != null) {
                        List<Integer> configList = (List<Integer>)configField.get(config);
                        System.out.println(configList);
                        infoList = infoList.entrySet().stream().filter(e -> configList.contains(Integer.parseInt(e.getKey()))).collect(Collectors.toMap(e->e.getKey(), e->e.getValue()));
                        break;
                    }
                }

                field.set(info, infoList);
            }

                /*
            System.out.printf("%s %s %s %s%n",
                    Modifier.toString(field.getModifiers()),
                    field.getType().getSimpleName(),
                    field.getName(),
                    field.get(info).toString()
            );
            */
        }

        return info;
    }

    public InputStreamResource getImageFromConnection(Integer userId, Integer connectionId) throws IOException {
        Connection connection = connectionDAO.findById(connectionId).orElseThrow();

        if(!Objects.equals(connection.getUser1().getId(), userId) && !Objects.equals(connection.getUser2().getId(), userId)) throw new AccessDeniedException("User has no access to this connection");

        User otherUser;
        ContactConfig config;
        if(Objects.equals(connection.getUser1().getId(), userId)) {
            otherUser = connection.getUser2();
            config = connection.getConfig2();
        }
        else {
            otherUser = connection.getUser1();
            config = connection.getConfig1();
        }

        if(config.getImages().isEmpty()) return null;

        ContactInfo info = contactInfoDAO.findByUser(otherUser).orElseThrow();

        String imageName = info.getImageUrls().get(config.getImages().getFirst().toString()).getLast();
        String imagePath = "images/" + otherUser.getId();

        String filePath = new File(imagePath, imageName).getAbsolutePath();

        // Create a File object
        File imageFile = new File(filePath);

        // Check if the file exists
        if (!imageFile.exists()) {
            throw new FileNotFoundException("Image not found: " + filePath);
        }

        // Convert the file into an InputStreamResource
        FileInputStream fileInputStream = new FileInputStream(imageFile);
        return new InputStreamResource(fileInputStream);
    }

    // ContactConfig

    public List<ContactConfig> getConfigsByUserId(Integer userId) {
        User user = userDAO.findById(userId).orElseThrow();
        return contactConfigDAO.findByUser(user);
    }

    public ContactConfig getConfigById(Integer configId) {
        return contactConfigDAO.findById(configId).orElseThrow();
    }

    public ContactConfig createConfig(Integer userId, ContactConfig config) {
        User user = userDAO.findById(userId).orElseThrow();
        config.setUser(user);
        return contactConfigDAO.save(config);
    }

    public ContactConfig updateConfig(Integer configId, ContactConfig configData) {
        ContactConfig contactConfig = contactConfigDAO.findById(configId).orElseThrow();
        contactConfig.updateData(configData);
        return contactConfigDAO.save(contactConfig);
    }

    public void deleteConfig(Integer configId) {
        ContactConfig config = contactConfigDAO.findById(configId).orElseThrow();
        contactConfigDAO.delete(config);
    }
}

package com.mobile.Projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.Projet.model.User;
import com.mobile.Projet.repository.UserRepository;
import com.mobile.Projet.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
@RequestMapping("/user")
public class UserController {

    private static final String UPLOAD_DIR = "C:\\Users\\Administrator\\Desktop\\frontendMobile-asma\\src\\assets\\profileImages\\";


    @Autowired
    UserRepository userRepository;

    @Autowired
    EmailService emailService;

    private String saveFile(MultipartFile file) throws IOException {
        Path uploadPath = Paths.get(UPLOAD_DIR);

        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        String filename = System.currentTimeMillis() + "_" + file.getOriginalFilename();
        Path filePath = uploadPath.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        return ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/product/download/")
                .path(filename)
                .toUriString();
    }


    @GetMapping("/download/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename) throws MalformedURLException {
        Path filePath = Paths.get(UPLOAD_DIR + filename);
        Resource resource = new UrlResource(filePath.toUri());

        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }


    @PutMapping(value = "/register", consumes = {"multipart/form-data"})
    public ResponseEntity<Map<String, String>> signUp(@RequestParam("user") MultipartFile userFile, @RequestParam("file") MultipartFile file) throws IOException {
        // Convert MultipartFile to String
        String userStr = new BufferedReader(new InputStreamReader(userFile.getInputStream(), StandardCharsets.UTF_8))
                .lines()
                .collect(Collectors.joining("\n"));

        // Deserialize JSON string to User object
        ObjectMapper objectMapper = new ObjectMapper();
        User user = objectMapper.readValue(userStr, User.class);

        // Check if user already exists in the database to prevent duplicates
        if (user != null && !userRepository.existsByEmail(user.getEmail())) {
            try {
                // Handling file saving and retrieving URL for user picture
                String fileUrl = saveFile(file);
                user.setUserPicture(fileUrl);

                // Building user object for persistence
                User savedUser = User.builder()
                        .userCity(user.getUserCity())
                        .userDelegation(user.getUserDelegation())
                        .userStreet(user.getUserStreet())
                        .email(user.getEmail())
                        .userName(user.getUserName())
                        .phoneNumber(user.getPhoneNumber())
                        .userPicture(user.getUserPicture())
                        .password(hashPassword(user.getPassword()))
                        .build();

                // Saving user to repository
                userRepository.save(savedUser);

                // Prepare and send successful response
                Map<String, String> response = new HashMap<>();
                response.put("message", "User registered successfully with Image");
                return ResponseEntity.ok(response);

            } catch (Exception e) {
                // Handle file saving or other internal errors
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("message", "Error registering user: " + e.getMessage());
                return ResponseEntity.internalServerError().body(errorResponse);
            }
        } else {
            // Prepare error response if user data is invalid or user already exists
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "User already exists or data is incomplete");
            return ResponseEntity.badRequest().body(errorResponse);
        }
    }





    @GetMapping("/getAllUsers")
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }

    private String hashPassword(String plainPassword) {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        return passwordEncoder.encode(plainPassword);
    }

//    @PostMapping("/register")
//    public void signUp(@RequestBody User user){
//        if(user != null){
//            if(!userRepository.existsByEmail(user.getEmail())){
//                User user1 =User.builder()
//                        .userCity(user.getUserCity())
//                        .userDelegation(user.getUserDelegation())
//                        .userStreet(user.getUserStreet())
//                        .email(user.getEmail())
//                        .userName(user.getUserName())
//                        .phoneNumber(user.getPhoneNumber())
//                        .userOrders(user.getUserOrders())
//                        .userProducts(user.getUserProducts())
//                        .userPicture(user.getUserPicture())
//                        .password(hashPassword(user.getPassword()))
//                        .build();
//                userRepository.save(user1);
//
//
//            }
//        }
//    }

    @PostMapping ("/signIn")
    public boolean logIn(@RequestBody User user){

        User user1=userRepository.findByEmailIgnoreCase(user.getEmail());

        if(user1 != null){
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            return passwordEncoder.matches(user.getPassword(), user1.getPassword());
        }else {
            return false;
        }
    }

    @PostMapping("/forgotPassword")
    public void forgotPassword(@RequestBody User user){
        if(user.getEmail() != null){
            emailService.sendEmail(user.getEmail(),"Reset your Password","/resetPassword/"+user.getEmail());
        }
    }

    @PostMapping("/resetPassword/{email}")
    public void resetPassword(@PathVariable String email,@RequestBody User user){
        if(user!= null){
            if(userRepository.existsByEmail(email)){
                User user1=userRepository.findByEmailIgnoreCase(email);
                user1.setPassword(hashPassword(user.getPassword()));
                userRepository.save(user1);
            }

        }
    }

    @GetMapping("/userProfile/{email}")
    public User  userProfile(@PathVariable String email){
        if(userRepository.existsByEmail(email)){
            return userRepository.findByEmailIgnoreCase(email);
        }
        return null;
    }






}




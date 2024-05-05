package com.mobile.Projet.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mobile.Projet.model.Product;
import com.mobile.Projet.model.User;
import com.mobile.Projet.repository.ProductRepository;
import com.mobile.Projet.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/product")
public class ProductController {

    private static final String UPLOAD_DIR = "C:\\Users\\Administrator\\Desktop\\frontendMobile-asma\\src\\assets\\";

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

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

    @GetMapping("/getProducts")
    public List<Product> getAllProduct() {
        return productRepo.findAll();
    }

    @PutMapping(value = "/addProduct", consumes = {"multipart/form-data"})
    public ResponseEntity<String> addProduct(
            @RequestParam("product") String productJson,
            @RequestParam("image") MultipartFile imageFile) {
        try {
            // Convert JSON string to Product object
            ObjectMapper objectMapper = new ObjectMapper();
            Product product = objectMapper.readValue(productJson, Product.class);

            // Retrieve user based on the email provided in the product
            String userEmail = product.getUser().getEmail();
            User user = userRepo.findByEmailIgnoreCase(userEmail);

            // Associate the retrieved user with the product
            product.setUser(user);

            // Save the product with the associated user
            String imageUrl = saveFile(imageFile);
            product.setProductPicture(imageUrl);
            productRepo.save(product);

            return ResponseEntity.ok().body("{\"message\": \"Product added successfully with image\"}");
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to add product: " + ex.getMessage());
        }
    }


    @DeleteMapping("/deleteProduct/{id}")
    public void deleteProduct(@PathVariable long id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id);
        }
    }

    @PutMapping("/updateProduct/{id}")
    public void updateProduct(@PathVariable long id, @RequestBody Product product) {
        Product product1 = productRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Product not found with id: " + id));

        if (product.getProductPicture() != null) {
            product1.setProductPicture(product.getProductPicture());
        }
        if (product.getCategory() != null) {
            product1.setCategory(product.getCategory());
        }
        if (product.getPrice() != 0) {
            product1.setPrice(product.getPrice());
        }
        if (product.getDescription() != null) {
            product1.setDescription(product.getDescription());
        }
        if (product.getState() != null) {
            product1.setState(product.getState());
        }
        // Uncomment and use if necessary
        // if(product.getReservationMaxDuration() != null) { product1.setReservationMaxDuration(product.getReservationMaxDuration()); }

        productRepo.save(product1);
    }
}

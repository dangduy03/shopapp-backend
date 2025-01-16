package com.example.shopapp.services;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.shopapp.models.Product;
import com.example.shopapp.models.ProductImage;
import com.example.shopapp.repositorys.ProductImageRepository;
import com.example.shopapp.repositorys.ProductRepository;

@Service
@RequiredArgsConstructor
public class MinioService {

    @Autowired
    private MinioClient minioClient;
    
    @Autowired
    private ProductRepository productReopsitory;
    
    @Autowired
    private ProductImageRepository productImageRepository;

    @Value("${minio.bucket-name}")
    private String bucketName;

    public String uploadFile(MultipartFile file) throws Exception {
        try {
            String fileName = file.getOriginalFilename();
            
            minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucketName)
                            .object(fileName)
                            .stream(file.getInputStream(), file.getSize(), -1)
                            .contentType(file.getContentType())
                            .build()
            );

            return "File uploaded successfully: " + fileName;
        } catch (Exception e) {
            throw new Exception("Error while uploading file: " + e.getMessage());
        }
    }
    
    public List<String> uploadFiles(List<MultipartFile> files,Long id) throws Exception {
    	long MAX_FILE_SIZE = 10 * 1024 * 1024;
        List<String> uploadedFileNames = new ArrayList<>();
        
        Product existingProduct = productReopsitory.findById(id).orElse(null);

        for (MultipartFile file : files) {
            try {
                if (file.getSize() > MAX_FILE_SIZE) {
                    throw new Exception("File " + file.getOriginalFilename() + " vượt quá dung lượng tối đa cho phép là 5MB.");
                }
                
                String fileName = file.getOriginalFilename();
                minioClient.putObject(
                        PutObjectArgs.builder()
                                .bucket(bucketName)
                                .object(fileName)
                                .stream(file.getInputStream(), file.getSize(), -1)
                                .contentType(file.getContentType())
                                .build()
                );
                
                ProductImage productImage = ProductImage.builder()
                		.product(existingProduct)
                		.imageUrl(bucketName + "/" + fileName)
                		.build();
                productImageRepository.save(productImage);
                uploadedFileNames.add(fileName);
            } catch (Exception e) {
                throw new Exception("Lỗi khi upload file: " + file.getOriginalFilename() + ". " + e.getMessage());
            }
        }

        return uploadedFileNames;
    }
}

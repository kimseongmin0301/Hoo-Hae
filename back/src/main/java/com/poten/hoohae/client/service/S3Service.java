package com.poten.hoohae.client.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.poten.hoohae.client.config.S3Config;
import com.poten.hoohae.client.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

@Service
public class S3Service {

    private final AmazonS3 s3;
    private final String bucketName = "hoohae";
    private final String endpointUrl = "https://kr.object.ncloudstorage.com"; // S3 엔드포인트 URL

    @Autowired
    public S3Service(S3Config s3Config) {
        this.s3 = s3Config.getS3();
    }

    public void setObjectACL(String bucketName,String objectName){
        try {
            // 현재 ACL의 정보를 가져온다
            AccessControlList accessControlList = s3.getObjectAcl(bucketName, objectName);

            // 읽기권한을 모든 유저에게 줌
            accessControlList.grantPermission(GroupGrantee.AllUsers, Permission.Read);

            //설정을 버킷에 적용시킨다.
            s3.setObjectAcl(bucketName, objectName, accessControlList);
        } catch (AmazonS3Exception e) {
            e.printStackTrace();
        } catch(SdkClientException e) {
            e.printStackTrace();
        }
    }

    public List<Map<String, String>> uploadFiles(List<MultipartFile> files) throws IOException {
        List<Map<String, String>> fileUrls = new ArrayList<>();
        for(MultipartFile file : files){
            System.out.println(file.isEmpty());
            System.out.println(file.getOriginalFilename());
            System.out.println(file.getSize());
            System.out.println(file.getInputStream());
            System.out.println(file.getName());
            System.out.println(file.getContentType());
            System.out.println(file.getResource());
        }

        if (files.isEmpty()) {
            return fileUrls;
        }

        for (MultipartFile file : files) {
            Map<String, String> map = new HashMap<>();
            if (file.isEmpty()) {
                System.out.println("Skipping empty file.");
                continue;
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = String.valueOf(UUID.randomUUID());

            String fileExtension = "";
            if (originalFileName != null) {
                String[] parts = originalFileName.split("\\.");
                if (parts.length > 1) {
                    fileExtension = parts[parts.length - 1].toLowerCase();
                }
            }

            // 로그 추가
            System.out.println("Processing file: " + originalFileName + " with extension: " + fileExtension);

            if (isImageExtension(fileExtension)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());

                // 파일 업로드 시 로그 추가
                System.out.println("Uploading to S3: " + fileName);

                s3.putObject(bucketName, fileName, file.getInputStream(), metadata);
                setObjectACL(bucketName, fileName);

                String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
                map.put("link", fileUrl);
                map.put("name", fileName);
                map.put("orgName", file.getOriginalFilename());

                fileUrls.add(map);

                // 파일이 정상적으로 리스트에 추가되었는지 로그로 확인
                System.out.println("Added file URL: " + fileUrl);
            } else {
                System.out.println("Skipping non-image file: " + originalFileName);
            }
        }

        return fileUrls;
    }

    private boolean isImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }
}

package com.poten.hoohae.client.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.poten.hoohae.client.config.S3Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

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

    public List<String> uploadFiles(List<MultipartFile> files) throws IOException {
        List<String> fileUrls = new ArrayList<>(); // 업로드된 파일 URL을 저장할 리스트
        if (files == null || files.isEmpty()) {
            return fileUrls; // 에러를 발생시키지 않고 빈 리스트 반환
        }

        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                continue; // 빈 파일은 건너뜁니다.
            }

            String originalFileName = file.getOriginalFilename();
            String fileName = String.valueOf(UUID.randomUUID()); // UUID로 파일 이름 생성

            String fileExtension = "";
            if (originalFileName != null) {
                String[] parts = originalFileName.split("\\."); // '.'을 기준으로 분리
                if (parts.length > 1) {
                    fileExtension = parts[parts.length - 1].toLowerCase(); // 마지막 요소를 가져와서 소문자로 변환
                }
            }

            if (isImageExtension(fileExtension)) {
                ObjectMetadata metadata = new ObjectMetadata();
                metadata.setContentLength(file.getSize());

                // S3에 파일 업로드
                s3.putObject(bucketName, fileName, file.getInputStream(), metadata);
                setObjectACL(bucketName, fileName);

                // 파일 URL 생성
                String fileUrl = endpointUrl + "/" + bucketName + "/" + fileName;
                fileUrls.add(fileUrl);
            }
        }

        return fileUrls;
    }

    private boolean isImageExtension(String extension) {
        return extension.equals("jpg") || extension.equals("jpeg") || extension.equals("png");
    }
}

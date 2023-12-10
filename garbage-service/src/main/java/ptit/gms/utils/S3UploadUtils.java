package ptit.gms.utils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ptit.gms.config.ConfigValue;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ObjectCannedACL;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

@Service
public class S3UploadUtils {
    @Autowired
    ConfigValue configValue;

    public String uploadImageFile(String endpoint, MultipartFile multipartFile) throws IOException {
        AwsCredentials credentials = AwsBasicCredentials.create(configValue.getS3AccessKey(), configValue.getS3SecretKey());
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        File file = convertMultiPartToFile(multipartFile);

        String fileName = generateFileName(multipartFile);
        String key = endpoint + '/' + fileName;

        S3Client client = S3Client.builder().
                region(Region.of(configValue.getS3Region())).
                credentialsProvider(provider).
                build();

        PutObjectRequest request = PutObjectRequest.builder().
                bucket(configValue.getS3Bucket()).
                key(key).
                acl(ObjectCannedACL.PUBLIC_READ).
                build();

        client.putObject(request, RequestBody.fromFile(file));
        return configValue.getS3Url() + '/' + key;
    }

    private File convertMultiPartToFile(MultipartFile file) throws IOException {
        File convFile = new File(file.getOriginalFilename());
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }

    private String generateFileName(MultipartFile multiPart) {
        return new Date().getTime() + "-" + multiPart.getOriginalFilename().replace(" ", "_");
    }
}

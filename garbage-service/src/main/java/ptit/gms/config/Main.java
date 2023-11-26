package ptit.gms.config;

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

public class Main {
    public static void main(String[] args) {
        String bucketName = "garbage-system";
        String filename = "369327971_3499971620290509_4486222460399197155_n.jpg";
        String filePath = "E:/anh/25-08/" + filename;

        String key = "bins/" + filename;

        AwsCredentials credentials = AwsBasicCredentials.create(
                "AKIARA4HHNICUVJVYW4E",
                "sVt5Y4I93/AHWbfFm7ZZ5aNbYseWC/alOtmmIwKz"
        );
        AwsCredentialsProvider provider = StaticCredentialsProvider.create(credentials);

        S3Client client = S3Client.builder().region(Region.AP_SOUTHEAST_2).credentialsProvider(provider).build();

        PutObjectRequest request = PutObjectRequest.builder().
                bucket(bucketName).
                key(key).
                acl(ObjectCannedACL.PUBLIC_READ).
                build();

        client.putObject(request, RequestBody.fromFile(new File(filePath)));

    }
}

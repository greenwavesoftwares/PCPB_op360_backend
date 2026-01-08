package com.random.number.service;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import com.random.number.models.FileTypeInfo;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.util.*;
import java.util.regex.*;

@Service
public class SQLExecutionService {

    @Autowired
    private MinioClient minioClient;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private final String bucket = "procurement";

    public String processSql(String sqlQuery) throws Exception {

        Pattern pattern = Pattern.compile("0x[0-9A-Fa-f]+");
        Matcher matcher = pattern.matcher(sqlQuery);

        List<String> binaries = new ArrayList<>();

        while (matcher.find()) {
            binaries.add(matcher.group());
        }

        List<String> keys = new ArrayList<>();

        for (String hex : binaries) {

            String hexData = hex.substring(2);
            //byte[] bytes = new BigInteger(hexData, 16).toByteArray();
            byte[] bytes = hexStringToByteArray(hexData);

            FileTypeInfo info = FileTypeUtil.detect(bytes);

            String objectKey = UUID.randomUUID() + info.extension;

            try (InputStream input = new ByteArrayInputStream(bytes)) {
                minioClient.putObject(
                    PutObjectArgs.builder()
                            .bucket(bucket)
                            .object(objectKey)
                            .stream(input, bytes.length, -1)
                            .contentType(info.mimeType)
                            .build()
                );
            }

            keys.add(objectKey);
        }

        for (int i = 0; i < binaries.size(); i++) {
            sqlQuery = sqlQuery.replace(binaries.get(i), "'" + keys.get(i) + "'");
        }

        jdbcTemplate.execute(sqlQuery);

        return "SQL executed successfully. Uploaded files: " + keys;
    }
    private byte[] hexStringToByteArray(String hex) {
        int len = hex.length();
        byte[] data = new byte[len / 2];

        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(hex.charAt(i), 16) << 4)
                    + Character.digit(hex.charAt(i + 1), 16));
        }
        return data;
    }

}

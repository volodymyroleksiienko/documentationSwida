package com.swida.documetation.utils.other;

import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

public class GenerateResponseForExport {
    public ResponseEntity<Resource> generate(String filePath, String startDate, String endDate) throws FileNotFoundException {
        File file = new File(filePath);
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
        HttpHeaders headers = new HttpHeaders();
        System.out.println(startDate);
        System.out.println(endDate);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getName());
        System.out.println();
        System.out.println(file.getName());
        System.out.println();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");

        return ResponseEntity.ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }
}

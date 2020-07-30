package com.swida.documetation.controllers;

import com.swida.documetation.data.entity.UserCompany;
import com.swida.documetation.data.entity.storages.TreeStorage;
import com.swida.documetation.data.enums.Roles;
import com.swida.documetation.data.service.UserCompanyService;
import com.swida.documetation.data.service.storages.TreeStorageService;
import com.swida.documetation.utils.xlsParsers.ParseTreeStorageToXLS;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Controller
public class SecurityController {
    @Autowired
    UserCompanyService userCompanyService;
    @Autowired
    TreeStorageService treeStorageService;


    @PostMapping("/enterRequest")
    public String loginPost(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        int userID = userCompanyService.findByUsername(auth.getName()).getId();
        return "redirect:/fabric/index-"+userID;
    }

//    @GetMapping("/save")
//    public ResponseEntity<Resource> save() throws FileNotFoundException {
//        ParseTreeStorageToXLS parser = new ParseTreeStorageToXLS(treeStorageService.findAll());
//        String filePath = parser.parse();
//        File file = new File(filePath);
//        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));
//        HttpHeaders headers = new HttpHeaders();
//        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename="+file.getName());
//        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
//        headers.add("Pragma", "no-cache");
//        headers.add("Expires", "0");
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentLength(file.length())
//                .contentType(MediaType.APPLICATION_OCTET_STREAM)
//                .body(resource);
//    }
}

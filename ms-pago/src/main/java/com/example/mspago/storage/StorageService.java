package com.example.mspago.storage;

import org.springframework.web.multipart.MultipartFile;
import java.util.List;


public interface StorageService {
    String store(MultipartFile file);
    List<String> loadAll();

}
package com.project.utility;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileStorageUtil {

    private static final String BASE_FOLDER = "uploads/";

    public String saveFile(String userName, String uniqueFileName, byte[] fileBytes) throws IOException {
        // Create user-specific folder
        String userFolder = BASE_FOLDER + userName;
        File directory = new File(userFolder);
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create file in user folder with unique name
        File file = new File(userFolder + "/" + uniqueFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        }

        return file.getAbsolutePath(); // Return the full path of the saved file
    }
}
package com.project.utility;

import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Component
public class FileStorageUtil {

    private static final String BASE_FOLDER;

    // Static block to initialize BASE_FOLDER relative to the JAR file location
    static {
        String jarDir = System.getProperty("user.dir"); // Gets the directory where the JAR is run
        BASE_FOLDER = jarDir + File.separator + "uploads"; // Creates "uploads" folder in the same location
    }

    public String saveFile(String userName, String uniqueFileName, byte[] fileBytes) throws IOException {
        // Create the base "uploads" folder if it doesn't exist
        File baseDirectory = new File(BASE_FOLDER);
        if (!baseDirectory.exists()) {
            if (!baseDirectory.mkdirs()) {
                throw new IOException("Failed to create base uploads directory: " + BASE_FOLDER);
            }
        }

        // Create the user-specific folder inside the base folder
        String userFolder = BASE_FOLDER + File.separator + userName;
        File userDirectory = new File(userFolder);
        if (!userDirectory.exists()) {
            if (!userDirectory.mkdirs()) {
                throw new IOException("Failed to create user directory: " + userFolder);
            }
        }

        // Create the complete file path inside the user folder
        File file = new File(userFolder + File.separator + uniqueFileName);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(fileBytes);
        }

        return file.getAbsolutePath(); // Return the full path of the saved file
    }
}
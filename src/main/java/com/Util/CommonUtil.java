package com.Util;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;

public class CommonUtil {
    public static String[] getFileNames(String directoryPath) throws IOException {
        List<String> fileNames = new ArrayList<>();

        // Visit all files in the directory and add their names to the list
        Files.walkFileTree(Paths.get(directoryPath), EnumSet.noneOf(FileVisitOption.class), Integer.MAX_VALUE, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                fileNames.add(file.getFileName().toString().split("\\.")[0]);
                return FileVisitResult.CONTINUE;
            }
        });

        // Convert the list to an array
        return fileNames.toArray(new String[0]);
    }
}

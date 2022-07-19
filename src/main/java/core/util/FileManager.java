package core.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Slf4j
public class FileManager {

    public File createFile(String fileName) {
        if (fileName == null) { return null; }

        return new File(fileName);
    }

    public boolean isExist(String fileName) {
        if (fileName == null) { return false; }

        File file = new File(fileName);
        return file.exists();
    }

    public boolean mkdirs(String fileName) {
        if (fileName == null) { return false; }

        File file = new File(fileName);
        return file.mkdirs();
    }

    public boolean writeString(String fileName, String data, boolean isAppend) {
        if (fileName == null) { return false; }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, isAppend))) {
            bufferedWriter.write(data);
            return true;
        } catch (Exception e) {
            log.warn("[FileManager] Fail to write the file. (fileName={})", fileName, e);
            return false;
        }
    }

    public String readAllToString(String fileName) {
        if (fileName == null) { return null; }

        try {
            Path path = Paths.get(fileName);
            return Files.readString(path);
        } catch (Exception e) {
            log.warn("[FileManager] Fail to read the file. (fileName={})", fileName);
            return null;
        }
    }

    public int getTotalLineNumber(String fileName) {
        int lineCount = 0;
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            while (reader.readLine() != null) {
                lineCount++;
            }
            return lineCount;
        } catch (Exception e) {
            log.warn("[FileManager] getTotalLineNumber.Exception", e);
            return 0;
        }
    }

    public String getLineByNumber(int lineNumber, String fileName) {
        if (lineNumber <= 0) { return null; }

        try (Stream<String> fileStream = Files.lines(Path.of(fileName))) {
            return fileStream.skip(lineNumber - 1).findFirst().get();
        } catch (Exception e) {
            return null;
        }
    }

    public void deleteFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            log.warn("[FileManager] Fail to delete the file. File is not exist. (path={})", path);
            return;
        }

        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                FileUtils.forceDelete(file);
            }
            log.debug("[FileManager] Success to delete the file. (path={})", path);
        } catch (Exception e) {
            log.warn("[FileManager] Fail to delete the file. (path={})", path, e);
        }
    }

    public void deleteFile(File file) {
        if (file == null) { return; }

        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            } else {
                FileUtils.forceDelete(file);
            }
            log.debug("[FileManager] Success to delete the file. (path={})", file.getAbsolutePath());
        } catch (Exception e) {
            log.warn("[FileManager] Fail to delete the file. (path={})", file.getAbsolutePath(), e);
        }
    }

}

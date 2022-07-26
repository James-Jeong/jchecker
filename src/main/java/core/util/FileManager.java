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
        if (isEmptyString(fileName)) { return null; }

        return new File(fileName);
    }

    public boolean isExist(String fileName) {
        if (isEmptyString((fileName))) { return false; }

        File file = new File(fileName);
        return file.exists();
    }

    public boolean mkdirs(String fileName) {
        if (isEmptyString(fileName)) { return false; }

        File file = new File(fileName);
        return file.mkdirs();
    }

    public boolean writeString(String fileName, String data, boolean isAppend) {
        if (isEmptyString(fileName)) { return false; }

        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(fileName, isAppend))) {
            bufferedWriter.write(data);
            return true;
        } catch (Exception e) {
            log.warn("[FileManager] Fail to write the file. (fileName={})", fileName, e);
            return false;
        }
    }

    public String readAllToString(String fileName) {
        if (isEmptyString(fileName)) { return null; }

        try {
            Path path = Paths.get(fileName);

            StringBuilder result = new StringBuilder();
            for (String line : Files.readAllLines(path)) {
                result.append(line);
            }

            return result.toString();
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

        File file = new File(fileName);
        Path path = file.toPath();

        try (Stream<String> fileStream = Files.lines(path)) {
            return fileStream.skip(lineNumber - 1).findFirst().orElse(null);
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

    public boolean trimFile(String fileName) {
        String fileContent = readAllToString(fileName);
        if (isEmptyString(fileContent)) { return false; }

        fileContent = trimLine(fileContent);
        return writeString(fileName, fileContent, false);
    }

    public String trimLine(String line) {
        line = line.replaceAll("(^M)", "\n").trim();
        return line.replaceAll("(\r\n|\r|\n\r)", "\n").trim();
    }

    public boolean isEmptyString(String line) {
        return (line == null) || line.isEmpty();
    }

}

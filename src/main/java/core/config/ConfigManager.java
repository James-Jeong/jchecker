package core.config;

import lombok.Data;
import org.ini4j.Ini;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

/**
 * @class public class UserConfig
 * @brief UserConfig Class
 */
@Data
public class ConfigManager {

    ////////////////////////////////////////////////////////////
    private static final Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    private Ini ini = null;
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // CONSTANT
    private static final String CONSTANT_PRINT_SUCCESS_LOG_FORMAT = "Load [{}] config...(OK)";
    private static final String CONSTANT_PRINT_FAIL_LOG_FORMAT_1 = "Fail to load [{}-{}].";
    private static final String CONSTANT_PRINT_FAIL_LOG_FORMAT_2 = "Fail to load [{}-{}]. ({})";
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // Section String
    public static final String SECTION_COMMON = "COMMON"; // COMMON Section 이름
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // Field String
    // COMMON
    public static final String FIELD_ID = "ID";
    public static final String FIELD_THREAD_POOL_SIZE = "THREAD_POOL_SIZE";
    public static final String FIELD_RESULT_FILE_PATH = "RESULT_FILE_PATH";
    public static final String FIELD_DISCARD_KEYWORDS = "DISCARD_KEYWORDS";
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////
    // VARIABLES
    // COMMON
    private String id = null;
    private int threadPoolSize = 0;
    private String resultFilePath = null;
    private final List<String> discardKeywords = new ArrayList<>();
    ////////////////////////////////////////////////////////////

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn public AuditConfig(String configPath)
     * @brief AuditConfig 생성자 함수
     * @param configPath Config 파일 경로 이름
     */
    public ConfigManager(String configPath) {
        File iniFile = new File(configPath);
        if (!iniFile.isFile() || !iniFile.exists()) {
            logger.warn("Not found the config path. (path={})", configPath);
            System.exit(1);
        }

        try {
            this.ini = new Ini(iniFile);

            loadCommonConfig();

            logger.info("Load config [{}]", configPath);
        } catch (IOException e) {
            logger.error("ConfigManager.IOException", e);
        }
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn private void loadCommonConfig()
     * @brief COMMON Section 을 로드하는 함수
     */
    private void loadCommonConfig() {
        this.id = getIniValue(SECTION_COMMON, FIELD_ID);
        if (id == null) {
            logger.error(CONSTANT_PRINT_FAIL_LOG_FORMAT_1, SECTION_COMMON, FIELD_ID);
            System.exit(1);
        }

        this.threadPoolSize = Integer.parseInt(getIniValue(SECTION_COMMON, FIELD_THREAD_POOL_SIZE));
        if (this.threadPoolSize <= 0) {
            logger.error(CONSTANT_PRINT_FAIL_LOG_FORMAT_2, SECTION_COMMON, FIELD_THREAD_POOL_SIZE, threadPoolSize);
            System.exit(1);
        }

        this.resultFilePath = getIniValue(SECTION_COMMON, FIELD_RESULT_FILE_PATH);
        if (this.resultFilePath == null) {
            logger.error(CONSTANT_PRINT_FAIL_LOG_FORMAT_1, SECTION_COMMON, FIELD_RESULT_FILE_PATH);
            System.exit(1);
        }

        String discardKeywordString = getIniValue(SECTION_COMMON, FIELD_DISCARD_KEYWORDS);
        if (discardKeywordString != null && !discardKeywordString.isEmpty()) {
            StringTokenizer stringTokenizer = new StringTokenizer(discardKeywordString.trim(), ",");
            if (stringTokenizer.hasMoreTokens()) {
                discardKeywords.add(stringTokenizer.nextToken().trim());
            }
        }

        logger.debug(CONSTANT_PRINT_SUCCESS_LOG_FORMAT, SECTION_COMMON);
    }

    ////////////////////////////////////////////////////////////////////////////////

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getThreadPoolSize() {
        return threadPoolSize;
    }

    public void setThreadPoolSize(int threadPoolSize) {
        this.threadPoolSize = threadPoolSize;
    }

    public String getResultFilePath() {
        return resultFilePath;
    }

    public void setResultFilePath(String resultFilePath) {
        this.resultFilePath = resultFilePath;
    }

    public List<String> getDiscardKeywords() {
        return discardKeywords;
    }

    ////////////////////////////////////////////////////////////////////////////////

    /**
     * @fn private String getIniValue(String section, String key)
     * @brief INI 파일에서 지정한 section 과 key 에 해당하는 value 를 가져오는 함수
     * @param section Section
     * @param key Key
     * @return 성공 시 value, 실패 시 null 반환
     */
    private String getIniValue(String section, String key) {
        String value = ini.get(section,key);
        if (value == null) {
            logger.warn("[ {} ] \" {} \" is null.", section, key);
            System.exit(1);
            return null;
        }

        value = value.trim();
        logger.debug("\tGet Config [{}] > [{}] : [{}]", section, key, value);
        return value;
    }

    ////////////////////////////////////////////////////////////////////////////////

}

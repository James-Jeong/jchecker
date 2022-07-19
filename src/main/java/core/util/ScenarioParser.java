package core.util;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import core.validation.scenario.model.CaseDto;
import core.validation.scenario.model.ScenarioDto;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ScenarioParser {

    private final ObjectMapper objectMapper;
    private final String json;

    public ScenarioParser(String scenarioPath) {
        this.objectMapper = new ObjectMapper();
        // JSON 의 모든 데이터를 파싱하는 것이 아닌 내가 필요로 하는 데이터, 즉 내가 필드로 선언한 데이터들만 파싱
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        FileManager fileManager = new FileManager();
        this.json = fileManager.readAllToString(scenarioPath);
        if (json == null || json.isEmpty()) {
            log.error("Fail to read the scenario file.");
        }
    }

    public ScenarioDto readScenarioDto() {
        if (json == null || json.isEmpty()) { return null; }

        try {
            return objectMapper.readValue(json, ScenarioDto.class);
        } catch (Exception e) {
            log.warn("ScenarioParser.readScenarioDto.Exception", e);
            return null;
        }
    }

    public CaseDto readCaseDto(Object value) {
        if (value == null) { return null; }

        try {
            return objectMapper.convertValue(value, CaseDto.class);
        } catch (Exception e) {
            log.warn("ScenarioParser.readCase.Exception", e);
            return null;
        }
    }

}

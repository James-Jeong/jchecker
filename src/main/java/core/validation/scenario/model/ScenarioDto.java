package core.validation.scenario.model;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import core.util.PrettyPrinter;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
// Words are in lower-case letters, separated by underscores
public class ScenarioDto {

    private String name;
    private Map<String, Map<String, Object>> scenarios;

    @Override
    public String toString() {
        return PrettyPrinter.printJson(this);
    }

}

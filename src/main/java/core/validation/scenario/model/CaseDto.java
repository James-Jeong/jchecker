package core.validation.scenario.model;

import core.util.PrettyPrinter;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CaseDto {

    private String expectedFile;
    private String actualFile;

    @Override
    public String toString() {
        return PrettyPrinter.printJson(this);
    }

}

package core.validation.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ValidationResultDto {

    private boolean isSuccess;
    private int wrongCount;

}

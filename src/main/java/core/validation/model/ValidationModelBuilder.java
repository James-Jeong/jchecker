package core.validation.model;

public class ValidationModelBuilder {

    private final ValidationModel validationModel;

    public ValidationModelBuilder() {
        this.validationModel = new ValidationModel();
    }

    public ValidationModelBuilder setFileName(String fileName) {
       validationModel.setFileName(fileName);
       return this;
    }

    public ValidationModelBuilder setLastLineNumber(int lastLineNumber) {
        validationModel.setLastLineNumber(lastLineNumber);
        return this;
    }

    public ValidationModelBuilder setCurLineNumber(int curLineNumber) {
        validationModel.setCurLineNumber(curLineNumber);
        return this;
    }

    public ValidationModel build() {
        return validationModel.isValid()? validationModel : null;
    }

}

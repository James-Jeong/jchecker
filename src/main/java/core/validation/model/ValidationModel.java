package core.validation.model;

public class ValidationModel {


    private String fileName;
    private int LastLineNumber;
    private int curLineNumber = 0;

    public ValidationModel() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getLastLineNumber() {
        return LastLineNumber;
    }

    public void setLastLineNumber(int lastLineNumber) {
        LastLineNumber = lastLineNumber;
    }

    public int getCurLineNumber() {
        return curLineNumber;
    }

    public void setCurLineNumber(int curLineNumber) {
        this.curLineNumber = curLineNumber;
    }

    public boolean isValid() {
        return (getFileName() != null) && (getLastLineNumber() > 0);
    }

    @Override
    public String toString() {
        return "ValidationModel{" +
                "fileName='" + fileName + '\'' +
                ", LastLineNumber=" + LastLineNumber +
                ", curLineNumber=" + curLineNumber +
                '}';
    }
}

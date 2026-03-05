package accountapi.exceptions;

public class FunctionalException extends RuntimeException {
    private String code;
    private String description;
    public FunctionalException(String code, String description) {
        super();
        this.code = code;
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
}

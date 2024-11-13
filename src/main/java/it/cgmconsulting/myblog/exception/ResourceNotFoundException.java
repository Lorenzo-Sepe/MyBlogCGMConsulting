package it.cgmconsulting.myblog.exception;

public class ResourceNotFoundException extends RuntimeException{

    private final String resourceName;

    private final String fieldName;

    private final Object value;


    /**
     * Constructs a new runtime exception with the specified detail message.
     * The cause is not initialized, and may subsequently be initialized by a
     * call to {@link #initCause}.
     *
     * @param resourceName the name of the resource
     *                     that was not found
     * @param fieldName the name of the field
     *                  that was not found
     * @param value the value of the field
     *              that was not found
     */
    public ResourceNotFoundException(String resourceName, String fieldName, Object value) {
        super(String.format("%s not found with %s: %s",resourceName, fieldName, value));
        this.resourceName = resourceName;
        this.fieldName = fieldName;
        this.value = value;
    }
}

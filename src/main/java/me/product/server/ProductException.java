package me.product.server;

/**
 * Exception thrown in the server if a logic error is detected.
 */
public class ProductException extends Exception
{
    /**
     * The constructor.
     *
     * @param message the error message to be passed to the user.
     */
    public ProductException(String message)
    {
        super(message);
    }
}

/*
* This java class gets all the exceptions in the code so that it can be logged later via a logger or a file that can be synced back to our server.
*/
package app.virraj.com.ilab;

/**
* This java class gets all the exceptions in the code so that it can be logged later via a logger or a file that can be synced back to our server.
 * @since 2018-03-15
*/
public class CustomException extends Exception {
    private String msg = "";

    /**
     * Public non-parametrized constructor.
     */
    public CustomException(){
        super();
    }

    /**
     * Constructs a new exception with {@code null} as its detail message.
     * @param message custom messages.
     */
    public CustomException(String message){
        super(message);
        this.msg = message;
    }

    /**
     * Constructs a new exception with the specified cause.
     * @param cause the cause of the exception.
     */
    public CustomException(Throwable cause){
        super(cause);

    }

    /**
     * Constructs a new exception with the custom message andspecified cause.
     * @param message custom messages.
     * @param cause the cause of the exception.
     */
    public CustomException(String message, Throwable cause){
        super(message,cause);
        this.msg= message;

    }

    /**
     * Returns the message passed with exception.
     * @return message passed with exception.
     */
    public String getMsg() {
        return msg;
    }

}

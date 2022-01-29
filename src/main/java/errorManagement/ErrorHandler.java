package errorManagement;

/**
 * Created by Alireza on 6/28/2015.
 */
public class ErrorHandler {
    public static boolean hasError = false;

    //1
    public static boolean getError() {
        return hasError;
    }

    public static void setError(boolean temp) {
        hasError = temp;
    }

    public static void printError(String msg) {
        hasError = true;
        System.out.println(msg);
    }
}


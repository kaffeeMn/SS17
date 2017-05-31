public class ExceptionHandler{
    
    public static void handleException(Exception e){
        e.printStackTrace();
        System.out.format("Something went wrong:\t%1$s\n", e.getMessage());
    }
    public static void handleException(Exception e, String message){
        System.out.println(message);
        handleException(e);
    }
}

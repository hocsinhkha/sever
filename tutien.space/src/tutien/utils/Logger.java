package tutien.utils;

import java.io.PrintWriter;
import java.io.StringWriter;


public class Logger {

    public static final String RESET = "\033[0m";

    public static final String RED = "\033[4;31m";
    public static final String GREEN = "\033[0;32m";
    public static final String PURPLE = "\033[0;35m";
    public static final String BLUE = "\033[0;34m";
    public static final String YELLOW = "\u001B[33m";
    public static String GREEN_BOLD_BRIGHT;
    
    
    
    public static final String YELLOW_BOLD = "\033[1;33m"; // YELLOW
    
    
    
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW

    /**
     * Note: System.out.print
     */
    public static void log(String text) {
        System.out.print(text);
    }
    
    public static void log(String color, String text){
        System.out.print(color + text + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void success(String text) {
        System.out.print(GREEN + text + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void warning(String text) {
        System.out.print(BLUE + text + RESET);
    }

    /**
     * Note: System.out.print
     */
    public static void error(String text) {
        System.out.print(RED + text + RESET);
    }

    public static void logException(Class clazz, Exception ex, String... log) {
        try {
            if(log != null && log.length > 0){
                log(PURPLE, log[0] + "\n");
            }
            StackTraceElement stackTraceElements[] = (new Throwable()).getStackTrace();
            String nameMethod = stackTraceElements[1].getMethodName();
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            ex.printStackTrace(pw);
            String detail = sw.toString();
            String[] arr = detail.split("\n");
            Logger.warning("Có lỗi tại class: ");
            Logger.error(clazz.getName());
            Logger.warning(" - tại phương thức: ");
            Logger.error(nameMethod + "\n");
            Logger.warning("Chi tiết lỗi:\n");
            for (String str : arr) {
                Logger.error(str + "\n");
            }
            Logger.log("--------------------------------------------------------\n");
        } catch (Exception e) {
        }
    }

    public static void GREEN(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    public static void GREEN_BOLD_BRIGHT(String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
}

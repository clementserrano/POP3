public abstract class ConsoleApp {

    public ConsoleApp(){

    }

    public static void setConsoleColor(ConsoleColor color){
        System.out.print(color);
    }

    public static void resetConsoleColor(){
        System.out.print(ConsoleColor.ANSI_RESET);
    }
}

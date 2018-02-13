import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;

public class Client extends ConsoleApp {

    public static void main(String[] args) {
        try {
            setConsoleColor(ConsoleColor.ANSI_RED);
            System.out.println("trying to connect to server");
            Socket socket = new Socket(InetAddress.getByName("localhost"), 25);
            System.out.println("connected");
            resetConsoleColor();

            setConsoleColor(ConsoleColor.ANSI_CYAN);
            System.out.println("Sending some text");
            socket.getOutputStream().write("Bonjour !".getBytes());
            resetConsoleColor();

            setConsoleColor(ConsoleColor.ANSI_RED);
            System.out.println("Closing connection");
            resetConsoleColor();

            socket.close();

        }catch (ConnectException connectException){
            resetConsoleColor();
            setConsoleColor(ConsoleColor.ANSI_RED);
            System.out.println("Unable to connect : Connexion refused. The server may not be running.");
            resetConsoleColor();
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}

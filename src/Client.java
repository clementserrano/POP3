import java.io.OutputStream;
import java.net.ConnectException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Client extends ConsoleApp {

    private static Socket socket;

    public static void main(String[] args) {
        try {
            log("trying to connect to server", ConsoleColor.ANSI_RED);
            socket = new Socket(InetAddress.getByName("localhost"), 25);
            log("Connected", ConsoleColor.ANSI_RED);

            Scanner scanner = new Scanner(System.in);

            while (socket.isConnected()){
                String input = scanner.next();
                if (input == null || input.isEmpty()){
                    //break;
                }
                else {
                    log("Sending command " + input);
                    socket.getOutputStream().write((input + "\r\n").getBytes());
                    //send(input);
                    /*
                    if (input.contains("QUIT")){
                        sendMessage(EventPOP3.QUIT);
                    }
                    */
                }
            }
            socket.close();
            log("Disconnected", ConsoleColor.ANSI_RED);

        }catch (ConnectException connectException){
            log("Unable to connect : Connexion refused. The server may not be running.", ConsoleColor.ANSI_RED);
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private static void sendMessage(EventPOP3 eventPOP3, int parameter) throws Exception {
        send(eventPOP3.getCmd() + " " + parameter);
    }

    private static void sendMessage(EventPOP3 eventPOP3) throws Exception {
        send(eventPOP3.getCmd());
    }

    private static void send(String message) throws Exception {
        log("Sending command " + message);
        socket.getOutputStream().write(message.getBytes());
    }

}

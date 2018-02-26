import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ConsoleApp {

    private static final int port = 25;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            setConsoleColor(ConsoleColor.ANSI_RED);
            log("Starting Server", ConsoleColor.ANSI_RED);
            serverSocket = new ServerSocket(port, 10);
            log("InetAddress : " + serverSocket.getInetAddress(), ConsoleColor.ANSI_RED);
            log("Port :" + serverSocket.getLocalPort(), ConsoleColor.ANSI_RED);
            log("Waiting for client ... ");

            while (true){
                Socket inputClientSocket = serverSocket.accept();
                log("Client "+ inputClientSocket.getInetAddress() + " connected.", ConsoleColor.ANSI_GREEN);

                new Thread(new Connexion(inputClientSocket)).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

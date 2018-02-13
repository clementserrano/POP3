import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ConsoleApp {

    private static final int port = 25;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            setConsoleColor(ConsoleColor.ANSI_RED);
            System.out.println("Starting Server");
            serverSocket = new ServerSocket(port, 10);
            System.out.println("InetAddress : " + serverSocket.getInetAddress());
            System.out.println("Port :" + serverSocket.getLocalPort());
            resetConsoleColor();

            while (true){
                resetConsoleColor();
                System.out.println("Waiting for client ... ");
                Socket inputClientSocket = serverSocket.accept();
                setConsoleColor(ConsoleColor.ANSI_GREEN);
                System.out.println("Client "+ inputClientSocket.getInetAddress() + " connected.");
                resetConsoleColor();

                new Thread(new Connexion(inputClientSocket)).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

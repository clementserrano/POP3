package Server;

import Helpers.ConsoleApp;
import Helpers.ConsoleColor;

import java.net.ServerSocket;
import java.net.Socket;

public class Server extends ConsoleApp {

    private static final int port = 25;
    private static ServerSocket serverSocket;

    public static void main(String[] args) {

        try {
            ConsoleApp.setConsoleColor(ConsoleColor.ANSI_RED);
            ConsoleApp.log("Starting Server.Server", ConsoleColor.ANSI_RED);
            serverSocket = new ServerSocket(port, 10);
            ConsoleApp.log("InetAddress : " + serverSocket.getInetAddress(), ConsoleColor.ANSI_RED);
            ConsoleApp.log("Port :" + serverSocket.getLocalPort(), ConsoleColor.ANSI_RED);
            ConsoleApp.log("Waiting for client ... ");

            while (true){
                Socket inputClientSocket = serverSocket.accept();
                ConsoleApp.log("Client "+ inputClientSocket.getInetAddress() + " connected.", ConsoleColor.ANSI_GREEN);

                new Thread(new Connexion(inputClientSocket)).start();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

}

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

public class Connexion implements Runnable {

    private Socket socket;
    private String message;

    public Connexion(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        try {

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            while (socket.isConnected()){
                if ((message = in.readLine()) != null ) {
                    print(message);
                }
            }
            System.out.println("Connexion closed");
            socket.close();

        }catch (Exception e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    private void print(String message){
        System.out.print(ConsoleColor.ANSI_CYAN);
        System.out.print(socket.getInetAddress() + " said : " + ConsoleColor.ANSI_RESET);
        System.out.println(message);
    }

}

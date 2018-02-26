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
            System.out.println("Connexion Thread launched");
            System.out.println(socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            States etat = States.AUTHORIZATION;

            while (socket.isConnected()){
                if ((message = in.readLine()) != null ) {
                    print(message);
                    /*
                    String[] array = message.split(" ");
                    String evt = array[0];
                    String param = array[1];

                    switch (EventPOP3.valueOf(evt)) {
                        case APOP:
                            switch (etat) {
                                case AUTHORIZATION:
                                    break;
                                case TRANSACTION:
                                    break;
                            }
                            break;
                        case STAT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    break;
                                case TRANSACTION:
                                    break;
                            }
                            break;
                        case RETR:
                            switch (etat) {
                                case AUTHORIZATION:
                                    break;
                                case TRANSACTION:
                                    break;
                            }
                            break;
                        case QUIT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    socket.close();
                                    break;
                                case TRANSACTION:
                                    socket.close();
                                    break;
                            }
                            break;

                        default:
                            break;
                    }
                    */
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

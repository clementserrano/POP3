import java.io.IOException;
import java.net.ServerSocket;

public class Serveur {

    public static void main(String[] args) {

        ServerSocket ss;

        // Ouverture connexion
        try {
            ss = new ServerSocket(110);
        } catch (IOException e) {
            e.printStackTrace();
        }



    }
}

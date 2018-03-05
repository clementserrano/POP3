package Server;

import Helpers.ConsoleColor;
import Helpers.EventPOP3;
import Helpers.States;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;


public class Connexion implements Runnable {

    private Socket socket;

    //Tableau des utilisateurs et passwords
    private static Map<String, String> users;

    //Tableau des utilisateurs et de leurs mails associés
    private static Map<String, Map<Integer, Mail>> boitesMail;


    public Connexion(Socket socket, Map<String, String> users, Map<String, Map<Integer, Mail>> boitesMail) {
        this.socket = socket;
        this.users = users;
        this.boitesMail = boitesMail;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server.Server.ReceptionAsyncTask Thread launched");
            System.out.println(socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            States etat = States.AUTHORIZATION;
            String message;
            String user = "";
            Map<Integer, Mail> boiteMail = new HashMap<>();

            while (socket.isConnected()) {
                if ((message = in.readLine()) != null) {
                    print(message);

                    String[] array = message.split(" ");
                    String evt = array[0];
                    String param = array[1];

                    switch (EventPOP3.valueOf(evt)) {
                        case APOP:
                            switch (etat) {
                                case AUTHORIZATION:
                                    // Vérifie si user existe
                                    if (users.keySet().contains(param)) {
                                        // Vérifie si pass OK
                                        String pass = new String(array[2].getBytes(), StandardCharsets.UTF_8);
                                        String passInDB =new String(MessageDigest.getInstance("MD5").digest(users.get(param).getBytes()), StandardCharsets.UTF_8);
                                        if (passInDB.equals(pass)) {
                                            user = param;
                                            boiteMail = boitesMail.get(user);
                                            String reponse = "+OK " + user + " a " + boiteMail.size() + " messages.";
                                            out.write(reponse + "\r\n");
                                            System.out.println("Server said : " + reponse);
                                        }
                                    }
                                    break;
                                case TRANSACTION:
                                    String reponse = "Commande " + message + " ignorée";
                                    out.write(reponse + "\r\n");
                                    System.out.println("Server said : " + reponse);
                                    break;
                            }
                            break;
                        case STAT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    String reponse = "Commande " + message + " ignorée";
                                    out.write(reponse + "\r\n");
                                    System.out.println("Server said : " + reponse);
                                    break;
                                case TRANSACTION:
                                    String reponse2 = "+OK nbMails : " + boiteMail.size() + " taille : "
                                            + nbOctet(boiteMail) + " octets";
                                    out.write(reponse2 + "\r\n");
                                    System.out.println("Server said : " + reponse2);
                                    break;
                            }
                            break;
                        case RETR:
                            switch (etat) {
                                case AUTHORIZATION:
                                    String reponse = "Commande " + message + " ignorée";
                                    out.write(reponse + "\r\n");
                                    System.out.println("Server said : " + reponse);
                                    break;
                                case TRANSACTION:
                                    // Vérifie si le numéro correspond
                                    Integer nMail = Integer.valueOf(param);
                                    if (boiteMail.keySet().contains(nMail)) {
                                        // Construit la réponse
                                        Mail mail = boiteMail.get(nMail);
                                        String mes = "+OK " + mail.getNbBytes() + " octets\n";
                                        mes += mail;
                                        out.write(mes + "\r\n");
                                        System.out.println("Server said : " + mes);
                                    } else {
                                        String reponse2 = "-ERR";
                                        out.write(reponse2 + "\r\n");
                                        System.out.println("Server said : " + reponse2);
                                    }
                                    break;
                            }
                            break;
                        case QUIT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    socket.close();
                                    break;
                                case TRANSACTION:
                                    // Update nothing
                                    socket.close();
                                    break;
                            }
                            break;
                        default:
                            break;
                    }

                }
            }
            System.out.println("Server.Server.ReceptionAsyncTask closed");
            socket.close();

        } catch (Exception e) {
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    private int nbOctet(Map<Integer, Mail> mails) {
        int res = 0;
        for (Map.Entry<Integer, Mail> m : mails.entrySet()) {
            res += m.getValue().getNbBytes();
        }
        return res;
    }

    private void print(String message) {
        System.out.print(ConsoleColor.ANSI_CYAN);
        System.out.print(socket.getInetAddress() + " said : " + ConsoleColor.ANSI_RESET);
        System.out.println(message);
    }
}

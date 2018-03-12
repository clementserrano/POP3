package Server;

import Helpers.ConsoleColor;
import Helpers.EventPOP3;
import Helpers.States;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.Socket;
import java.net.SocketException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class Connexion implements Runnable {

    private SSLSocket socket;

    //Tableau des utilisateurs et passwords
    private static Map<String, String> users;

    //Tableau des utilisateurs et de leurs mails associés
    private static Map<String, Map<Integer, Mail>> boitesMail;


    public Connexion(SSLSocket socket, Map<String, String> users, Map<String, Map<Integer, Mail>> boitesMail) {
        this.socket = socket;
        this.users = users;
        this.boitesMail = boitesMail;
    }

    @Override
    public void run() {
        try {
            System.out.println("Server : socket opened");
            System.out.println(socket);

            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            States etat = States.AUTHORIZATION;
            String message;
            String user = "";
            Map<Integer, Mail> boiteMail = new HashMap<>();

            SimpleDateFormat sdf = new SimpleDateFormat("hhmmss");
            String timbreADate = "<1234."+sdf.format(new Date())+"@server.com>";

            if(socket.isConnected()){
                // Envoi du timbre à date
                writeAndPrint(out, "+OK POP3 server ready " + timbreADate);
            }

            while (socket.isConnected()) {
                if ((message = in.readLine()) != null) {
                    print(message);

                    String[] array = message.split(" ");
                    String evt = array[0];
                    String param = null;

                    if(array.length > 1){
                        param = array[1];
                    }

                    switch (EventPOP3.valueOf(evt)) {
                        case APOP:
                            switch (etat) {
                                case AUTHORIZATION:
                                    // Vérifie si user existe
                                    if (users.keySet().contains(param)) {
                                        // Vérifie si pass OK
                                        String pass = new String(array[2].getBytes(), StandardCharsets.UTF_8);
                                        String sommeDeControle = new StringBuilder().append(users.get(param)).append(timbreADate).toString();
                                        String passwordMD5 = new String(MessageDigest.getInstance("MD5").digest(
                                                sommeDeControle.getBytes()), StandardCharsets.UTF_8);
                                        if (passwordMD5.equals(pass)) {
                                            user = param;
                                            boiteMail = boitesMail.get(user);
                                            writeAndPrint(out, "+OK " + user + " a " + boiteMail.size() + " messages.");
                                            etat = States.TRANSACTION;
                                        }
                                    }
                                    break;
                                case TRANSACTION:
                                    writeAndPrint(out, "Commande " + message + " ignorée");
                                    break;
                            }
                            break;
                        case STAT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    writeAndPrint(out, "Commande " + message + " ignorée");
                                    break;
                                case TRANSACTION:
                                    writeAndPrint(out, "+OK nbMails : " + boiteMail.size()
                                            + " taille : " + nbOctet(boiteMail) + " octets");
                                    break;
                            }
                            break;
                        case RETR:
                            switch (etat) {
                                case AUTHORIZATION:
                                    writeAndPrint(out,"Commande " + message + " ignorée");
                                    break;
                                case TRANSACTION:
                                    // Vérifie si le numéro correspond
                                    Integer nMail;
                                    if (param != null) {
                                        nMail = Integer.valueOf(param);
                                    }
                                    else {
                                        writeAndPrint(out, "-ERR");
                                        break;
                                    }
                                    if (boiteMail.keySet().contains(nMail)) {
                                        // Construit la réponse
                                        Mail mail = boiteMail.get(nMail);
                                        writeAndPrint(out, "+OK " + mail.getNbBytes() + " octets\n" + mail);
                                    } else {
                                        writeAndPrint(out, "-ERR");
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
            socket.close();
            System.out.println("Server : socket closed");

        } catch (SocketException e) {
            System.out.println(socket.getInetAddress() + " : " + e.getMessage());
        } catch (Exception e2){
            System.out.println("An error occurred");
            e2.printStackTrace();
        }
    }

    // Retourne le nombre d'octets total de la boite mail
    private int nbOctet(Map<Integer, Mail> mails) {
        int res = 0;
        for (Map.Entry<Integer, Mail> m : mails.entrySet()) {
            res += m.getValue().getNbBytes();
        }
        return res;
    }

    // Affiche le message du client sur la console
    private void print(String message) {
        System.out.print(ConsoleColor.ANSI_CYAN);
        System.out.print(socket.getInetAddress() + " said : " + ConsoleColor.ANSI_RESET);
        System.out.println(message);
    }

    // Ecrit sur la connexion et affiche sur la console
    private void writeAndPrint(BufferedWriter out, String message) throws IOException {
        out.write(message + "\r\n");
        out.flush();
        System.out.println("Server said : " + message);
    }
}

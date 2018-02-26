package Server;

import Helpers.ConsoleColor;
import Helpers.EventPOP3;
import Helpers.States;

import java.io.*;
import java.net.Socket;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Connexion implements Runnable {

    private Socket socket;

    //Tableau des utilisateurs et passwords
    private static Map<String, String> users;

    //Tableau des utilisateurs et de leurs mails associés
    private static Map<String, Map<Integer, String>> boitesMail;


    public Connexion(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {
        // Initialise users et boites mails
        initData();

        try {
            System.out.println("Server.Server.Connexion Thread launched");
            System.out.println(socket);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            States etat = States.AUTHORIZATION;
            String message;
            String user = "";
            Map<Integer, String> boiteMail = new HashMap<>();

            while (socket.isConnected()){
                if ((message = in.readLine()) != null ) {
                    print(message);

                    String[] array = message.split(" ");
                    String evt = array[0];
                    String param = array[1];

                    switch (EventPOP3.valueOf(evt)) {
                        case APOP:
                            switch (etat) {
                                case AUTHORIZATION:
                                    // Vérifie si user existe
                                    if(users.keySet().contains(param)){
                                        // Vérifie si pass OK
                                        String pass = array[2];
                                        if(MessageDigest.getInstance("MD5").digest(users.get(param).getBytes())
                                                .equals(MessageDigest.getInstance("MD5").digest(pass.getBytes()))){
                                            user = param;
                                            boiteMail = boitesMail.get(user);
                                            out.write("+OK " + user + " a " + boiteMail.size() + " messages.");
                                        }
                                    }
                                    break;
                                case TRANSACTION:
                                    out.write("Commande "+ message +" ignorée");
                                    break;
                            }
                            break;
                        case STAT:
                            switch (etat) {
                                case AUTHORIZATION:
                                    out.write("Commande "+ message +" ignorée");
                                    break;
                                case TRANSACTION:
                                    out.write("+OK nbMails : " + boiteMail.size() + " taille : " + nbOctet(boiteMail)
                                            + " octets");
                                    break;
                            }
                            break;
                        case RETR:
                            switch (etat) {
                                case AUTHORIZATION:
                                    out.write("Commande "+ message +" ignorée");
                                    break;
                                case TRANSACTION:
                                    // Vérifie si le numéro correspond
                                    Integer nMail = Integer.valueOf(param);
                                    if(boiteMail.keySet().contains(nMail)){
                                        // Construit la réponse
                                        String mail = boiteMail.get(nMail);
                                        String mes = "+OK " + mail.getBytes().length + " octets\n";
                                        mes += mail;
                                        out.write(mes);
                                    }else{
                                        out.write("-ERR");
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
            System.out.println("Server.Server.Connexion closed");
            socket.close();

        }catch (Exception e){
            System.out.println("An error occurred");
            e.printStackTrace();
        }
    }

    private int nbOctet(Map<Integer,String> mails) {
        int res = 0;
        for(Map.Entry<Integer,String> m : mails.entrySet()){
            res += m.getValue().getBytes().length;
        }
        return res;
    }

    private void initData() {
        // Users
        users = new HashMap<>();
        users.put("Jean","Karim");

        // Boites mail
        boitesMail = new HashMap<>();
        Map<Integer, String> mails = new HashMap<>();
        mails.put(1,"Jérome ta mère");
        mails.put(2,"Philippe ton père");
        boitesMail.put("Jean", mails);
    }

    private void print(String message){
        System.out.print(ConsoleColor.ANSI_CYAN);
        System.out.print(socket.getInetAddress() + " said : " + ConsoleColor.ANSI_RESET);
        System.out.println(message);
    }

}

/*
Написать текстовый чат для двух пользователей на сокетах.
Чат должен быть реализован по принципу клиент-сервер.
Один пользователь находится на сервере, второй --- на клиенте.
Адреса и порты задаются через командную строку: клиенту --- куда соединяться,
серверу --- на каком порту слушать.
При старте программы выводится текстовое приглашение, в котором можно ввести одну из следующих команд:
    1. Задать имя пользователя (@name Vasya);
    2. Послать текстовое сообщение (@send hello);
    3. Выход (@quit);
Принятые сообщения автоматически выводятся на экран. Программа работает по протоколу UDP.
*/

import org.w3c.dom.ls.LSOutput;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;
import java.io.IOException;

class WriteMessageServer extends Thread {
    @Override
    public void run() {
        while (true) {
            String serverMessage;
            try {
                Client.ClientData.setTime(new Date());
                Client.ClientData.setDt1(new SimpleDateFormat("HH:mm:ss"));
                Client.ClientData.setDtime(Client.ClientData.getDt1().format(Client.ClientData.getTime()));
                serverMessage = ServerData.getServerReaderConsole().readLine();

                if (serverMessage.equals("@stop")) {
                    ServerData.downServer();
                } else {
                    ServerData.getServerWriterSocket().write("[" + Client.ClientData.getDtime() + "] "
                            + ServerData.getNickname() + ": " + serverMessage + "\n");
                    System.out.println("[" + Client.ClientData.getDtime() + "] "
                            + ServerData.getNickname() + ": " + serverMessage);
                }
                ServerData.getServerWriterSocket().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}

public class Server {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter port for listening: ");
        int port = scanner.nextInt();
        ServerSocket serverSocket = new ServerSocket(port);
        System.out.println("Server is started!");
        System.out.println("Waiting for connection...");
        Socket clientSocket = serverSocket.accept();
        System.out.println("The user is connected!");
        new ServerData(clientSocket);
    }
}

class ServerData extends Thread {
    private static Socket socketToMessaging;
    private static BufferedReader serverReaderSocket;
    private static BufferedWriter serverWriterSocket;
    private static BufferedReader serverReaderConsole;

    private static final String name = "Server";

    public static String getNickname() {
        return name;
    }

    public ServerData(Socket socket) throws IOException {
        setSocketToMessaging(socket);
        setServerReaderSocket(new BufferedReader(new InputStreamReader(socket.getInputStream())));
        setServerWriterSocket(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));
        setServerReaderConsole(new BufferedReader(new InputStreamReader(System.in)));
        start();
        new WriteMessageServer().start();
    }

    public void run() {
        String word;
        try {
            word = serverReaderSocket.readLine();

            try {
                serverWriterSocket.write(word + "\n");
                serverWriterSocket.flush();
            } catch (IOException ignored) {}

            try {
                while (true) {
                    if (word != null) {
                        word = serverReaderSocket.readLine();
                        if (word != null) {
                            System.out.println(word);
                            sendMessage(word);
                        }
                    } else {
                        System.out.println("The user is disconnected!");
                        socketToMessaging.close();
                        downServer();
                        break;
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void downServer() {
        try {
            if (!socketToMessaging.isClosed()) {
                socketToMessaging.close();
                serverReaderSocket.close();
                serverWriterSocket.close();
                serverReaderConsole.close();
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendMessage(String message) {
        try {
            serverWriterSocket.write(message + "\n");
            serverWriterSocket.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Socket getSocketToMessaging() {
        return socketToMessaging;
    }

    public void setSocketToMessaging(Socket socketToMessaging) {
        ServerData.socketToMessaging = socketToMessaging;
    }

    public static BufferedReader getServerReaderSocket() {
        return serverReaderSocket;
    }

    public static void setServerReaderSocket(BufferedReader serverReaderSocket) {
        ServerData.serverReaderSocket = serverReaderSocket;
    }

    public static BufferedWriter getServerWriterSocket() {
        return serverWriterSocket;
    }

    public static void setServerWriterSocket(BufferedWriter serverWriterSocket) {
        ServerData.serverWriterSocket = serverWriterSocket;
    }

    public static BufferedReader getServerReaderConsole() {
        return serverReaderConsole;
    }

    public static void setServerReaderConsole(BufferedReader serverReaderConsole) {
        ServerData.serverReaderConsole = serverReaderConsole;
    }
}

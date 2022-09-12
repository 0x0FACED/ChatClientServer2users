import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter address: ");
        String address = scanner.nextLine();
        System.out.print("Enter port: ");
        int port = scanner.nextInt();
        new ClientData(address, port);
    }

    static class ClientData{
        private static String nickname;
        private static Socket clientSocket;
        private static BufferedReader clientReaderSocket;
        private static BufferedWriter clientWriterSocket;
        private static BufferedReader clientReaderConsole;
        private static int port;
        private static String address;

        private static Date time;
        private static String dtime;
        private static SimpleDateFormat dt1;

        public ClientData(String address, int port){
            setAddress(address);
            setPort(port);

            try{
                ClientData.clientSocket = new Socket(getAddress(), getPort());
            } catch (IOException e) {
                System.err.println("Failed.");
            }

            try{
                clientReaderConsole = new BufferedReader(new InputStreamReader(System.in));
                clientReaderSocket = new BufferedReader(new InputStreamReader(ClientData.clientSocket.getInputStream()));
                clientWriterSocket = new BufferedWriter(new OutputStreamWriter(ClientData.clientSocket.getOutputStream()));
                setFirstNickname();
                new ReadMessage().start();
                new WriteMessage().start();
            } catch (IOException e){
                closeConnection();
            }
        }

        public void setFirstNickname(){
            System.out.print("Hello! Set your nickname: ");
            try {
                setNickname(ClientData.getClientReaderConsole().readLine());
                ClientData.getClientWriterSocket().write("Hello, " + getNickname() + "!\n");
                ClientData.getClientWriterSocket().flush();
            } catch (IOException ignored) {}
        }

        public static void menuClient() throws IOException {
            while (true) {
                System.out.println("Choose option:");
                System.out.println("1. change nickname - @name. (Current nickname is " + ClientData.nickname + ");\n" +
                        "2. exit from menu - @close;\n" +
                        "3. exit from chat - @exit;\n" +
                        "0. to call the menu - @menu.\n");
                String tempStr = clientReaderConsole.readLine();
                if (tempStr.equals("@name")) {
                    writeNickname();
                } else if (tempStr.equals("@close")) {
                    break;
                } else if (tempStr.equals("@exit")){
                    closeConnection();
                } else {
                    System.out.println("Unknown command. Try again.");
                }
            }
            System.out.println("You close the menu.");
        }

        public static void closeConnection() {
            try {
                if (!clientSocket.isClosed()) {
                    clientSocket.close();

                    clientReaderSocket.close();
                    clientWriterSocket.close();
                    System.exit(0);
                }
            } catch (IOException ignored) {}
        }

        private static void writeNickname(){
            try {
                System.out.print("Write new nickname: ");
                ClientData.nickname = clientReaderConsole.readLine();
                clientWriterSocket.write("Hello, " + ClientData.nickname + "\n");
                clientWriterSocket.flush();
            } catch (IOException ignored) {}
        }

        private void setAddress(String address){
            ClientData.address = address;
        }

        private void setPort(int port){
            ClientData.port = port;
        }

        private String getAddress(){
            return ClientData.address;
        }

        private int getPort(){
            return  ClientData.port;
        }

        public static String getNickname() {
            return nickname;
        }

        public static void setNickname(String nickname) {
            ClientData.nickname = nickname;
        }

        public static Socket getClientSocket() {
            return clientSocket;
        }

        public static void setClientSocket(Socket clientSocket) {
            ClientData.clientSocket = clientSocket;
        }

        public static BufferedReader getClientReaderSocket() {
            return clientReaderSocket;
        }

        public static void setClientReaderSocket(BufferedReader clientReaderSocket) {
            ClientData.clientReaderSocket = clientReaderSocket;
        }

        public static BufferedWriter getClientWriterSocket() {
            return clientWriterSocket;
        }

        public static void setClientWriterSocket(BufferedWriter clientWriterSocket) {
            ClientData.clientWriterSocket = clientWriterSocket;
        }

        public static BufferedReader getClientReaderConsole() {
            return clientReaderConsole;
        }

        public static void setClientReaderConsole(BufferedReader clientReaderConsole) {
            ClientData.clientReaderConsole = clientReaderConsole;
        }

        public static Date getTime() {
            return time;
        }

        public static void setTime(Date time) {
            ClientData.time = time;
        }

        public static String getDtime() {
            return dtime;
        }

        public static void setDtime(String dtime) {
            ClientData.dtime = dtime;
        }

        public static SimpleDateFormat getDt1() {
            return dt1;
        }

        public static void setDt1(SimpleDateFormat dt1) {
            ClientData.dt1 = dt1;
        }
    }
}

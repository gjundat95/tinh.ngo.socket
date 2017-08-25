import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Server {

    private List<ServerThread> sockets = new ArrayList<>();
    private ServerSocket serverSocket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Server () {
        try {
            serverSocket = new ServerSocket(9999);
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }

        while (true) {
            try {
                System.out.print("Waiting client...\n");
                Socket socket = serverSocket.accept();
                System.out.print("getRemoteSocketAddress: "+socket.getRemoteSocketAddress()+"\n");
                System.out.print("getLocalAddress: "+socket.getLocalAddress()+"\n");
                System.out.print("channel: "+socket.getChannel()+"\n");

                ServerThread serverThread = new ServerThread(socket);
                sockets.add(serverThread);
                Thread thread = new Thread(serverThread);
                thread.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    class ServerThread implements Runnable {

        private BufferedWriter bufferedWriter;
        private BufferedReader bufferedReader;
        private Socket socket;

        public ServerThread(Socket socket){
            this.socket = socket;
            try {
                this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void sendMessage(String msg){
            try {
                this.bufferedWriter.write(msg);
                this.bufferedWriter.newLine();
                this.bufferedWriter.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void sendToAnyOne(String msg){
            for(ServerThread item: sockets){
                item.sendMessage(msg);
            }
        }

        @Override
        public void run() {

            try {
                String response;
                while ((response = this.bufferedReader.readLine()) != null){
                    sendToAnyOne(response);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}



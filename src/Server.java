import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Server {

    private List<ManagerServer> managerClients = new ArrayList<>();
    private List<String> users = new ArrayList<>();
    private ServerSocket serverSocket;


    public Server () throws IOException {

        serverSocket = new ServerSocket(9999);

        while (true) {
            System.out.print("Waiting client...\n");
            Socket socket = serverSocket.accept();
            System.out.print("socket connect: "+socket.getRemoteSocketAddress()+"\n");

            ManagerServer managerServer = new ManagerServer(socket);
            managerClients.add(managerServer);

            Thread serverThread = new Thread(managerServer);
            serverThread.start();
            showAllUser();

        }
    }

    public void showAllUser() {
        System.out.print("------------------------------------------- \n");
        System.out.print("List user: \n");
        for (ManagerServer client: managerClients){
            System.out.println(client.userName);
        }
        System.out.print("------------------------------------------- \n");
    }

    public void sendToAll(String userName, String msg) throws IOException {
        for(ManagerServer client: managerClients){
            if(!client.userName.equals(userName)){
                client.sendMessage(userName+": "+msg);
            }
        }
    }

    public void sendToUser(String userNameSend, String msg, String userNameReceive) throws IOException {
        for(ManagerServer client: managerClients){
            if(client.userName.equals(userNameReceive)){
                client.sendMessage(userNameSend+": "+msg);
            }
        }
    }

    class ManagerServer implements Runnable {

        private BufferedWriter bufferedWriter;
        private BufferedReader bufferedReader;
        private String userName;
        private Socket socket;

        public ManagerServer(Socket socket) throws IOException {

            this.socket = socket;
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            userName = bufferedReader.readLine();
            users.add(userName);

}

        public void sendMessage(String msg) throws IOException {
            this.bufferedWriter.write(msg);
            this.bufferedWriter.newLine();
            this.bufferedWriter.flush();
        }


        @Override
        public void run() {

            try {
                String response;
                while ((response = this.bufferedReader.readLine()) != null){

                   if (!response.equals("") && response.contains("-")){
                       sendToUser(userName, response.split("-")[1], response.split("-")[0]);

                   }else {
                       //sendToAll(userName, response);

                   }
                }

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

}



import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Scanner;

public class Server {

    private List<Socket> sockets;
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

        try {
            System.out.print("Waiting client...\n");
            Socket socket = serverSocket.accept();
            System.out.print("socket connect: "+socket.getRemoteSocketAddress()+"\n");

            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

//            Thread thread = new Thread(new ServerThread(bufferedWriter));
//            thread.start();

            String response;
            while ((response = bufferedReader.readLine()) != null){
                System.out.print("\nMessage: "+response+"\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}


//class ServerThread implements Runnable {
//
//    private BufferedWriter bufferedWriter;
//    private Scanner scanner = new Scanner(System.in);
//
//
//    public ServerThread(BufferedWriter bufferedWriter){
//        this.bufferedWriter = bufferedWriter;
//    }
//
//    @Override
//    public void run() {
//        try {
//            while (true) {
//                System.out.print("\nChat: ");
//                String text = scanner.nextLine();
//                bufferedWriter.write(text);
//                bufferedWriter.newLine();
//                bufferedWriter.flush();
//            }
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//    }
//}
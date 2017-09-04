import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client() throws IOException {

        socket = new Socket("127.0.0.1", 9999);

        bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        Thread thread = new Thread(new ClientThread(bufferedWriter));
        thread.start();

        String response;
        while ((response = bufferedReader.readLine()) != null) {
            System.out.print(response + "\n");
        }


    }

    class ClientThread implements Runnable {

        private BufferedWriter bufferedWriter;
        private Scanner scanner = new Scanner(System.in);
        private boolean isFirst = true;

        public ClientThread(BufferedWriter bufferedWriter) {
            this.bufferedWriter = bufferedWriter;
        }

        @Override
        public void run() {
            try {
                while (true) {
                   if (isFirst) {
                       System.out.print("Enter user name: ");
                       String text = scanner.nextLine();
                       bufferedWriter.write(text);
                       bufferedWriter.newLine();
                       bufferedWriter.flush();
                       isFirst =  false;
                   }else {
                       System.out.print("\nChat: \n");
                       String text = scanner.nextLine();
                       bufferedWriter.write(text);
                       bufferedWriter.newLine();
                       bufferedWriter.flush();
                   }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}


import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Scanner scanner = new Scanner(System.in);
    private boolean isChatFirst = true;

    public Client() {
        try {
            socket = new Socket("10.11.10.54", 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        Thread thread = new Thread(new ClientThread(socket));
        thread.start();

        try {
            if (isChatFirst) {
                System.out.print("\nChat: ");
                String text = scanner.nextLine();
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                bufferedWriter.flush();
                isChatFirst = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

class ClientThread implements Runnable {

    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;
    private Socket socket;
    private Scanner scanner = new Scanner(System.in);

    public ClientThread(Socket socket) {
        this.socket = socket;
        try {
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {

        try {
            String response;
            while ((response = bufferedReader.readLine()) != null) {
                System.out.print("\nMessage: " + response + "\n");
                try {
                    System.out.print("\nChat: ");
                    String text = scanner.nextLine();
                    bufferedWriter.write(text);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
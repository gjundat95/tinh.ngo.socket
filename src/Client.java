import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private BufferedReader bufferedReader;

    public Client() {
        try {
            socket = new Socket("127.0.0.1", 9999);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {

            Thread thread = new Thread(new ClientThread(bufferedWriter));
            thread.start();

            String response;
            while ((response = bufferedReader.readLine()) != null) {
                System.out.print("\nMessage: " + response + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}

class ClientThread implements Runnable {

    private BufferedWriter bufferedWriter;
    private Scanner scanner = new Scanner(System.in);

    public ClientThread(BufferedWriter bufferedWriter) {
        this.bufferedWriter = bufferedWriter;
    }

    @Override
    public void run() {
        try {
            while (true) {
                System.out.print("\nChat: ");
                String text = scanner.nextLine();
                bufferedWriter.write(text);
                bufferedWriter.newLine();
                bufferedWriter.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
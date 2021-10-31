import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.UnknownHostException;

public class Client {
    
    public static void main(String[] args) throws UnknownHostException, IOException {
        MySocket sc = new MySocket(args[0], Integer.parseInt(args[1]));

        // input thread
        new Thread() {
            public void run() {
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String line;
                try {
                    sc.println(args[2]); //Send nick to server
                    while ((line = in.readLine()) != null) {
                        sc.println(line);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                sc.shutdownInput();
            };
        }.start();

        // output thread
        new Thread() {
            public void run() {
                String line;
                while ((line = sc.readLine()) != null) {
                    System.out.println(line);
                }
            };
        }.start();

    }
}

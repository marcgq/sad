import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.SocketException;
import java.net.SocketImpl;
import java.net.UnknownHostException;

public class MySocket extends Socket {
    private BufferedReader br;
    private PrintWriter pw;

    public MySocket(String host, int port) throws UnknownHostException, IOException {
        super(host,port);
        initializeStreams();
    }

    public MySocket(SocketImpl s) throws SocketException, SecurityException {
        super(s);
    }

    public void initializeStreams() {
        try {
            br = new BufferedReader(new InputStreamReader(getInputStream()));
            pw = new PrintWriter(getOutputStream(), true); //True to autoflush stream
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String s){
        pw.println(s);
    }

    public String readLine(){
        try {
            return br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

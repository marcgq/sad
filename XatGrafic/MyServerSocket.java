import java.io.IOException;
import java.net.ServerSocket;
import java.net.SocketException;
import java.net.SocketImpl;

public class MyServerSocket extends ServerSocket{
    public MyServerSocket(int port) throws IOException, SecurityException, IllegalArgumentException{
        super(port);
    }
    public MySocket accept() throws SocketException, SecurityException, IOException{
            MySocket s = new MySocket((SocketImpl) null);
            implAccept(s);
            s.initializeStreams();
            return s;
    }
}

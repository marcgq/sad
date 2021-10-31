import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

public class Server {
    static ConcurrentHashMap<String,MySocket> m = new ConcurrentHashMap<String,MySocket>();
    public static void main(String[] args) throws IOException{
        MyServerSocket ss = new MyServerSocket( Integer.parseInt(args[0]) );
        while (true) {
            MySocket s = ss.accept();
            new Thread(){
                public void run() {
                    String line, nick = s.readLine();
                    m.put(nick, s);
                    System.out.println(nick + " joined the chat");
                    while ((line = s.readLine()) != null) {
                        for (String key: m.keySet()) {
                            if(nick!=key) m.get(key).println(nick + ": " + line);
                        }
                    }
                    m.remove(nick).shutdownInput();
                    System.out.println(nick + " left the chat");
                }
            }.start();
        }
        
    }
}

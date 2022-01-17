import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;


public class Server {
    static ConcurrentHashMap<String,MySocket> m = new ConcurrentHashMap<String,MySocket>();
    public static void main(String[] args) throws IOException{
        MyServerSocket ss = new MyServerSocket( Integer.parseInt(args[0]) );
        while (true) {
            MySocket s = ss.accept();
            
            new Thread(){
                public void run() {
                    boolean exit=false;
                    String line, nick = s.readLine();
                    if (m.containsKey(nick)) exit = true;
                    if (exit){
                        try {
                            s.println("Username already taken");
                            s.close();
                        } catch (IOException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }else{
                    //JSONObject json = new JSONObject(m);
                    String users ="";
                    for (String key: m.keySet()) {
                        users = users + key + ",";
                    }
                    s.println(users);
                    m.put(nick, s);

                    for (String key: m.keySet()) {
                        if(nick!=key) m.get(key).println("Server>Joined>"+nick);
                    }

                    System.out.println(nick + " joined the chat");

                    while ((line = s.readLine()) != null) {
                        if (!"".equals(line)){
                            for (String key: m.keySet()) {
                                if(nick!=key) m.get(key).println(nick + "> " + line);
                            }
                        }
                    }
                    m.remove(nick).shutdownInput();
                    for (String key: m.keySet()) {
                        m.get(key).println("Server>Left>"+nick);
                    }
                    System.out.println(nick + " left the chat");
                    }
                }
            }.start();
        }
        
    }
}

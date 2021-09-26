
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;


public class EditableBufferedReader extends BufferedReader {
    static final int BACK = 256, FORW = 257, ERASE=258, SUPR=259, HOME=260, END=270;
    
    
    
    public EditableBufferedReader(Reader in) throws IOException {
        super(in);
        
    }
    
    public void setRaw() throws IOException {
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo </dev/tty"});

        
    } 
    
    public void unsetRaw() throws IOException{
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty -raw echo </dev/tty"});

    }
    
    @Override
    public int read() throws IOException{
        int key = super.read();
        if (key!=27 && key!=8) {
            return key;
        } else if (key==8){
            System.out.print("\033[2K"); // Erase all line in display
            System.out.print("\033[1G"); //Cursor at 1st column
            return ERASE;
        } else {
            int key2 = super.read();
            int key3 = super.read();
           
            switch(key3){
                case 'C':
                    System.out.print("\033[1C"); //Cursor forward
                    return(FORW);
                   
                case 'D':
                    System.out.print("\033[1D"); //Cursor backward
                    return(BACK);
                case '1':
                    super.read();
                    return(HOME);
                case '3':
                    System.out.print("\033[2K"); // Erase all line in display
                    System.out.print("\033[1G"); //Cursor at 1st column
                    super.read();
                    return(SUPR);
                case '4':
                    super.read();
                    return(END);
                default:
                    return 0;
            }
            
        }
    }
    
    @Override
    public String readLine() throws IOException{
        int index = 0;
        String str = "", cola="";
        int key = 0;
        while(key!=13){
            setRaw();
            key = read();
            
            switch(key){
                case BACK:
                    if(index > 0)  index -= 1;
                    break;
                case FORW:
                    if(index<str.length())  index += 1;
                    System.out.print("\033["+(index+1)+"G");
                    break;
                case HOME:
                    index=0;
                    System.out.print("\033["+(index+1)+"G");
                    break;
                case END:
                    index=str.length();
                    System.out.print("\033["+(index+1)+"G");
                    break;
                case 13:
                    break;
                case ERASE:
                    if(index==0) break;
                    cola = str.substring(index);
                    str = str.substring(0, index-1) + cola;
                    System.out.print(str);
                    System.out.print("\033["+(index)+"G"); //Cursor placement
                    index-=1;
                    break;
                case SUPR:
                    if(index==str.length()) {
                        System.out.print(str);
                        System.out.print("\033["+(index+1)+"G");
                        break;
                    }
                    cola = str.substring(index+1);
                    str = str.substring(0, index) + cola;
                    System.out.print(str);
                    System.out.print("\033["+(index+1)+"G"); //Cursor placement
                    
                    break;
                default:
                    cola = str.substring(index);
                    str = str.substring(0, index) + (char)key + cola;
                    index+=1;
                    System.out.print((char) key+cola);
                    System.out.print("\033["+(index+1)+"G"); //Cursor placement
                    
            } 
            
        }
        unsetRaw();
        return str;
    }
}

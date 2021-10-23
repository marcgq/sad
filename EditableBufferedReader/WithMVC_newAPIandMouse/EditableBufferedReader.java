import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;


public class EditableBufferedReader extends BufferedReader {
    Console console;
    int mouseX, mouseY;

    public EditableBufferedReader(Reader in) throws IOException {
        super(in);
    }

    public void setRaw() throws IOException {
        Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", "stty raw -echo </dev/tty" });
    }

    public void unsetRaw() throws IOException {
        Runtime.getRuntime().exec(new String[] { "/bin/sh", "-c", "stty -raw echo </dev/tty" });
    }

    @Override
    public int read() throws IOException {
        int key = super.read();
        if (key != Key.ESCAPE && key != Key.BACKSPACE) {
            return key;
        } else if (key == Key.BACKSPACE) { 
            return Key.ERASE;
        } else {
            if(super.read()!='[') return (Key.BELL);
            switch (key=super.read()) {
                case 'C': // Right arrow
                    return (Key.FORW);
                case 'D': // Left arrow
                    return (Key.BACK);
                case 'M':
                    int button = super.read();
                    if((button & 0b1000011) == 0){ //Check MB1 was pressed
                        mouseX = Integer.parseInt(new String(Integer.toString(super.read()).getBytes("UTF-8"),"ISO-8859-1"))-32;
                        mouseY = Integer.parseInt(new String(Integer.toString(super.read()).getBytes("UTF-8"),"ISO-8859-1"))-32;
                        return(Key.MOUSE);
                    } 
                    super.read(); //Discard 2 following bytes
                    super.read();
                    return(Key.BELL);
                case '1': // Home
                case '2': // Insert
                case '3': // Delete
                case '4': // End
                    super.read(); //Flush wave hyphen
                    return (Key.HOME + key - '1');
                default:
                    return (Key.BELL);
            }
        }
    }

    @Override
    public String readLine() throws IOException {
        setRaw();
        Console console = new Console();
        Line line = new Line(console);
        line.setRow(getRow());
        int key = 0;
        while (key != Key.CR) {
            switch (key = read()) {
                case Key.BACK:
                    line.decrementIndex();
                    break;
                case Key.FORW:
                    line.incrementIndex();
                    break;
                case Key.HOME:
                    line.homeIndex();
                    break;
                case Key.END:
                    line.endIndex();
                    break;
                case Key.INS:
                    line.insertMode = !line.insertMode;
                    break;
                case Key.ERASE:
                    line.delete();
                    break;
                case Key.SUPR:
                    line.supress();
                    break;
                case Key.MOUSE:
                    line.mouseIndex(mouseX, mouseY);
                case Key.BELL:
                case Key.CR: 
                    break;
                default:
                    line.insert((char) key, line.insertMode);
            }
        }
        unsetRaw();
        return line.toString();
    }

    public int getRow() throws IOException{
        readUntil('[');
        int row = Integer.parseInt(readUntil(';'));
        readUntil('R');
        return row;
    }

    private String readUntil(char delim) throws IOException{
        StringBuilder b = new StringBuilder();
        int ch;
        while((ch=super.read()) != delim){
            b.append((char) ch);
        }
        return b.toString();
    }
}

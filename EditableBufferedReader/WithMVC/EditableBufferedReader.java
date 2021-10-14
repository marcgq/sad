import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

public class EditableBufferedReader extends BufferedReader {
    Console console;
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
        Line line = new Line();
        console = new Console();
        line.addObserver(console);
        int key = 0;
        setRaw();
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
                    System.out.print("\033[1G");
                    break;
                case Key.END:
                    line.endIndex();
                    System.out.print("\033[" + (line.getIndex() + 1) + "G");
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
                case Key.BELL:
                case Key.CR: // Carriage return
                    break;
                default:
                    line.insert((char) key, line.insertMode);
            }
        }
        unsetRaw();
        return line.toString();
    }
}

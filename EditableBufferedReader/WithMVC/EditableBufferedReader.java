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
                    super.read(); //Flush wave hyphen
                    return (Key.HOME);
                case '2':
                    super.read(); //Flush wave hyphen
                    return (Key.INS);
                case '3': // Delete
                    super.read(); //Flush wave hyphen
                    return (Key.SUPR);
                case '4': // End
                    super.read(); //Flush wave hyphen
                    return (Key.END);
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
        boolean insertMode = true;
        int key = 0;
        while (key != Key.CR) {
            setRaw();
            switch (key = read()) {
                case Key.BACK:
                case Key.FORW:
                case Key.HOME:
                case Key.END:
                    line.modifyIndex(key);
                    break;
                case Key.INS:
                    insertMode = !insertMode;
                    break;
                case Key.ERASE:
                case Key.SUPR:
                    line.delete(key);
                    break;
                case Key.BELL:
                case Key.CR: // Carriage return
                    break;
                default:
                    line.insert((char) key, insertMode);
            }
        }
        unsetRaw();
        return line.getString();
    }
}

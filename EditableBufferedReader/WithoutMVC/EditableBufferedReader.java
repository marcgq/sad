
import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author marc
 */

public class EditableBufferedReader extends BufferedReader {
    public static final int BACK = 256, FORW = 257, ERASE = 258, SUPR = 259, HOME = 260, END = 270, INS = 271;

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
        if (key != 27 && key != 8) {
            System.out.print("\033[2K"); // Erase all line in display
            System.out.print("\033[1G"); // Cursor at 1st column
            return key;
        } else if (key == 8) { // Backspace
            System.out.print("\033[2K"); // Erase all line in display
            System.out.print("\033[1G"); // Cursor at 1st column
            return ERASE;
        } else {
            super.read();
            int key2= super.read();

            switch (key2) {
                case 'C': // Right arrow
                    System.out.print("\033[1C"); // Cursor forward
                    return (FORW);

                case 'D': // Left arrow
                    System.out.print("\033[1D"); // Cursor backward
                    return (BACK);
                case '1': // Home
                    super.read();
                    return (HOME);
                case '3': // Delete
                    System.out.print("\033[2K"); // Erase all line in display
                    System.out.print("\033[1G"); // Cursor at 1st column
                    super.read();
                    return (SUPR);
                case '2':
                    super.read();
                    return (INS);
                case '4': // End
                    super.read();
                    return (END);
                default:
                    return 0;
            }

        }
    }

    @Override
    public String readLine() throws IOException {
        Line line = new Line();
        boolean insertMode = true;
        int key = 0;
        while (key != 13) {
            setRaw();
            key = read();

            switch (key) {
                case BACK:
                case FORW:
                case HOME:
                case END:
                    line.modifyIndex(key);
                    System.out.print("\033[" + (line.getIndex() + 1) + "G");
                    break;
                case INS:
                    insertMode = !insertMode;
                    break;
                case 13: // Carriage return
                    System.out.print(line);
                    break;
                case ERASE:
                    line.delete(key);
                    System.out.print(line);
                    System.out.print("\033[" + (line.getIndex()) + "G"); // Cursor placement
                    break;
                case SUPR:
                    line.delete(key);
                    System.out.print(line);
                    System.out.print("\033[" + (line.getIndex() + 1) + "G"); // Cursor placement
                    break;
                default:
                    line.insert((char) key, insertMode);
                    System.out.print(line);
                    System.out.print("\033[" + (line.getIndex() + 1) + "G"); // Cursor placement
            }

        }
        unsetRaw();
        return line.getString();
    }
}

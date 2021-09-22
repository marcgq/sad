
import java.io.IOException;
import java.io.Reader;

public class columnes {

    public static void main(String[] args) throws IOException, InterruptedException {
        //Terminal in raw mode with supressed echo
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty raw -echo </dev/tty"}).waitFor();

        //Escape sequence to request terminal dimensions (in characters)
        System.out.print("\033[18t");

        //Reading request from console and storing it in a char array
        char[] buf = new char[10];
        Reader reader = System.console().reader();
        reader.read(buf);

        //Terminal in cooked mode with echo
        Runtime.getRuntime().exec(new String[]{"/bin/sh", "-c", "stty -raw echo </dev/tty"}).waitFor();

        //Parsing the obtained values stored in an array into two separed variables
        String column = "", row = "";
        boolean row_found = false;
        for (int i = 4; i < buf.length; i++) {
            if (buf[i] != 59 && buf[i] != 116) {
                if (!row_found) {
                    row += buf[i];
                } else {
                    column += buf[i];
                }
            } else if (buf[i] == 59 && !row_found) {
                row_found = true;
            }
        }

        //Printing terminal size
        System.out.println("The size of the terminal (in characters) is " + column + " columns and " + row + " rows");

    }
}

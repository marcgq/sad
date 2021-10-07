import java.util.Observable;
import java.util.Observer;

public class Console implements Observer {

    @Override
    public void update(Observable o, Object arg) {
        if(arg!=null && (int) arg==Key.BELL){
            System.out.print("\007");
        } else {
            System.out.print("\033[2K" + "\033[1G" + (Line)o + "\033[" + (((Line) o).getIndex()+1) + "G"); // Erase line, cursor at begg., print line, place cursor
        }
    }
    
}

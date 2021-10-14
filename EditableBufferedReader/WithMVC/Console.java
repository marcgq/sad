import java.util.Observable;
import java.util.Observer;

public class Console implements Observer {

    @Override
    public void update(Observable o, Object arg) { 
        if (arg != null) {
            switch ((int) arg) {
                case Key.BACK:
                    System.out.print("\033[1D");
                    break;
                case Key.FORW:
                    System.out.print("\033[1C");
                    break;
                case Key.HOME:
                    System.out.print("\033[1G");
                    break;
                case Key.END:
                    System.out.print("\033[" + (((Line) o).getIndex() + 1) + "G");
                    break;
                case Key.ERASE:
                    System.out.print("\033[1D" + "\033[1P"); // Cursor backwards, delete character
                    break;
                case Key.SUPR:
                    System.out.print("\033[1P"); // Delete character
                    break;
                case Key.BELL:
                    System.out.print("\007"); // Ring bell
                case Key.CR: // Carriage return
                    break;      
            }          
        } else {
            int index = ((Line) o).getIndex();
            System.out.print("\033[0K" + ((Line) o).toString().substring(index-1) + "\033[" + (index + 1) + "G");                                                                                                    
                
        }
    }
}


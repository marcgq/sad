import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;


public class Console implements PropertyChangeListener {
    public Console() {
        super();
        System.out.print("\033[1;2'z"); // Enable locator reporting
        System.out.print("\033[?1000h");  //  DECSET Enable Mouse Tracking 
        System.out.print("\033[6n"); //Report cursor position to get row 
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        switch (evt.getPropertyName()) {
            case "bell":
                System.out.print("\007");
                break;
            case "str":
                int index = ((Line) evt.getSource()).getIndex();
                System.out.print("\033[0K" + ((String) evt.getNewValue()).substring(index-1) + "\033[" + (index + 1) + "G");
                break;                                                                                                   
            case "str_delete":
                System.out.print("\033[1D" + "\033[1P");
                break;
            case "str_supress":
                System.out.print("\033[1P"); 
                break;
            case "index": 
                int newIndex = (Integer) evt.getNewValue();
                int oldIndex = (Integer) evt.getOldValue();
                if (newIndex == oldIndex-1) {
                    System.out.print("\033[1D");
                } else if (newIndex == oldIndex+1) {
                    System.out.print("\033[1C");
                } else if (newIndex==0) {
                    System.out.print("\033[1G");
                } else {
                    System.out.print("\033[" + ((Integer)evt.getNewValue() + 1) + "G");
                }

        }
        
    }
    
}

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


public class Line{
    int index;
    String str;
    boolean insertMode;
    PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Line(PropertyChangeListener pcl) {
        index = 0;
        str = "";
        insertMode = true;
        pcs.addPropertyChangeListener(pcl);
    }

    public void insert(char car, Boolean insert) {
        String str_aux = str;
        if (insert) {
            String cola = str.substring(index);
            str = str.substring(0, index) + car + cola;
            index += 1;
            
        } else {
            if (index == str.length()) {
                str = str + car;
                index += 1;
            } else {
                String cola = str.substring(index + 1);
                str = str.substring(0, index) + car + cola;
                index += 1;
            }
        }
        pcs.firePropertyChange(new PropertyChangeEvent(this, "str", str_aux, str));
    }

    public void incrementIndex(){
        if (index < str.length()) {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "index", index, index+=1)); 
        } else {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "bell", null, null));
        }
    }

    public void decrementIndex(){
        if (index > 0) {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "index", index, index-=1));
        } else {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "bell", null, null));
        }
    }
    
    public void homeIndex(){
        pcs.firePropertyChange(new PropertyChangeEvent(this, "index", index, index=0));
    }

    public void endIndex(){
        pcs.firePropertyChange(new PropertyChangeEvent(this, "index", index, index=str.length()));
    }
    
    public void delete() {
        String str_aux = str;
        if (index > 0) {
            String cola = str.substring(index);
            str = str.substring(0, index - 1) + cola;
            index -= 1;
            pcs.firePropertyChange(new PropertyChangeEvent(this, "str_delete", str_aux, str));
        } else {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "bell", null, null));
        }
    }

    public void supress() {
        String str_aux = str;
        if (index < str.length()) {
            String cola = str.substring(index + 1);
            str = str.substring(0, index) + cola;
            pcs.firePropertyChange(new PropertyChangeEvent(this, "str_supress", str_aux, str));
        } else {
            pcs.firePropertyChange(new PropertyChangeEvent(this, "bell", null, null));
        }
    }
    public int getIndex() {
        return index;
    }

    public String toString() {
        return str;
    }

}

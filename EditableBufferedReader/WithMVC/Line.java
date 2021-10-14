import java.util.Observable;

public class Line extends Observable {
    int index;
    String str;
    boolean insertMode;

    public Line() {
        super();
        index = 0;
        str = "";
        insertMode = true;
    }

    public void insert(char car, Boolean insert) {
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
        setChanged();
        notifyObservers();
    }

    public void incrementIndex(){
        if (index < str.length()) {
            index += 1;
            setChanged();
            notifyObservers(Key.FORW);
            
        } else {
            setChanged();
            notifyObservers(Key.BELL);
        }
    }

    public void decrementIndex(){
        if (index > 0) {
            index -= 1;
            setChanged();
            notifyObservers(Key.BACK);
        }  else {
            setChanged();
            notifyObservers(Key.BELL);
        }
    }
    
    public void homeIndex(){
        index=0;
        setChanged();
        notifyObservers(Key.HOME);
    }

    public void endIndex(){
        index=str.length();
        setChanged();
        notifyObservers(Key.END);
    }
    public void delete() {
        if (index > 0) {
            String cola = str.substring(index);
            str = str.substring(0, index - 1) + cola;
            index -= 1;
            setChanged();
            notifyObservers(Key.ERASE);
        } else {
            setChanged();
            notifyObservers(Key.BELL);
        }
    }

    public void supress() {
        if (index < str.length()) {
            String cola = str.substring(index + 1);
            str = str.substring(0, index) + cola;
            setChanged();
            notifyObservers(Key.SUPR);
        } else {
            setChanged();
            notifyObservers(Key.BELL);
        }
    }
    public int getIndex() {
        return index;
    }

    public String toString() {
        return str;
    }

}

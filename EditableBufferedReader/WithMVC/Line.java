import java.util.Observable;

public class Line extends Observable {
    int index;
    String str;
    
    public Line() {
        super();
        index = 0;
        str = "";
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

    public void delete(int key) {
        if (key == Key.ERASE && index > 0) {
            String cola = str.substring(index);
            str = str.substring(0, index - 1) + cola;
            index -= 1;
            setChanged();
            notifyObservers();
        } else if (key == Key.SUPR && index < str.length()) {
            String cola = str.substring(index + 1);
            str = str.substring(0, index) + cola;
            setChanged();
            notifyObservers();
        } else {
            setChanged();
            notifyObservers(Key.BELL);
        }

    }

    public void modifyIndex(int key) {
        switch (key) {
            case Key.BACK:
                if (index > 0) {
                    index -= 1;
                    setChanged();
                    notifyObservers();
                }  else {
                    setChanged();
                    notifyObservers(Key.BELL);
                }
                break;
            case Key.FORW:
                if (index < str.length()) {
                    index += 1;
                    setChanged();
                    notifyObservers();
                } else {
                    setChanged();
                    notifyObservers(Key.BELL);
                }
                break;
            case Key.HOME:
                index = 0;
                setChanged();
                notifyObservers();
                break;
            case Key.END:
                index = str.length();
                setChanged();
                notifyObservers();
        }
    }

    public int getIndex() {
        return index;
    }
    public String getString(){
        return str;
    }

    public String toString() {
        return str;
    }

}

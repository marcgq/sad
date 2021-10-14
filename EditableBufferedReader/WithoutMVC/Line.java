public class Line {
    int index;
    String str;
    boolean insertMode;

    public Line() {
        index = 0;
        str = "";
        insertMode = true;
    }

    public String insert(char car, Boolean insert) {
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
        return str.substring(index-1);
    }

    public void delete() {
        if (index > 0) {
            String cola = str.substring(index);
            str = str.substring(0, index - 1) + cola;
            index -= 1;
        } else {
            System.out.print("\007");
        }
    }

    public void supress() {
        if (index < str.length()) {
            String cola = str.substring(index + 1);
            str = str.substring(0, index) + cola;
        } else {
            System.out.print("\007");
        }
    }

    public boolean incrementIndex(){
        if (index < str.length()) {
            index += 1;
            return true;
        } else {
            System.out.print("\007");
            return false;
        }
    }

    public boolean decrementIndex(){
        if (index > 0) {
            index -= 1;
            return true;
        }  else {
            System.out.print("\007");
            return false;
        }
    }
    
    public void homeIndex(){
        index=0;
    }

    public void endIndex(){
        index=str.length();
    }

    public int getIndex() {
        return index;
    }

    public String toString() {
        return str;
    }

}

public class Line {
    int index;
    String str;
    public static final int BACK = 256, FORW = 257, ERASE = 258, SUPR = 259, HOME = 260, END = 270, INS = 271;

    public Line() {
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
    }

    public void delete(int key) {
        if (key == ERASE && index > 0) {
            String cola = str.substring(index);
            str = str.substring(0, index - 1) + cola;
            index -= 1;
        } else if (key == SUPR && index < str.length()) {
            String cola = str.substring(index + 1);
            str = str.substring(0, index) + cola;
        }

    }

    public void modifyIndex(int key) {
        switch (key) {
            case BACK:
                if (index > 0)
                    index -= 1;
                break;
            case FORW:
                if (index < str.length())
                    index += 1;

                break;
            case HOME:
                index = 0;

                break;
            case END:
                index = str.length();
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

public class Item {

    private int index, value;

    public Item(int index, int value) {
        this.index = index;
        this.value = value;
    }

    public String toString() {
        return "(" + index + "," + value + ")";
    }

    public int getIndex() {
        return index;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
import java.util.*;

public class Anim {

    private Item pivot;
    private ArrayList<Item> i_checks, j_checks;
    private ArrayList<ItemPair> swapped;

    public int i, j;
    private int low, high;

    public Anim() {
        pivot = null;
        i_checks = new ArrayList<>();
        j_checks = new ArrayList<>();
        swapped = new ArrayList<>();
    }

    public void setPivot(Item pivot) {
        this.pivot = pivot;
    }

    public Item getPivot() {
        return pivot;
    }

    public void addI_check(Item item) {
        i_checks.add(item);
    }

    public ArrayList<Item> getI_checks() {
        return i_checks;
    }

    public void addJ_check(Item item) {
        j_checks.add(item);
    }

    public ArrayList<Item> getJ_checks() {
        return j_checks;
    }

    public void addSwap(Item a, Item b) {
        swapped.add(new ItemPair(a, b));
    }

    public ArrayList<ItemPair> getSwapped() {
        return swapped;
    }

    public void setLow(int low) {
        this.low = low;
        i = low;
    }

    public int getLow() {
        return low;
    }

    public void setHigh(int high) {
        this.high = high;
        j = high;
    }

    public int getHigh() {
        return high;
    }

}
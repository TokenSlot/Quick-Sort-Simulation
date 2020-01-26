public class ItemPair {

    private Item a, b;

    public ItemPair(Item a, Item b) {
        this.a = a;
        this.b = b;
    }

    public String toString() {
        return "(" + a + "," + b + ")";
    }

    public Item getA() {
        return a;
    }

    public Item getB() {
        return b;
    }

}
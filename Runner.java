public class Runner {

    public static void main(String[] args) {
        QuickSort quickie = new QuickSort();
        quickie.setVisible(true);
    }
}


// import java.util.*;

// public class Anim {

//     private Item pivot;
//     private ArrayList<Item> i_checks, j_checks;
//     private ItemPair swapped;

//     public Anim() {
//         pivot = null;
//         i_checks = new ArrayList<>();
//         j_checks = new ArrayList<>();
//         swapped = null;
//     }

//     public void setPivot(Item pivot) {
//         this.pivot = pivot;
//     }

//     public Item getPivot() {
//         return pivot;
//     }

//     public void addI_check(Item item) {
//         i_checks.add(item);
//     }

//     public ArrayList<Item> getI_checks() {
//         return i_checks;
//     }

//     public void addJ_check(Item item) {
//         j_checks.add(item);
//     }

//     public ArrayList<Item> getJ_checks() {
//         return j_checks;
//     }

//     public void setSwapped(Item a, Item b) {
//         swapped = new ItemPair(a, b);
//     }

//     public ItemPair getSwapped() {
//         return swapped;
//     }
// }
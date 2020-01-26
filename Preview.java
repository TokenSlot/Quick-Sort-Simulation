import java.util.ArrayList;

public class Preview {

    private ArrayList<Item> lowList;
    private ArrayList<Item> equalList;
    private ArrayList<Item> highList;
    private Item pivot;

    /**
     * Finds all the lower, equal and higher values based
     * on the pivot and adds them into an Array List.
     * @param arr array to be partitioned
     * @param pi the pivot's index
     */
    public Preview(Item[] arr, int low, int high, int pi) {
        pivot = arr[pi];

        lowList = new ArrayList<>();
        equalList = new ArrayList<>();
        highList = new ArrayList<>();

        // Testing purposes
        for (Item x : arr) {
            System.out.print(x + ", ");
        }
        System.out.println("\n" + low + " " + high);
        for (int i = low; i <= high; i++) {
            System.out.print(pi + " - ");
            System.out.print(arr[i]);
            if (arr[i].getValue() < arr[pi].getValue()) {
                System.out.print(" < ");
                lowList.add(arr[i]);
            } else if (arr[i].getValue() > arr[pi].getValue()) {
                System.out.print(" > ");
                highList.add(arr[i]);
            } else {
                System.out.print(" = ");
                if (!arr[i].equals(pivot)) equalList.add(arr[i]);
            }
            System.out.println(arr[pi]);
        }
        System.out.println("");

        // for (int i = low; i <= high; i++) {
        //     if (arr[i].getValue() < arr[pi].getValue()) {
        //         lowList.add(arr[i]);
        //     } else if (arr[i].getValue() > arr[pi].getValue()) {
        //         highList.add(arr[i]);
        //     } else {
        //         if (!arr[i].equals(pivot)) equalList.add(arr[i]);
        //     }
        // }
    }

    public ArrayList<Item> getLowList() {
        return lowList;
    }

    public ArrayList<Item> getEqualList() {
        return equalList;
    }

    public ArrayList<Item> getHighList() {
        return highList;
    }

    public Item getPivot() {
        return pivot;
    }

    /* Test Run */
    // public static void main(String[] args) {
    //     int[] arr = {3,11,3,0,15,-24};
    //     int pi = arr.length/2;
    //     AnimQ animq = new AnimQ(arr, pi-1);
    //     for (int x : animq.low) {
    //         System.out.print(x + " ");
    //     }
    //     System.out.println("");
    //     for (int x : animq.equal) {
    //         System.out.print(x + " ");
    //     }
    //     System.out.println("");
    //     for (int x : animq.high) {
    //         System.out.print(x + " ");
    //     }
    // }
}
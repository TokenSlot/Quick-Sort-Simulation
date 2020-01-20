import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import javax.swing.event.*;
import java.util.*;
import javax.swing.UIManager.LookAndFeelInfo;

public class QuickSort extends JFrame {

    private static final long serialVersionUID = 1043813624503542032L;

    private JLabel[] indexLabel;
    private JLabel[] arrLabel;
    private JLabel lowLabel, equalLabel, greatLabel, passLabel, speedLabel;
    private JSlider speedSlider;
    private JToggleButton playBtn;
    private JButton inputBtn, randBtn, randAllBtn;
    private JComboBox<Integer> indexPicker;
    private JTextField inputField;

    private Timer tm;

    private Item[] arr;
    private Item[] orig; //used for resetting the ui

    public Item[] ts;

    private Queue<AnimQ> animq;

    public QuickSort() {

        try {
            // Changes the theme of the UI to nimbus
            for (LookAndFeelInfo info:UIManager.getInstalledLookAndFeels())  {
                if ("Nimbus".equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
            java.net.URL url = ClassLoader.getSystemResource("resources/ico.png");
            Toolkit kit = Toolkit.getDefaultToolkit();
            Image img = kit.createImage(url);
            setIconImage(img);
        } catch (Exception e) {}

        animq = new LinkedList<>();

        indexLabel = new JLabel[10];

        for (int i = 0; i < 10; i++) {
            indexLabel[i] = new JLabel(i + "");
            indexLabel[i].setFont(new Font("Dialog", 1, 20));
            indexLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            indexLabel[i].setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            indexLabel[i].setBounds(10+(80*i), 10, 70, 30);
            add(indexLabel[i]);
        }

        arrLabel = new JLabel[10];
        arr = new Item[10];
        arr = generateArray();

        orig = new Item[10];
        orig = copyArray(arr, orig);

        for (int i = 0; i < 10; i++) {
            arrLabel[i] = new JLabel(arr[i].getValue() + "");
            int len = arrLabel[i].getText().length();
            arrLabel[i].setFont(new Font("Dialog", 1, 20-len));
            arrLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            arrLabel[i].setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            arrLabel[i].setBounds(10+(80*i), 50, 70, 30);
            add(arrLabel[i]);
        }

        lowLabel = new JLabel("<");
        lowLabel.setFont(new Font("Dialog", 0, 24));
        lowLabel.setHorizontalAlignment(SwingConstants.CENTER);
        lowLabel.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        lowLabel.setBounds(10, 170, 70, 30);

        equalLabel = new JLabel("=");
        equalLabel.setFont(new Font("Dialog", 0, 24));
        equalLabel.setHorizontalAlignment(SwingConstants.CENTER);
        equalLabel.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        equalLabel.setBounds(10, 210, 70, 30);

        greatLabel = new JLabel(">");
        greatLabel.setFont(new Font("Dialog", 0, 24));
        greatLabel.setHorizontalAlignment(SwingConstants.CENTER);
        greatLabel.setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
        greatLabel.setBounds(10, 250, 70, 30);

        passLabel = new JLabel("Pass: 1");
        passLabel.setBounds(10, 290, 43, 16);

        speedLabel = new JLabel("Speed: x0");
        speedLabel.setBounds(63, 290, 65, 16);

        speedSlider = new JSlider();
        speedSlider.setMaximum(2);
        speedSlider.setValue(0);
        speedSlider.setBounds(10, 310, 200, 30);
        speedSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent evt) {
                speedSliderStateChanged();
            }
        });

        playBtn = new JToggleButton("Play");
        playBtn.setBounds(213, 310, 60, 30);
        playBtn.addItemListener(new ItemListener() {
            @Override
            public void itemStateChanged(ItemEvent evt) {
                playBtnItemStateChanged(evt);
            }
        });

        indexPicker = new JComboBox<>();
        indexPicker.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {0,1,2,3,4,5,6,7,8,9}));
        indexPicker.setBounds(432, 313, 40, 26);

        inputField = new JTextField();
        inputField.setBounds(475, 314, 90, 24);
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputBtnActionPerformed(evt);
                increasePicker();
            }
        });

        inputBtn = new JButton("Input");
        inputBtn.setBounds(568, 310, 60, 30);
        inputBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputBtnActionPerformed(evt);
            }
        });

        randBtn = new JButton("Random");
        randBtn.setBounds(631, 310, 77, 30);
        randBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                randBtnActionPerformed(evt);
            }
        });

        randAllBtn = new JButton("Random All");
        randAllBtn.setBounds(711, 310, 94, 30);
        randAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                randAllBtnActionPerformed(evt);
            }
        });

        add(lowLabel);
        add(equalLabel);
        add(greatLabel);
        add(passLabel);
        add(speedLabel);
        add(speedSlider);
        add(playBtn);
        add(indexPicker);
        add(inputField);
        add(inputBtn);
        add(randBtn);
        add(randAllBtn);

        tm = new Timer(1, new ActionListener(){
            public void actionPerformed(ActionEvent evt) {
                timerActionPerformed(evt);
            }
        });
    }

    /**
     * Takes the middle element as pivot, places it the right position
     * places smaller values to the left of the pivot and adds them to
     * a an AnimQ's low list and all equal or greater values are placed
     * to the right of the pivot but equal values are added to an AnimQ's
     * equal list and the greater values are added to the AnimQ's high list.
     * This function uses Hoare's partition scheme.
     * @param arr array to be partitioned
     * @param low starting index of the array
     * @param high ending index of the array
     * @return the partitioning index
     */
    private int partition(Item[] arr, int low, int high) {
        int pi = low+(high-low)/2;
        int pivot = arr[pi].getValue();
        int i = low - 1, j = high + 1;

        AnimQ q = new AnimQ(arr, low, high, pi);
        animq.add(q);
        while (true) {

            do {
                i++;
            } while (arr[i].getValue() < pivot);

            do {
                j--;
            } while (arr[j].getValue() > pivot);

            if (i >= j) {
                return j;
            }

            Item temp = new Item(arr[i].getIndex(), arr[i].getValue());
            arr[i] = arr[j];
            arr[j] = temp;
        }
    }

    /**
     * Main function that implements the Quick Sort Algorithm.
     * @param arr array to be sorted
     * @param low starting index of the array
     * @param high ending index of the array
     * @return sorted array
     */
    public void quickSort(Item[] arr, int low, int high) {
        if (low < high) {
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi - 1);
            quickSort(arr, pi + 1, high);
        }
    }

    /**
     * Copies array a to array b
     * @param a base array
     * @param b array copied from array a
     * @return copied array
     */
    private Item[] copyArray(Item[] a, Item[] b) {
        for (int i = 0; i < b.length; i++) {
            b[i] = new Item(i, a[i].getValue());
        }

        return b;
    }

    /**
     * Generates an array with random values from 0 to Integer.MAX_VALUE.
     * @return generated array
     */
    private Item[] generateArray() {
        Random rand = new Random();
        Item arr[] = new Item[10];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = new Item(i, rand.nextInt(Integer.MAX_VALUE));
            // arr[i] = new Item(i, rand.nextInt(10));
        }

        return arr;
    }

    /**
     * Controls the speed of the animation.
     */
    private void speedSliderStateChanged() {
        speedLabel.setText("Speed: x" + speedSlider.getValue());
        if (speedSlider.getValue() > 0) {
            playBtn.setText("Stop");
            playBtn.setSelected(true);

            // tm.setDelay(speedSlider.getValue());
            // tm.start();

            // testSort();

            // Resets the input forms and disables the option to change
            // the values of the array when the animation is playing.
            indexPicker.setEnabled(false);
            inputField.setEnabled(false);
            inputBtn.setEnabled(false);
            randBtn.setEnabled(false);
            randAllBtn.setEnabled(false);
            indexPicker.setSelectedIndex(0);
            inputField.setText("");
        } else if (speedSlider.getValue() == 0) {
            playBtn.setText("Play");
            playBtn.setSelected(false);

            tm.stop();

            // Enables the option to change the values
            // of the array when the animation is not playing.
            indexPicker.setEnabled(true);
            inputField.setEnabled(true);
            inputBtn.setEnabled(true);
            randBtn.setEnabled(true);
            randAllBtn.setEnabled(true);
        }
    }

    /**
     * Plays or stops the animation.
     * @param evt Used to get the state of the toggle button
     */
    private void playBtnItemStateChanged(ItemEvent evt) {
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            speedSlider.setValue(1);
        } else {
            speedSlider.setValue(0);
        }
    }

    private void timerActionPerformed(ActionEvent evt) {
        for (int i = 0; i < 10; i++) {
            arrLabel[i].setBounds(arrLabel[i].getX(), arrLabel[i].getY()+1, 70, 30);
        }
    }

    /**
     * Get input from user and change the value in the array base on the selected index.
     */
    private void inputBtnActionPerformed(ActionEvent evt) {
        int index = indexPicker.getSelectedIndex();
        int value = Integer.parseInt(arrLabel[index].getText());

        if (!"".equals(inputField.getText())) {
            try {
                value = Integer.parseInt(inputField.getText());

                if (value >= 0) {
                    arr[index].setValue(value);
                    arrLabel[index].setText(arr[index].getValue() + "");
                    int len = arrLabel[index].getText().length();
                    arrLabel[index].setFont(new Font("Dialog", 1, 20-len));
                    inputField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this,
                    "Negative numbers are not accepted.",
                    "Invalid Input",
                    JOptionPane.WARNING_MESSAGE);
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this,
                    inputField.getText() + " is not an integer.",
                    "Invalid Input: " + e,
                    JOptionPane.ERROR_MESSAGE);
            }
            validate();
            repaint();
        }
    }

    /**
     * This function increases the index picker's value if
     * you press enter instead of clicking input.
     */
    private void increasePicker() {
        indexPicker.setSelectedIndex((indexPicker.getSelectedIndex()+1)%10);
    }

    /**
     * Randomizes the value in the array base on the selected index.
     */
    private void randBtnActionPerformed(ActionEvent evt) {
        Random rand = new Random();
        int index = indexPicker.getSelectedIndex();

        arr[index].setValue(rand.nextInt(Integer.MAX_VALUE));

        arrLabel[index].setText(arr[index].getValue() + "");
        int len = arrLabel[index].getText().length();
        arrLabel[index].setFont(new Font("Dialog", 1, 20-len));

        validate();
        repaint();
    }

    /**
     * Randomizes all the values in the array.
     */
    private void randAllBtnActionPerformed(ActionEvent evt) {
        arr = generateArray();

        for (int i = 0; i < 10; i++) {
            arrLabel[i].setText(arr[i].getValue() + "");
            int len = arrLabel[i].getText().length();
            arrLabel[i].setFont(new Font("Dialog", 1, 20-len));
        }

        orig = copyArray(arr, orig);

        validate();
        repaint();
    }

    /**
     * Test Sort
     */
    // private void testSort() {
    //     ts = new Item[10];
    //     ts = copyArray(arr, ts);
    //     quickSort(ts, 0, ts.length-1);

    //     for (Item x : orig) {
    //         System.out.print(x + ", ");
    //     }
    //     System.out.println("");

    //     for (Item x : ts) {
    //         System.out.print(x + ", ");
    //     }
    //     System.out.println("");

    //     int count = 1;
    //     for (AnimQ q : animq) {
    //         System.out.println("Pass "+ (count++) +": Pivot = " + q.getPivot());
    //         System.out.println(q.getLowList());
    //         System.out.println(q.getEqualList());
    //         System.out.println(q.getHighList());
    //     }
    // }
}
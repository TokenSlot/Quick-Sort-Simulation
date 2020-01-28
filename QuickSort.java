import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.SwingConstants;
import javax.swing.Timer;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.event.*;

public class QuickSort extends JFrame {

    private static final long serialVersionUID = 1043813624503542032L;

    private JPanel optionsPanel;
    private JPanel inputPanel, speedPanel, previewPanel;
    private JComboBox<Integer> indexPicker;
    private JTextField inputField;
    private JButton addBtn, inputBtn, randBtn, randAllBtn;
    private JSlider speedSlider;
    private JLabel speedLabel, lessLabel, equalLabel, moreLabel;

    private JPanel boardPanel;
    private JLabel[] arrLabel;

    private JPanel controlsPanel;
    private JPanel mediaPanel, infoPanel;
    private JLabel compareLabel, pivotLabel, passLabel;
    private JToggleButton playBtn;
    private JButton startBtn, rewindBtn, fastForwardBtn, endBtn;
    private JSlider passSlider;

    private Timer tm;

    private Item[] arr, orig, sorted;

    private int pass, animIndex, delay, INDEX, JNDEX;
    private int swapIterator;
    private int[] arrX;

    private Color clrA, clrB;

    private boolean isLowerDone, isHigherDone;
    private boolean isSwap, hasPair, isSwapDone, isY1Done, isY2Done, isXDone;


    // private ArrayList<Preview> previewq;
    private ArrayList<Anim> animList;

    public QuickSort() {

        try {
            // Changes the theme of the UI to nimbus
            for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
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

        setTitle("Sanic's Quickie Sort: Gotta go fast!");
        setDefaultCloseOperation(javax.swing.JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(810, 510);
        setLocationRelativeTo(null);

        optionsPanel = new JPanel(new BorderLayout());

        inputPanel = new JPanel();

        indexPicker = new JComboBox<>();
        indexPicker.setModel(new DefaultComboBoxModel<Integer>(new Integer[] {0,1,2,3,4,5,6,7,8,9}));

        inputField = new JTextField("", 16);
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputBtnActionPerformed(evt);
                increasesIndexPicker();
            }
        });

        addBtn = new JButton("Add");
        addBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputBtnActionPerformed(evt);
                increasesIndexPicker();
            }
        });

        inputBtn = new JButton("Insert");
        inputField.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                inputBtnActionPerformed(evt);
            }
        });

        randBtn = new JButton("Random");
        randBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                randBtnActionPerformed(evt);
            }
        });

        randAllBtn = new JButton("Random All");
        randAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                randAllBtnActionPerformed(evt);
            }
        });

        inputPanel.add(indexPicker);
        inputPanel.add(inputField);
        inputPanel.add(addBtn);
        inputPanel.add(inputBtn);
        inputPanel.add(randBtn);
        inputPanel.add(randAllBtn);

        speedPanel = new JPanel();

        speedLabel = new JLabel("Speed x0");

        speedSlider = new JSlider();
        speedSlider.setMaximum(5);
        speedSlider.setValue(0);
        speedSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent evt) {
                speedSliderStateChanged();
            }
        });

        speedPanel.add(speedLabel);
        speedPanel.add(speedSlider);

        previewPanel = new JPanel();

        lessLabel = new JLabel();
        equalLabel = new JLabel();
        moreLabel = new JLabel();

        previewPanel.add(lessLabel);
        previewPanel.add(equalLabel);
        previewPanel.add(moreLabel);

        optionsPanel.add(inputPanel, BorderLayout.CENTER);
        optionsPanel.add(speedPanel, BorderLayout.EAST);
        optionsPanel.add(previewPanel, BorderLayout.SOUTH);

        boardPanel = new JPanel(null);
        boardPanel.setBackground(new Color(51, 51, 51));
        boardPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        arrLabel = new JLabel[10];
        arr = new Item[10];
        arr = generateArray();

        orig = new Item[10];
        orig = copyArray(arr, orig);

        animList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            arrLabel[i] = new JLabel(arr[i].getValue() + "");
            int len = arrLabel[i].getText().length();
            arrLabel[i].setFont(new Font("Dialog", 1, 20-len));
            arrLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            arrLabel[i].setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            arrLabel[i].setBackground(new Color(152, 152, 152));
            arrLabel[i].setForeground(new Color(0, 0, 0));
            arrLabel[i].setOpaque(true);
            arrLabel[i].setBounds(55+(70*i), 110, 70, 100);
            boardPanel.add(arrLabel[i]);
        }

        controlsPanel = new JPanel(new BorderLayout());

        infoPanel = new JPanel(new BorderLayout());
        compareLabel = new JLabel("-");
        compareLabel.setHorizontalAlignment(SwingConstants.CENTER);
        pivotLabel = new JLabel("-");
        pivotLabel.setHorizontalAlignment(SwingConstants.CENTER);
        passLabel = new JLabel("-");
        passLabel.setHorizontalAlignment(SwingConstants.CENTER);

        infoPanel.add(compareLabel, BorderLayout.NORTH);
        infoPanel.add(pivotLabel, BorderLayout.CENTER);
        infoPanel.add(passLabel, BorderLayout.SOUTH);

        mediaPanel = new JPanel();

        startBtn = new JButton("|<");

        rewindBtn = new JButton("<<");

        playBtn = new JToggleButton("I>");
        playBtn.addItemListener(new ItemListener() {
        @Override
        public void itemStateChanged(ItemEvent evt) {
                playBtnItemStateChanged(evt);
            }
        });

        fastForwardBtn = new JButton(">>");

        endBtn = new JButton(">|");

        mediaPanel.add(startBtn);
        mediaPanel.add(rewindBtn);
        mediaPanel.add(playBtn);
        mediaPanel.add(fastForwardBtn);
        mediaPanel.add(endBtn);

        passSlider = new JSlider();

        controlsPanel.add(infoPanel, BorderLayout.NORTH);
        controlsPanel.add(mediaPanel, BorderLayout.WEST);
        controlsPanel.add(passSlider, BorderLayout.CENTER);

        add(optionsPanel, BorderLayout.NORTH);
        add(boardPanel, BorderLayout.CENTER);
        add(controlsPanel, BorderLayout.SOUTH);

        animIndex = -1;
        pass = 0;

        INDEX = 0;
        JNDEX = 0;

        swapIterator = 0;

        clrA = new Color(102,102,255);
        clrB = new Color(255,102,102);

        isLowerDone = false;
        isHigherDone = false;

        isSwap = false;
        isSwapDone = false;
        isY1Done = false;
        isY2Done = false;
        isXDone = false;

        arrX = new int[10];
        for (int i = 0; i < 10; i++) {
            arrX[i] = 55+(70*i);
        }

        tm = new Timer(0, new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                timerActionPerformed(evt);
            }
        });
        generateSortedArray();
    }

    /**
     * Resets the animation back to its original state
     */
    private void resetAnimation() {
        pass = 0;
        for (int i = 0; i < 10; i++) {
            arrLabel[i].setText(orig[i].getValue() + "");
            int len = arrLabel[i].getText().length();
            arrLabel[i].setFont(new Font("Dialog", 1, 20-len));
            arrLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            arrLabel[i].setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            arrLabel[i].setBackground(new Color(152, 152, 152));
            arrLabel[i].setForeground(new Color(0, 0, 0));
            arrLabel[i].setOpaque(true);
            arrLabel[i].setBounds(55+(70*i), 110, 70, 100);
        }
    }

    /**
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

        animList.get(animIndex).setLow(i);
        animList.get(animIndex).setHigh(j);
        // Item pivotItem = arr[pi];
        animList.get(animIndex).setPivot(arr[pi]);
        while (true) {

            do {
                i++;
                animList.get(animIndex).addI_check(arr[i]);
            } while (arr[i].getValue() < pivot);

            do {
                j--;
                animList.get(animIndex).addJ_check(arr[j]);
            } while (arr[j].getValue() > pivot);

            if (i >= j) {
                return j;
            }

            animList.get(animIndex).addSwap(arr[i], arr[j]);
            // Item temp = new Item(arr[i].getIndex(), arr[i].getValue());
            // arr[i] = arr[j];
            // arr[j] = temp;
            int temp = arr[i].getValue();
            arr[i].setValue(arr[j].getValue());
            arr[j].setValue(temp);
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
            animIndex++;
            animList.add(new Anim());
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
        if (speedSlider.getValue() > 0) {
            playBtn.setText("||");
            playBtn.setSelected(true);

            // Resets the input forms and disables the option to change
            // the values of the array when the animation is playing.
            indexPicker.setEnabled(false);
            inputField.setEnabled(false);
            addBtn.setEnabled(false);
            inputBtn.setEnabled(false);
            randBtn.setEnabled(false);
            randAllBtn.setEnabled(false);
            indexPicker.setSelectedIndex(0);
            inputField.setText("");

            switch(speedSlider.getValue()) {
                case 1:
                    delay = 700;
                    speedLabel.setText("Speed: x0.5");
                    break;
                case 2:
                    delay = 500;
                    speedLabel.setText("Speed: x1");
                    break;
                case 3:
                    delay = 300;
                    speedLabel.setText("Speed: x1.5");
                    break;
                case 4:
                    delay = 100;
                    speedLabel.setText("Speed: x2");
                    break;
            }

            tm.setDelay(delay);
            tm.start();

            for (int i = 0; i < animList.size(); i++) {
                Anim anim = animList.get(i);
                System.out.println("PASS " + (i+1));
                System.out.println(anim.getI_checks());
                System.out.println(anim.getJ_checks());
                System.out.println(anim.getSwapped());
            }

        } else if (speedSlider.getValue() == 0) {
            speedLabel.setText("Speed: x0");
            playBtn.setText("I>");
            playBtn.setSelected(false);

            tm.stop();

            // Enables the option to change the values
            // of the array when the animation is not playing.
            indexPicker.setEnabled(true);
            addBtn.setEnabled(true);
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
            speedSlider.setValue(2);
        } else {
            speedSlider.setValue(0);
        }
    }

    /**
     * Starts the animation
     * @param evt
     */
    private void timerActionPerformed(ActionEvent evt) {
        animate();
    }

    /**
     * Checks if the item has a paired item in a Array of ItemPairs
     * @param x
     * @return
     */
    public boolean hasPair(Item x, ArrayList<ItemPair> pairs) {
        for (ItemPair p : pairs) {
            if (p.getA() == x) {
                return true;
            }
        }
        return false;
    }

    /**
     * The main animation function
     */
    private void animate() {
        //pass++;
        if (pass < animList.size()) {
            passLabel.setText("Pass " + (pass+1));
            Anim anim = animList.get(pass);
            Item pivot = anim.getPivot();
            int pi = pivot.getIndex();
            arrLabel[pi].setBackground(new Color(51, 153, 0));

            tm.setDelay(delay);

            if (!isSwap) {
                Item A = anim.getI_checks().get(INDEX);
                if (hasPair(A, anim.getSwapped())) {

                }
            } else {
                tm.setDelay(delay/100);

                if (isSwapDone) {
                    isSwapDone = false;
                    isSwap = false;
                    isY1Done = false;
                    isXDone = false;
                    isY2Done = false;
                    swapIterator += 1;
                } else {
                    Item A = anim.getSwapped().get(swapIterator).getA();
                    Item B = anim.getSwapped().get(swapIterator).getB();
                    int A_in = A.getIndex();
                    int B_in = B.getIndex();
                    int x_i = arrLabel[A_in].getX();
                    int y_i = arrLabel[A_in].getY();
                    int x_j = arrLabel[B_in].getX();
                    int y_j = arrLabel[B_in].getY();
                    if (!isY1Done) {
                        if (arrLabel[A_in].getY() > 10 || arrLabel[B_in].getY() < 210) {
                            if (arrLabel[A_in].getY() > 10) arrLabel[A_in].setBounds(x_i, y_i-1, 70, 100);
                            if (arrLabel[B_in].getY() < 210) arrLabel[B_in].setBounds(x_j, y_j+1, 70, 100);
                        } else {
                            isY1Done = true;
                        }
                    } else {
                        if (!isXDone) {
                            if (arrLabel[A_in].getX() < arrX[B_in] || arrLabel[B_in].getX() > arrX[A_in]) {
                                if (arrLabel[A_in].getX() < arrX[B_in]) arrLabel[A_in].setBounds(x_i+1, y_i, 70, 100);
                                if (arrLabel[B_in].getX() > arrX[A_in]) arrLabel[B_in].setBounds(x_j-1, y_j, 70, 100);
                            } else {
                                isXDone = true;
                            }
                        } else {
                            if (!isY2Done) {
                                if (arrLabel[A_in].getY() < 110 || arrLabel[B_in].getY() > 110) {
                                    if (arrLabel[A_in].getY() < 110) arrLabel[A_in].setBounds(x_i, y_i+1, 70, 100);
                                    if (arrLabel[B_in].getY() > 110) arrLabel[B_in].setBounds(x_j, y_j-1, 70, 100);
                                } else {
                                    arrLabel[A_in].setBackground(clrB);
                                    arrLabel[B_in].setBackground(clrA);
                                    JLabel tempLabel = arrLabel[A_in];
                                    arrLabel[A_in] = arrLabel[B_in];
                                    arrLabel[B_in] = tempLabel;
                                    isY2Done = true;
                                    isSwapDone = true;
                                }
                            }
                        }
                    }
                }
            }

        } else {
            for (int i = 0; i < 10; i++) {
                arrLabel[i].setBackground(new Color(152, 152, 152));
            }
            speedSlider.setValue(0);
        }
        // if (pass < animList.size()) {
        //     passLabel.setText("Pass " + (pass+1));
        //     Anim q = animList.get(pass);
        //     Item pivot = q.getPivot();
        //     int pi = pivot.getIndex();
        //     arrLabel[pi].setBackground(new Color(51, 153, 0));

        //     tm.setDelay(delay);

        //     if (isSwap == false) {
        //         if (A_in == q.getLow() || Integer.parseInt(arrLabel[A_in].getText()) < pivot.getValue()) {
        //             A_in++;
        //             arrLabel[A_in].setBackground(clrA);
        //         }

        //         if (B_in == q.getHigh() || Integer.parseInt(arrLabel[B_in].getText()) > pivot.getValue()) {
        //             B_in--;
        //             arrLabel[B_in].setBackground(clrB);
        //             isSwap = false;
        //         } else {
        //             isSwap = true;
        //         }

        //         if (A_in >= B_in) {
        //             for (int i = 0; i < 10; i++) {
        //                 arrLabel[i].setBackground(new Color(152, 152, 152));
        //             }
        //             A_in = q.getLow();
        //             B_in = q.getHigh();
        //             pass++;
        //         }
        //     } else {
        //         tm.setDelay(delay/100);

        //         if (isSwapDone) {
        //             isSwap = false;
        //             isY1Done = false;
        //             isXDone = false;
        //             isY2Done = false;
        //         } else {
        //             int x_i = arrLabel[A_in].getX();
        //             int y_i = arrLabel[A_in].getY();
        //             int x_j = arrLabel[B_in].getX();
        //             int y_j = arrLabel[B_in].getY();
        //             if (isY1Done == false) {
        //                 if (arrLabel[A_in].getY() > 10 || arrLabel[B_in].getY() < 210) {
        //                     if (arrLabel[A_in].getY() > 10) arrLabel[A_in].setBounds(x_i, y_i-1, 70, 100);
        //                     if (arrLabel[B_in].getY() < 210) arrLabel[B_in].setBounds(x_j, y_j+1, 70, 100);
        //                 } else {
        //                     isY1Done = true;
        //                 }
        //             } else {
        //                 if (isXDone == false) {
        //                     if (arrLabel[A_in].getX() < arrX[B_in] || arrLabel[B_in].getX() > arrX[A_in]) {
        //                         if (arrLabel[A_in].getX() < arrX[B_in]) arrLabel[A_in].setBounds(x_i+1, y_i, 70, 100);
        //                         if (arrLabel[B_in].getX() > arrX[A_in]) arrLabel[B_in].setBounds(x_j-1, y_j, 70, 100);
        //                     } else {
        //                         isXDone = true;
        //                     }
        //                 } else {
        //                     if (isY2Done == false) {
        //                         if (arrLabel[A_in].getY() < 110 || arrLabel[B_in].getY() > 110) {
        //                             if (arrLabel[A_in].getY() < 110) arrLabel[A_in].setBounds(x_i, y_i+1, 70, 100);
        //                             if (arrLabel[B_in].getY() > 110) arrLabel[B_in].setBounds(x_j, y_j-1, 70, 100);
        //                         } else {
        //                             JLabel tempLabel = arrLabel[A_in];
        //                             arrLabel[A_in] = arrLabel[B_in];
        //                             arrLabel[B_in] = tempLabel;
        //                             isY2Done = true;
        //                             isSwapDone = true;
        //                         }
        //                     }
        //                 }
        //             }
        //         }
        //     }
        // } else {
        //     for (int i = 0; i < 10; i++) {
        //         arrLabel[i].setBackground(new Color(152, 152, 152));
        //     }
        //     speedSlider.setValue(0);
        // }
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
                    orig[index].setValue(value);
                    arrLabel[index].setText(arr[index].getValue() + "");
                    int len = arrLabel[index].getText().length();
                    arrLabel[index].setFont(new Font("Dialog", 1, 20-len));
                    inputField.setText("");
                    animIndex = -1;
                    generateSortedArray();
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
     * This function increases the index picker's value by 1 or resets it to 0 if its 9.
     */
    private void increasesIndexPicker() {
        indexPicker.setSelectedIndex((indexPicker.getSelectedIndex()+1)%10);
    }


    /**
     * Randomizes the value in the array base on the selected index.
     */
    private void randBtnActionPerformed(ActionEvent evt) {
        Random rand = new Random();
        int index = indexPicker.getSelectedIndex();

        int value = rand.nextInt(Integer.MAX_VALUE);
        arr[index].setValue(value);
        orig[index].setValue(value);

        arrLabel[index].setText(arr[index].getValue() + "");
        int len = arrLabel[index].getText().length();
        arrLabel[index].setFont(new Font("Dialog", 1, 20-len));

        animIndex = -1;
        generateSortedArray();
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

        animIndex = -1;
        generateSortedArray();
        validate();
        repaint();
    }

    /**
     * This function will generate the sorted array
     */
    private void generateSortedArray() {
        animList.clear();
        sorted = new Item[10];
        sorted = copyArray(arr, sorted);
        quickSort(sorted, 0, sorted.length-1);
        resetAnimation();
    }
}
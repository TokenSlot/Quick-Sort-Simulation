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
    private JButton startBtn, rewindBtn, playBtn, fastForwardBtn, endBtn;
    private JSlider passSlider;

    private Timer tm;

    private Item[] arr;
    private Item[] orig; // used for resetting the ui
    private Item[] sorted;

    private ArrayList<Preview> previewq;

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
        speedSlider.setInverted(true);
        speedSlider.addChangeListener(new ChangeListener(){
            @Override
            public void stateChanged(ChangeEvent evt) {
                speedSliderStateChanged();
            }
        });

        speedPanel.add(speedLabel);
        speedPanel.add(speedSlider);

        previewPanel = new JPanel();

        previewq = new ArrayList<>();

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
        boardPanel.setBackground(new java.awt.Color(51, 51, 51));
        boardPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

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
            arrLabel[i].setBackground(new java.awt.Color(153, 153, 153));
            arrLabel[i].setForeground(new java.awt.Color(0, 0, 0));
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

        playBtn = new JButton("I>");
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
    }

    /**
     * Takes the middle element as pivot, places it the right position
     * places smaller values to the left of the pivot and adds them to
     * a an previewq's low list and all equal or greater values are placed
     * to the right of the pivot but equal values are added to an previewq's
     * equal list and the greater values are added to the previewq's high list.
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

        Preview q = new Preview(arr, low, high, pi);
        previewq.add(q);
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
            playBtn.setText("||");
            playBtn.setSelected(true);

            // tm.setDelay(speedSlider.getValue());
            // tm.start();

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
        } else if (speedSlider.getValue() == 0) {
            playBtn.setText("I>");
            playBtn.setSelected(false);

            // tm.stop();

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
                    orig[index].setValue(value);
                    arrLabel[index].setText(arr[index].getValue() + "");
                    int len = arrLabel[index].getText().length();
                    arrLabel[index].setFont(new Font("Dialog", 1, 20-len));
                    inputField.setText("");
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

            previewq.clear();
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

        generateSortedArray();
        previewq.clear();
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

        generateSortedArray();
        previewq.clear();
        validate();
        repaint();
    }

    /**
     * This function will generate the sorted array
     */
    private void generateSortedArray() {
        sorted = new Item[10];
        sorted = copyArray(arr, sorted);
        quickSort(sorted, 0, sorted.length-1);

        for (Item x : orig) {
            System.out.print(x + ", ");
        }

        System.out.println("");

        for (Item x : sorted) {
            System.out.print(x + ", ");
        }
        System.out.println("");

        int count = 1;
        for (Preview q : previewq) {
            System.out.println("Pass "+ (count++) +": Pivot = " + q.getPivot());
            System.out.println(q.getLowList());
            System.out.println(q.getEqualList());
            System.out.println(q.getHighList());
        }
    }
}
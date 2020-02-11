import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

import javax.swing.BorderFactory;
import javax.swing.JButton;
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
    private JButton editBtn;
    private JSlider speedSlider;
    private JLabel speedLabel, indexLabel;

    private JPanel boardPanel;
    private JLabel[] arrIndexLabel;
    private JTextField[] arrTextField;
    private JButton[] randBtn;
    private JLabel minLabel, maxLabel;
    private JTextField minTextField, maxTextField;
    private JButton randAllBtn, saveBtn, cancelBtn;
    private JLabel[] arrLabel;


    private JPanel controlsPanel;
    private JPanel mediaPanel, infoPanel;
    private JLabel compareLabel, pivotLabel, passLabel;
    private JToggleButton playBtn;
    private JButton startBtn, rewindBtn, fastForwardBtn, endBtn;
    private JSlider passSlider;

    private Timer tm;

    private Item[] arr, orig, sorted;

    private int pass, pivotIndex, animIndex, delay, INDEX, JNDEX;
    private int swapIterator;
    private int[] arrX;

    private Color clrA, clrB;

    // Animation flags
    private boolean isPivotSwap, isPivotSwapping, isSwap, isSwapDone, isY1Done, isY2Done, isXDone;

    // Saving flag
    private boolean areTextFieldsValid;

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
            // java.net.URL url = ClassLoader.getSystemResource("resources/ico.png");
            // Toolkit kit = Toolkit.getDefaultToolkit();
            // Image img = kit.createImage(url);
            // setIconImage(img);
        } catch (Exception e) {}

        setTitle("Sanic's Quickie Sort: Gotta go fast!");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        setResizable(false);
        setSize(810, 530);
        setLocationRelativeTo(null);

        optionsPanel = new JPanel(new BorderLayout());

        inputPanel = new JPanel();

        editBtn = new JButton("Edit Mode");
        editBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                editBtnActionPerformed();
            }
        });

        inputPanel.add(editBtn);

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

        indexLabel = new JLabel("-");

        previewPanel.add(indexLabel);

        optionsPanel.add(inputPanel, BorderLayout.WEST);
        optionsPanel.add(speedPanel, BorderLayout.CENTER);
        optionsPanel.add(previewPanel, BorderLayout.SOUTH);

        boardPanel = new JPanel(null);
        boardPanel.setBackground(new Color(51, 51, 51));
        boardPanel.setBorder(javax.swing.BorderFactory.createEtchedBorder());

        // Edit Mode
        arrIndexLabel = new JLabel[10];
        arrTextField = new JTextField[10];
        randBtn = new JButton[10];

        for (int i = 0; i < 10; i++) {
            arrIndexLabel[i] = new JLabel(i + "");
            arrIndexLabel[i].setFont(new Font("Dialog", 1, 48));
            arrIndexLabel[i].setForeground(new Color(102, 102, 102));
            arrIndexLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            arrIndexLabel[i].setBounds(55+(70*i), 40, 70, 62);
            arrIndexLabel[i].setVisible(false);

            arrTextField[i] = new JTextField();
            arrTextField[i].setBounds(55+(70*i), 110, 70, 100);
            arrTextField[i].setFont(new Font("Dialog", 1, 10));
            arrTextField[i].setVisible(false);

            randBtn[i] = new JButton("Random");
            randBtn[i].setFont(new Font("Dialog", 1, 10));
            randBtn[i].setBounds(55+(70*i), 210, 70, 30);
            randBtn[i].setVisible(false);
            int randBtnIndex = i;
            randBtn[i].addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    if (checkMinMax()) {
                        randBtnActionPerformed(randBtnIndex);
                    }
                }
            });

            boardPanel.add(arrIndexLabel[i]);
            boardPanel.add(arrTextField[i]);
            boardPanel.add(randBtn[i]);
        }

        minLabel = new JLabel("Min");
        minLabel.setForeground(new Color(255, 255, 255));
        minLabel.setBounds(50, 250, 30, 24);
        minLabel.setVisible(false);

        minTextField = new JTextField();
        minTextField.setBounds(90, 250, 100, 24);
        minTextField.setVisible(false);

        maxLabel = new JLabel("Max");
        maxLabel.setForeground(new Color(255, 255, 255));
        maxLabel.setBounds(50, 280, 30, 24);
        maxLabel.setVisible(false);

        maxTextField = new JTextField();
        maxTextField.setBounds(90, 280, 100, 24);
        maxTextField.setVisible(false);

        randAllBtn = new JButton("Random All");
        randAllBtn.setBounds(50, 310, 140, 32);
        randAllBtn.setVisible(false);
        randAllBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                if (checkMinMax()) {
                    randAllBtnActionPerformed(evt);
                }
            }
        });

        saveBtn = new JButton("Save");
        saveBtn.setBounds(320, 300, 80, 32);
        saveBtn.setVisible(false);
        saveBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                saveBtnActionPerformed();
            }
        });

        cancelBtn = new JButton("Cancel");
        cancelBtn.setBounds(400, 300, 80, 32);
        cancelBtn.setVisible(false);
        cancelBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt) {
                playModeActivate();
            }
        });

        boardPanel.add(minLabel);
        boardPanel.add(maxLabel);
        boardPanel.add(minTextField);
        boardPanel.add(maxTextField);
        boardPanel.add(randAllBtn);
        boardPanel.add(saveBtn);
        boardPanel.add(cancelBtn);

        //Play Mode
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

            arrTextField[i].setText(arr[i].getValue() + "");
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

        pivotIndex = 0;
        swapIterator = 0;

        clrA = new Color(102,102,255);
        clrB = new Color(255,102,102);

        isPivotSwap = false;
        isPivotSwapping = false;
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
        animIndex = -1;
        pass = 0;
        INDEX = 0;
        JNDEX = 0;
        pivotIndex = 0;
        swapIterator = 0;
        clrA = new Color(102,102,255);
        clrB = new Color(255,102,102);
        isPivotSwap = false;
        isPivotSwapping = false;
        isSwap = false;
        isSwapDone = false;
        isY1Done = false;
        isY2Done = false;
        isXDone = false;

        compareLabel.setText("-");
        pivotLabel.setText("-");
        passLabel.setText("-");

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
        animList.get(animIndex).setPivot(arr[pi]);

        System.out.println("Pivot = " + pivot + " at index " + pi);
        while (true) {

            do {
                i++;
                animList.get(animIndex).addI_check(arr[i]);
                System.out.println("Check i = " + i + ": " + arr[i]);
            } while (arr[i].getValue() < pivot);

            do {
                j--;
                animList.get(animIndex).addJ_check(arr[j]);
                System.out.println("Check j = " + j + ": " + arr[j]);
            } while (arr[j].getValue() > pivot);

            if (i >= j) {
                return j;
            }

            System.out.println("Swap: " + arr[i] + " at index " + i + ", " + arr[j] + " at index " + j);
            animList.get(animIndex).addSwap(arr[i], arr[j]);
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
            animList.get(animIndex).addPasses(this.arr);
            System.out.println("\nPASS " + animIndex + " (" + low + "-" + high + ")" + "\n");
            int pi = partition(arr, low, high);

            quickSort(arr, low, pi);
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

            editBtn.setEnabled(false);

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

            editBtn.setEnabled(true);

            tm.stop();
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
     * The main animation function
     */
    private void animate() {
        if (pass < animList.size()) {
            passLabel.setText("Pass " + (pass+1));
            Anim anim = animList.get(pass);
            Item pivot = anim.getPivot();
            if (!isPivotSwap)
                pivotIndex = pivot.getIndex();
            int pi = pivotIndex;

            if (!isPivotSwapping)
                arrLabel[pi].setBackground(new Color(51, 153, 0));
            pivotLabel.setText("Pivot: " + arrLabel[pi].getText());
            indexLabel.setText("Index: " + (anim.getLow()+1) + " to " + (anim.getHigh()-1));

            tm.setDelay(delay);
            System.out.println(delay);

            if (!isSwap) {
                if (INDEX < anim.getI_checks().size() && JNDEX < anim.getJ_checks().size()) {
                    if (INDEX < anim.getI_checks().size()) {
                        Item A = anim.getI_checks().get(INDEX);
                        Item A_target = null;
                        if (swapIterator < anim.getSwapped().size())
                            A_target = anim.getSwapped().get(swapIterator).getA();
                        int A_in = A.getIndex();
                        arrLabel[A_in].setBackground(clrA);
                        compareLabel.setText(arrLabel[A_in].getText() + " < " + arrLabel[pi].getText());
                        if (A == A_target) {
                            if (JNDEX < anim.getJ_checks().size()) {
                                Item B = anim.getJ_checks().get(JNDEX);
                                Item B_target = null;
                                if (swapIterator < anim.getSwapped().size())
                                    B_target = anim.getSwapped().get(swapIterator).getB();
                                int B_in = B.getIndex();
                                arrLabel[B_in].setBackground(clrB);
                                compareLabel.setText(arrLabel[B_in].getText() + " > " + arrLabel[pi].getText());
                                if (B == B_target) {
                                    isSwap = true;
                                } else {
                                    JNDEX++;
                                }
                            }
                        } else {
                            INDEX++;
                        }
                    }
                } else {
                    for (int i = 0; i < 10; i++) {
                        arrLabel[i].setBackground(new Color(152, 152, 152));
                    }
                    pass++;
                    INDEX = 0;
                    JNDEX = 0;
                    swapIterator = 0;
                    isPivotSwap = false;
                    indexLabel.setText("-");
                    compareLabel.setText("-");
                    pivotLabel.setText("-");
                }
            } else {
                tm.setDelay(delay/100);

                if (isSwapDone) {
                    isSwapDone = false;
                    isSwap = false;
                    isY1Done = false;
                    isXDone = false;
                    isY2Done = false;
                    isPivotSwapping = false;
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

                    if (pivot == A) {
                        pivotIndex = B.getIndex();
                        isPivotSwap = true;
                        isPivotSwapping = true;
                    } else if (pivot == B) {
                        pivotIndex = A.getIndex();
                        isPivotSwap = true;
                        isPivotSwapping = true;
                    }

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
    }

    private void editBtnActionPerformed() {
        resetAnimation();

        for (int i = 0; i < 10; i++) {
            arrTextField[i].setText(orig[i].getValue() + "");
        }

        for (int i = 0; i < 10; i++) {
            arrIndexLabel[i].setVisible(true);
            arrTextField[i].setVisible(true);
            randBtn[i].setVisible(true);

            arrLabel[i].setVisible(false);
        }

        minLabel.setVisible(true);
        maxLabel.setVisible(true);
        minTextField.setVisible(true);
        maxTextField.setVisible(true);
        randAllBtn.setVisible(true);
        saveBtn.setVisible(true);
        cancelBtn.setVisible(true);

        editBtn.setEnabled(false);
        speedSlider.setEnabled(false);
        startBtn.setEnabled(false);
        rewindBtn.setEnabled(false);
        playBtn.setEnabled(false);
        fastForwardBtn.setEnabled(false);
        endBtn.setEnabled(false);
        passSlider.setEnabled(false);
    }


    private boolean checkMinMax() {
        try {
            int min = 0, max = Integer.MAX_VALUE;

            if (!"".equals(minTextField.getText())) {
                min = Integer.parseInt(minTextField.getText());
            }

            if (!"".equals(maxTextField.getText())) {
                max = Integer.parseInt(maxTextField.getText());
            }

            if (min > max) {
                JOptionPane.showMessageDialog(this, "Min is bigger than Max", "Warning", JOptionPane.WARNING_MESSAGE);
                return false;
            } else {
                return true;
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e, "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }

    }

    /**
     * Randomizes the value in the array base on the selected index.
     */
    private void randBtnActionPerformed(int index) {
        Random rand = new Random();
        int min = 0, max = Integer.MAX_VALUE;

        if (!"".equals(minTextField.getText())) {
            min = Integer.parseInt(minTextField.getText());
        }

        if (!"".equals(maxTextField.getText())) {
            max = Integer.parseInt(maxTextField.getText());
        }

        int value = rand.nextInt((max - min) + 1) + min;

        arrTextField[index].setText(value + "");
    }

    /**
     * Randomizes all the values in the array.
     */
    private void randAllBtnActionPerformed(ActionEvent evt) {
        Random rand = new Random();
        int min = 0, max = Integer.MAX_VALUE;

        if (!"".equals(minTextField.getText())) {
            min = Integer.parseInt(minTextField.getText());
        }

        if (!"".equals(maxTextField.getText())) {
            max = Integer.parseInt(maxTextField.getText());
        }
        for (int i = 0; i < 10; i++) {
            int value = rand.nextInt((max - min) + 1) + min;
            arrTextField[i].setText(value + "");
        }
    }

    /**
     * Saves the inputs into the new array
     */
    private void saveBtnActionPerformed() {

        areTextFieldsValid = true;

        for (int i = 0; i < 10; i++) {
            if ("".equals(arrTextField[i].getText())) {
                areTextFieldsValid = false;
            } else {
                try {
                    Integer.parseInt(arrTextField[i].getText());
                } catch (NumberFormatException e) {
                    areTextFieldsValid = false;
                }
            }
        }

        if (areTextFieldsValid) {
            int confirmation = JOptionPane.showConfirmDialog(this, "Are you sure?");

            if (confirmation == JOptionPane.YES_OPTION) {
                for (int i = 0; i < 10; i++) {
                    int value = Integer.parseInt(arrTextField[i].getText());
                    arr[i].setValue(value);
                    orig[i].setValue(value);
                }
                playModeActivate();
            }

        } else {
            JOptionPane.showMessageDialog(this, "Some inputs are invalid.\nYou can only input 0 to Max Integer", "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * Switches to play mode
     */
    private void playModeActivate() {
        generateSortedArray();

        for (int i = 0; i < 10; i++) {
            arrIndexLabel[i].setVisible(false);
            arrTextField[i].setVisible(false);
            randBtn[i].setVisible(false);

            arrLabel[i].setVisible(true);
        }

        minLabel.setVisible(false);
        maxLabel.setVisible(false);
        minTextField.setVisible(false);
        maxTextField.setVisible(false);
        randAllBtn.setVisible(false);
        saveBtn.setVisible(false);
        cancelBtn.setVisible(false);

        editBtn.setEnabled(true);
        speedSlider.setEnabled(true);
        startBtn.setEnabled(true);
        rewindBtn.setEnabled(true);
        playBtn.setEnabled(true);
        fastForwardBtn.setEnabled(true);
        endBtn.setEnabled(true);
        passSlider.setEnabled(true);
    }

    /**
     * This function will generate the sorted array
     */
    private void generateSortedArray() {
        animList.clear();
        sorted = new Item[10];
        sorted = copyArray(arr, sorted);
        quickSort(sorted, 0, sorted.length-1);
        passSlider.setMinimum(0);
        passSlider.setMaximum(animList.size());
        resetAnimation();
    }
}
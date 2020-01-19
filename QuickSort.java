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

    private int[] arr;

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

        indexLabel = new JLabel[10];

        for (int i = 0; i < 10; i++) {
            indexLabel[i] = new JLabel(i + "");
            indexLabel[i].setFont(new Font("Dialog", 1, 10));
            indexLabel[i].setHorizontalAlignment(SwingConstants.CENTER);
            indexLabel[i].setBorder(BorderFactory.createLineBorder(new Color(0,0,0)));
            indexLabel[i].setBounds(10+(80*i), 10, 70, 30);
            add(indexLabel[i]);
        }

        arrLabel = new JLabel[10];
        arr = new int[10];
        generateArray(arr);

        for (int i = 0; i < 10; i++) {
            arrLabel[i] = new JLabel(arr[i] + "");
            arrLabel[i].setFont(new Font("Dialog", 1, 10));
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
     * Initializes the array containing the values to be sorted
     * @param arr array to be sorted
     */
    private void generateArray(int[] arr) {
        Random rand = new Random();
        for (int i = 0; i < arr.length; i++) {
            arr[i] = rand.nextInt(Integer.MAX_VALUE);
        }
    }

    /**
     * Controls the speed of the animation
     */
    private void speedSliderStateChanged() {
        speedLabel.setText("Speed: x" + speedSlider.getValue());
        if (speedSlider.getValue() > 0) {
            playBtn.setText("Stop");
            playBtn.setSelected(true);

            tm.setDelay(speedSlider.getValue());
            tm.start();

            // Resets the input forms and disables the option
            // to change the values of the array when
            // the animation is playing
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
     * Plays or stops the animation
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
        arrLabel[0].setBounds(arrLabel[0].getX(), arrLabel[0].getY()+1, 70, 30);
    }

    /**
     * Get input from user and change the value in the array base on the selected index
     */
    private void inputBtnActionPerformed(ActionEvent evt) {
        int index = indexPicker.getSelectedIndex();
        int value = Integer.parseInt(arrLabel[index].getText());

        if (!"".equals(inputField.getText())) {
            try {
                value = Integer.parseInt(inputField.getText());

                if (value >= 0) {
                    arr[index] = value;
                    arrLabel[index].setText(arr[index] + "");
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
     * Randomizes the value in the array base on the selected index
     */
    private void randBtnActionPerformed(ActionEvent evt) {
        Random rand = new Random();
        int index = indexPicker.getSelectedIndex();

        arr[index] = rand.nextInt(Integer.MAX_VALUE);
        arrLabel[index].setText(arr[index] + "");

        validate();
        repaint();
    }

    /**
     * Randomizes all the values in the array
     */
    private void randAllBtnActionPerformed(ActionEvent evt) {
        generateArray(arr);

        for (int i = 0; i < 10; i++) {
            arrLabel[i].setText(arr[i] + "");
        }

        validate();
        repaint();
    }
}
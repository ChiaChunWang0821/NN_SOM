import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.geom.Line2D;
import java.io.File;
import java.util.ArrayList;

public class NeuralNetworkJFrame {

    private DataSet dataSet;

    private int windowWidth;
    private int windowHeight;
    private int drawPanelSize; // width
    private int scaleRatio = 50;

    private Color lineColor = Color.RED;
    private Color[] pointColor = {Color.MAGENTA, Color.GREEN, Color.BLUE, Color.ORANGE, Color.CYAN};

    private ArrayList<Double> weightPoints = new ArrayList<>();

    private JFrame neuralNetworkFrame;
    private JPanel neuralNetworkPanel;
    private JPanel controlPanel;
    private JPanel showPanel;
    private JPanel drawPanel;

    private JButton loadFileButton;
    private JButton showResultButton;
    private JButton largeButton;
    private JButton smallButton;

    private JLabel filePathLabel;

    private JLabel numOfNeuronRowLabel;
    private JLabel numOfNeuronColumnLabel;

    private JTextField numOfNeuronRowTextField;
    private JTextField numOfNeuronColumnTextField;

    private JLabel dimensionLabel;
    private JLabel setLabel;
    private JLabel weightLabel;
    private JLabel testingRateLabel;

    private JLabel dimensionValueLabel;
    private JLabel setValueLabel;
    private JLabel weightValueLabel;
    private JLabel testingRateValueLabel;


    public NeuralNetworkJFrame(){
        windowWidth = Toolkit.getDefaultToolkit().getScreenSize().width;
        windowHeight = Toolkit.getDefaultToolkit().getScreenSize().height;

        neuralNetworkFrame = new JFrame("Neural Network");
        neuralNetworkFrame.setContentPane(neuralNetworkPanel);
        neuralNetworkFrame.setVisible(true);
        neuralNetworkFrame.setSize(windowWidth - 100, windowHeight - 100);
        neuralNetworkFrame.setLocationRelativeTo(null);
        neuralNetworkFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    protected void actionListener(OnNeuralNetworkCallback onNeuralNetworkCallback){
        numOfNeuronRowTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            private void changed(){
                try {
                    numOfNeuronRowTextField.setBackground(Color.white);
                    onNeuralNetworkCallback.getValueCallback(NeuralNetwork.NUMBER_OF_NEURONS_ROW, numOfNeuronRowTextField.getText());
                } catch (NumberFormatException e){
                    numOfNeuronRowTextField.setBackground(Color.pink);
                    onNeuralNetworkCallback.getValueCallback(NeuralNetwork.NUMBER_OF_NEURONS_ROW, "3");
                }
            }
        });

        numOfNeuronColumnTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                changed();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                changed();
            }

            private void changed(){
                try {
                    numOfNeuronColumnTextField.setBackground(Color.white);
                    onNeuralNetworkCallback.getValueCallback(NeuralNetwork.NUMBER_OF_NEURONS_COLUMN, numOfNeuronColumnTextField.getText());
                } catch (NumberFormatException e){
                    numOfNeuronColumnTextField.setBackground(Color.pink);
                    onNeuralNetworkCallback.getValueCallback(NeuralNetwork.NUMBER_OF_NEURONS_COLUMN, "3");
                }
            }
        });

        loadFileButton.addActionListener(e -> {
            JFileChooser jFileChooser = new JFileChooser();
            FileNameExtensionFilter fileNameExtensionFilter = new FileNameExtensionFilter("Text Files(*.txt)", "txt", "text");
            jFileChooser.setFileFilter(fileNameExtensionFilter);

            if(jFileChooser.showOpenDialog(neuralNetworkFrame) == JFileChooser.APPROVE_OPTION){
                File file = jFileChooser.getSelectedFile();

                filePathLabel.setForeground(null);
                filePathLabel.setText(file.getPath());

                dataSet = DataPreprocessor.splitData(DataPreprocessor.readFile(file));

                dimensionValueLabel.setText(Integer.toString(dataSet.dimension));
                setValueLabel.setText(dataSet.trainingSet + "/ " + dataSet.testingSet);
            }

            if(dataSet.trainingSet != 0){
                showResultButton.setEnabled(true);

                onNeuralNetworkCallback.dataReadyCallback(dataSet);

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });

        showResultButton.addActionListener(e -> {
            onNeuralNetworkCallback.showResultCallback();

            drawPanel.removeAll();
            drawPanel.repaint();
        });

        largeButton.addActionListener(e -> {
            if(scaleRatio < 100){
                scaleRatio += 10;

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });

        smallButton.addActionListener(e -> {
            if(scaleRatio > 10){
                scaleRatio -= 10;

                drawPanel.removeAll();
                drawPanel.repaint();
            }
        });
    }

    protected void updateValue(int type, String value){
        switch (type){
            case NeuralNetwork.DIMENSION:
                dimensionValueLabel.setText(value);
                break;
            case NeuralNetwork.TRAINING_SET:
                String s0 = setValueLabel.getText();
                String[] ss0 = s0.split("/");

                setValueLabel.setText(value + "/ " + ss0[1]);
                break;
            case NeuralNetwork.TESTING_SET:
                String s1 = setValueLabel.getText();
                String[] ss1 = s1.split("/");

                setValueLabel.setText(ss1[0] + "/ " + value);
                break;
            case NeuralNetwork.WEIGHT:
                weightValueLabel.setText(value);
                String[] s = value.split(",");
                for(int i = 0; i < s.length; i++){
                    weightPoints.add(Double.parseDouble(s[i]));
                }
                break;
            case NeuralNetwork.TESTING_RECOGNITION_RATE:
                testingRateValueLabel.setText(value + "%");
                break;
        }
    }

    private void createUIComponents() {
        drawPanel = new NeuralNetworkJFrame.GridPanel();
    }

    private class GridPanel extends JPanel{
        @Override
        public void paintComponent(Graphics g){
            super.paintComponent(g);

            drawPanelSize = drawPanel.getSize().width;

            g.drawLine(drawPanelSize / 2, 0, drawPanelSize / 2, drawPanelSize);
            g.drawLine(0, drawPanelSize / 2, drawPanelSize, drawPanelSize / 2);

            Graphics2D g2 = (Graphics2D) g;

            // draw grid
            for (double i = (drawPanelSize / 2d); i >= 0; i -= 5.0 * scaleRatio / 10) {
                drawGrid(g2, i);
            }
            for (double i = (drawPanelSize / 2d); i <= drawPanelSize; i += 5.0 * scaleRatio / 10) {
                drawGrid(g2, i);
            }

            // draw training point
            if (dataSet.trainingX.size() > 0 && dataSet.trainingX.get(0).length == 3) {
                drawPoint(g2, dataSet.trainingX, dataSet.trainingD);
            }

            // draw testing point
            if (dataSet.testingX.size() > 0 && dataSet.testingX.get(0).length == 3) {
                drawPoint(g2, dataSet.testingX, dataSet.testingD);
            }

            // draw line
            if (weightPoints.size() != 0 && dataSet.testingX.get(0).length == 3) {
                drawLine(g2, weightPoints);
            }
        }

        private void drawGrid(Graphics2D g2, double i) {
            g2.setStroke(new BasicStroke(1));

            double[] top, btn;
            double scaleLength = (i % (5.0 * scaleRatio / 5) == 0) ? 2.0 * scaleRatio / 20 : 1.0 * scaleRatio / 20;
            top = convertCoordinate(new double[]{(i - (drawPanelSize / 2)) / scaleRatio, scaleLength / scaleRatio});
            btn = convertCoordinate(new double[]{(i - (drawPanelSize / 2)) / scaleRatio, -scaleLength / scaleRatio});
            g2.draw(new Line2D.Double(top[0], top[1], btn[0], btn[1]));
            top = convertCoordinate(new double[]{-scaleLength / scaleRatio, ((drawPanelSize / 2) - i) / scaleRatio});
            btn = convertCoordinate(new double[]{scaleLength / scaleRatio, ((drawPanelSize / 2) - i) / scaleRatio});
            g2.draw(new Line2D.Double(top[0], top[1], btn[0], btn[1]));
        }

        private void drawPoint(Graphics2D g2, ArrayList<double[]> data, ArrayList<Double> output){
            g2.setStroke(new BasicStroke(3));

            for(int i = 0; i < data.size(); i++){
                double[] x = data.get(i);
                double[] point = convertCoordinate(new double[]{x[1], x[2]});
                g2.setColor(pointColor[(int)Math.round(output.get(i))]);
                g2.draw(new Line2D.Double(point[0], point[1], point[0], point[1]));
            }
        }

        private void drawLine(Graphics2D g2, ArrayList<Double> points){
            g2.setColor(lineColor);
            g2.setStroke(new BasicStroke(2));

            double[] lineStart, lineEnd;
//            if (points.get(2) != 0) {
            if (points.size() > 2) {
                lineStart = convertCoordinate(new double[]{-(double) drawPanelSize / scaleRatio, (points.get(0) + (double) drawPanelSize / scaleRatio * points.get(1)) / points.get(2)});
                lineEnd = convertCoordinate(new double[]{(double) drawPanelSize / scaleRatio, (points.get(0) - (double) drawPanelSize / scaleRatio * points.get(1)) / points.get(2)});
            } else {
                lineStart = convertCoordinate(new double[]{points.get(0) / points.get(1), (double) drawPanelSize / scaleRatio});
                lineEnd = convertCoordinate(new double[]{points.get(0) / points.get(1), -(double) drawPanelSize / scaleRatio});
            }
            g2.draw(new Line2D.Double(lineStart[0], lineStart[1], lineEnd[0], lineEnd[1]));
        }

        private double[] convertCoordinate(double[] beforePoint) {
            double[] afterPoint = new double[2];
            afterPoint[0] = (beforePoint[0] * scaleRatio) + (drawPanelSize / 2);
            afterPoint[1] = (drawPanelSize / 2) - (beforePoint[1] * scaleRatio);
            return afterPoint;
        }
    }
}

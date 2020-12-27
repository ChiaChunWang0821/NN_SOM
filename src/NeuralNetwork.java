public abstract class NeuralNetwork {

    protected final static int NUMBER_OF_NEURONS_ROW = 1;
    protected final static int NUMBER_OF_NEURONS_COLUMN = 2;

    protected final static int DIMENSION = -1;
    protected final static int TRAINING_SET = -2;
    protected final static int TESTING_SET = -3;
    protected final static int WEIGHT = -4;
    protected final static int TESTING_RECOGNITION_RATE = -5;

    protected abstract void setValue(int type, String string);
    protected abstract void setDataSet(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet);
    protected abstract void start(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet);

}

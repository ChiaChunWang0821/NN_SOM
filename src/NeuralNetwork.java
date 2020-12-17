public abstract class NeuralNetwork {

    protected abstract void setValue(int type, String string);
    protected abstract void setDataSet(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet);
    protected abstract void start(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet);

}

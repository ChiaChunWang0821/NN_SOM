public class NeuralNetworkTest{

    public static void main(String[] args)
    {
        testSOM();
    }

    private static void testSOM(){
        NeuralNetworkJFrame neuralNetworkJFrame = new NeuralNetworkJFrame();
        SOM som = new SOM();

        OnNeuralNetworkCallback onNeuralNetworkCallback = new OnNeuralNetworkCallback() {
            DataSet set = null;

            @Override
            public void dataReadyCallback(DataSet dataSet) {
                set = dataSet;
                som.setDataSet((OnNeuralNetworkCallback)this, dataSet);
            }

            @Override
            public void showResultCallback() {
                if (set != null){
                    som.setDataSet((OnNeuralNetworkCallback)this, set);
                    som.start((OnNeuralNetworkCallback)this, set);
                }
            }

            @Override
            public void getValueCallback(int type, String string) {
                som.setValue(type, string);
            }

            @Override
            public void setValueCallback(int type, String string) {
                neuralNetworkJFrame.updateValue(type, string);
            }
        };

        neuralNetworkJFrame.actionListener(onNeuralNetworkCallback);
    }
}

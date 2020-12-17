public class SOM extends NeuralNetwork{

    protected SOM(){ }

    @Override
    protected void setDataSet(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet){
//        setLayers(dataSet.dimension);
//        setNumOfClass(dataSet);
//        normalizedD(dataSet.trainingD);
//        normalizedD(dataSet.testingD);
//
//        if(checkMaxLearningCycle(dataSet.trainingSet)){
//            onNeuralNetworkCallback.setValueCallback(MAX_LEARNING_CYCLE, Integer.toString(maxLearningCycle));
//        }
    }

    @Override
    protected void setValue(int type, String string){
//        switch (type){
//            case EACH_LAYER_OF_NEURONS:
//                eachLayerOfNeuronsString = string.split(",");
//                break;
//            case MAX_LEARNING_CYCLE:
//                maxLearningCycle = Integer.parseInt(string);
//                break;
//            case LEARNING_RATE:
//                learningRate = Double.parseDouble(string);
//                break;
//            case MOMENTUM:
//                momentum = Double.parseDouble(string);
//                break;
//            case THRESHOLD:
//                threshold = Double.parseDouble(string);
//                break;
//            case ERROR_TOLERANCE:
//                errorTolerance = Double.parseDouble(string);
//                break;
//            case REGRESSION:
//                normalizedD = REGRESSION;
//                break;
//            case CLASSIFICATION:
//                normalizedD = CLASSIFICATION;
//                break;
//        }
    }

    @Override
    protected void start(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet){
//        clear();
//
//        train(dataSet);
//        test(dataSet);
//
//        updateValue(onNeuralNetworkCallback);
    }

}

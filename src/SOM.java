import java.math.BigDecimal;

public class SOM extends NeuralNetwork{

    private final int MULTIPLE_OF_CYCLE = 100;

    private int numOfNeuralColumn = 3;
    private int numOfNeuralRow = 3;
    private int numOfNeural;

    private int neighborhood;
    private int neighborhoodCycle;

    private int dimension;

    private double[][] weight;
    private double[] weightClass;

    private int maxLearningCycle;
    private int cycle = 0;
    private double learningRate = 0;

    private double testingRecognitionRate = 0;

    protected SOM(){

    }

    @Override
    protected void setDataSet(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet){
        dimension = dataSet.dimension;
    }

    @Override
    protected void setValue(int type, String string){
        switch (type){
            case NUMBER_OF_NEURONS_ROW:
                numOfNeuralRow = Integer.parseInt(string);
                break;
            case NUMBER_OF_NEURONS_COLUMN:
                numOfNeuralColumn = Integer.parseInt(string);
                break;
        }
    }

    @Override
    protected void start(OnNeuralNetworkCallback onNeuralNetworkCallback, DataSet dataSet){
        initialParameter();

        train(dataSet);
        test(dataSet);

        updateValue(onNeuralNetworkCallback);
    }

    private void initialParameter(){
        cycle = 0;

        numOfNeural = numOfNeuralRow * numOfNeuralColumn;
        if(numOfNeuralColumn <= numOfNeuralRow){
            if(numOfNeuralColumn % 2 == 0){
                neighborhood = numOfNeuralColumn;
            }
            else{
                neighborhood = numOfNeuralColumn - 1;
            }
        }
        else {
            if(numOfNeuralRow % 2 == 0){
                neighborhood = numOfNeuralRow;
            }
            else{
                neighborhood = numOfNeuralRow - 1;
            }
        }

        maxLearningCycle = numOfNeural * MULTIPLE_OF_CYCLE;

//        neighborhoodCycle = maxLearningCycle / (neighborhood / 2);
    }

    private void train(DataSet dataSet){
        initialWeight();

        while(cycle < maxLearningCycle){
            cycle++;
            adjustParameter();
            int inputIndex = randomSelectionInput(dataSet.trainingSet);
            int winner = selectWinner(dataSet.trainingX.get(inputIndex), dataSet.trainingD.get(inputIndex));
            adjustWeight(winner, dataSet.trainingX.get(inputIndex));
        }
    }

    private void initialWeight(){
        weight = new double[numOfNeural][dimension];
        weightClass = new double[numOfNeural];

        for(int j = 0; j < weight.length; j++){
            weightClass[j] = -1;

            for(int i = 0; i < weight[j].length; i++) {
                double rand = Math.random();
                BigDecimal bigDecimal = new BigDecimal(rand);
                weight[j][i] = bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
            }
        }
    }

    private int randomSelectionInput(int set){
        int rand = (int) Math.round(Math.random() * (set - 1));
        return rand;
    }

    private int selectWinner(double[] x, double d){
        int winnerIndex = 0;
        double minDistance = 0;
        for(int j = 0; j < weight.length; j++){
            double sum = 0;
            for(int i = 0; i < dimension; i++){
                double minus = x[i] - weight[j][i];
                sum += (minus * minus);
            }
            sum = Math.sqrt(sum);

            if(j == 0){
                minDistance = sum;
            }
            else {
                if(sum < minDistance){
                    minDistance = sum;
                    winnerIndex = j;
                }
            }
        }

        weightClass[winnerIndex] = d;
        return winnerIndex;
    }

    private void adjustWeight(int winner, double[] x){
        for(int j = 0; j < weight.length; j++){
            if(checkNeighborhood(winner, j)){
                for(int i = 0; i < weight[j].length; i++){
                    double tmp = weight[j][i] + (learningRate * (x[i] - weight[j][i]));
                    BigDecimal bigDecimal = new BigDecimal(tmp);
                    weight[j][i] = bigDecimal.setScale(5, BigDecimal.ROUND_HALF_UP).doubleValue();
                }
            }
        }
    }

    private boolean checkNeighborhood(int winner, int check){ // 3 * 3
        if(check == winner){
            return true;
        }
        else if(check == (winner + 1)){ // right
            return true;
        }
        else if(check == (winner - 1)){ // left
            return true;
        }
        else if(check == (winner - numOfNeuralColumn)){ // up
            return true;
        }
        else if(check == (winner + numOfNeuralColumn)){ // down
            return true;
        }
        else if(check == (winner - numOfNeuralColumn - 1)){ // up-left
            return true;
        }
        else if(check == (winner - numOfNeuralColumn + 1)){ // up-right
            return true;
        }
        else if(check == (winner + numOfNeuralColumn - 1)){ // down-left
            return true;
        }
        else if(check == (winner + numOfNeuralColumn + 1)){ // down-right
            return true;
        }
        else{
            return false;
        }
    }

    private void adjustParameter(){
        adjustLearningRate();
//        adjustNeighborhood();
    }

    private void adjustLearningRate(){
        learningRate = 0.9 * (1.0 - ((double)cycle / 1000.0));
    }

    private void adjustNeighborhood(){
        for(int i = 0; ; i++){
            if(cycle < neighborhoodCycle * i){
                neighborhood -= 2;
            }
        }
    }

    private void test(DataSet dataSet){
        int correct = 0;
        for(int i = 0; i < dataSet.testingSet; i++){
            if(checkClassificationCorrect(dataSet.testingX.get(i), dataSet.testingD.get(i))){
                correct++;
            }
        }

        testingRecognitionRate = (double) correct / dataSet.testingSet * 100.0;
    }

    private boolean checkClassificationCorrect(double[] x, double d){
        int winnerIndex = 0;
        double minDistance = 0;
        for(int j = 0; j < weight.length; j++){
            double sum = 0;
            for(int i = 0; i < dimension; i++){
                double minus = x[i] - weight[j][i];
                sum += (minus * minus);
            }
            sum = Math.sqrt(sum);

            if(sum < minDistance){
                winnerIndex = j;
            }
        }

        if(weightClass[winnerIndex] == d){
            return true;
        }
        else {
            return false;
        }
    }

    private void updateValue(OnNeuralNetworkCallback onNeuralNetworkCallback){
        StringBuilder weightString = new StringBuilder(" ");
//        for(double[] ww: weight){
//            weightString.append("(");
//            for(double w: weight[0]){
//                weightString.append(w).append(",");
//            }
//            weightString.deleteCharAt(weightString.length() - 1);
//            weightString.append("),");
//        }

        for(double w: weight[0]){
            weightString.append(w).append(",");
        }
        weightString.deleteCharAt(weightString.length() - 1);
        onNeuralNetworkCallback.setValueCallback(WEIGHT, weightString.toString());

        onNeuralNetworkCallback.setValueCallback(TESTING_RECOGNITION_RATE, Double.toString(testingRecognitionRate));
    }
}

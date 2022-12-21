public class ProcessStatistic {

    private int processNumber;
    private int arrivalTime;
    private int serviceTime = 0;
    private int startTime;

    private int waitTime;
    private int finishTime;
    private int turnAroundTime;
    private double normalizedTurnaroundTime;
    private double averageResponseTime;

    public ProcessStatistic(int processNumber, int arrivalTime){
        this.processNumber = processNumber;
        this.arrivalTime = arrivalTime;
    }

    public void setStartTime(int startTime){
        this.startTime = startTime;
    }

    public int getFinishTime(){
        return this.finishTime;
    }

    public void addWaitTime(int time){
        this.waitTime += time;
    }

    public void addServiceTime(int time){
        serviceTime += time;
    }

    public void setFinishTime(int finishTime){
        this.finishTime = finishTime;
        this.calculateTurnAroundTime();
        this.calculateNormalizedTurnaroundTime();
        this.calculateAverageResponseTime();
    }

    public void setArrivalTime(int arrivalTime){
        this.arrivalTime = arrivalTime;
    }

    private void calculateTurnAroundTime() {
        this.turnAroundTime = this.finishTime - this.arrivalTime;
    }

    private void calculateNormalizedTurnaroundTime(){
        this.normalizedTurnaroundTime = (double)this.turnAroundTime / this.serviceTime;
    }

    private void calculateAverageResponseTime(){

    }

    @Override
    public String toString(){

        String str;
        str = String.format("For process %d\n" +
                "\tArrival Time: %d\n" +
                "\tService Time: %d\n" +
                "\tStart Time: %d\n" +
                "\tFinish Time: %d\n" +
                "\tTurnaround Time: %d\n" +
                "\tNormalized Turnaround Time: %f\n" +
                "\tAverage Response Time: %.1f",processNumber, arrivalTime,
                serviceTime, startTime, finishTime, turnAroundTime,
                normalizedTurnaroundTime, averageResponseTime);

        return str;
    }

}

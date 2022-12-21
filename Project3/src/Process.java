
import java.util.ArrayList;
import java.util.List;

public class Process {
    private int processNumber;
    private List<Integer> listOfActivity;
    private int currentEventIndex = 0;
    private Event currentEvent;
    private ProcessStatistic statistic;
    private boolean processIsStarted = false;

    // FCFS constructor
    public Process(int processNumber, ArrayList<Integer> listOfActivity){
        this.processNumber = processNumber;
        this.listOfActivity = listOfActivity;
        this.statistic = new ProcessStatistic(this.processNumber, listOfActivity.get(0));
    }

    public Process(ArrayList<Integer> toArray) {
    }

    // VRR Constructor
    public Event getEvent(int currentClock){

        return generateEvent(currentClock, currentEventIndex++);
    }

    public int getEventTimeUnit(){
        return this.currentEvent.getTimeUnits();
    }
    public String getCurrentEventName(){
        return currentEvent.getEventName();
    }

    public int getProcessArrivalTime(){
        return listOfActivity.get(0);
    }
    public void setArrivalTime(int time){
        this.statistic.setArrivalTime(time);
    }

    public int getCurrentEventDuration(){
        return listOfActivity.get(currentEventIndex - 1);
    }
    public void setProcessIsStarted(boolean flag){
        this.processIsStarted = true;
    }

    public boolean isProcessIsStarted(){
        return this.processIsStarted;
    }
    public void addWaitTime(int time){
        this.statistic.addWaitTime(time);
    }

    public void decreaseCurrentEventIndex(){
        this.currentEventIndex = currentEventIndex - 1;
    }
    public void setEventTimeUnit(int time){
        currentEvent.setTimeUnit(time);
        listOfActivity.set(currentEventIndex, time);
    }
    public void addServiceTime(int time){
        statistic.addServiceTime(time);
    }

    public void setFinishTime(int time){
        this.statistic.setFinishTime(time);
    }

    private Event generateEvent(int currentClock, int currentEventIndex) {
        if (currentEventIndex == listOfActivity.size()){
            currentEvent = new Event(processNumber,"exit", currentClock, 0);
            return currentEvent;
        }

        int timeUnit = listOfActivity.get(currentEventIndex);
        int eventTime = currentClock + timeUnit;

//processNumber, eventName, atTime, timeUnit

        if(currentEventIndex == 0) {
            currentEvent = new Event(processNumber,"arrival", eventTime, timeUnit);
            statistic.setArrivalTime(eventTime);
            return currentEvent;
        }

        if(currentEventIndex % 2 == 0) {
            currentEvent = new Event(processNumber,"block", eventTime - timeUnit, timeUnit);
        }

        else {
            currentEvent = new Event(processNumber,"running", currentClock, timeUnit);
        }



        return currentEvent;
    }

    public void setProcessStartTime(int processStartTime){
        this.statistic.setStartTime(processStartTime);
    }

    @Override
    public String toString(){
        return statistic.toString();
    }


}

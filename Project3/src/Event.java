public class Event{
    private String eventName;
    private int processNumber;
    private int timeUnit;
    private int atTime;


    public Event(int processNumber, String eventName, int atTime, int timeUnit){
        this.eventName = eventName;
        this.processNumber = processNumber;
        this.atTime = atTime;
        this.timeUnit = timeUnit;
    }

    public Event(int processNumber, String eventName, int atTime){
        this(processNumber, eventName, atTime, 0);
    }

    public String getEventName() {
        return eventName;
    }
    public int getProcessNumber(){
        return this.processNumber;
    }

    public int getTimeUnits() {
        return timeUnit;
    }

    public int getEventTime(){
        return this.atTime;
    }
    public void setTimeUnit(int time){
        this.timeUnit = time;
    }

    @Override
    public String toString(){
        return String.format("Process%d %s atTime=%d For=%d Units", processNumber, eventName, atTime, timeUnit);
    }
}
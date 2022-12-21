import java.util.*;

public class HRRN implements Scheduler {

    private  Queue<Process> readyQueue = new PriorityQueue<>(100, new ProcessComparator());
    private  Queue<Event> eventQueue = new PriorityQueue<>(100, new EventComparator());
    private  int currentClock = 0;
    private boolean CPUIdle = true;
    private int allInQueue = 0;
    private boolean service_given;
    private double alpha;
    private List<Process> listOfProcess;

    public HRRN(boolean service_given, double alpha, ArrayList<Process> listOfProcess){
        this.listOfProcess = listOfProcess;
        this.service_given = service_given;
        this.alpha = alpha;
        generateTheStatistic();
    }

    public void generateTheStatistic(){

        // get first event for each one of the process
        for (int i = 0; i < listOfProcess.size(); i++) {
            eventQueue.add(listOfProcess.get(i).getEvent(currentClock));
        }
        // Get in while loop
        while(true){

            Event currentEvent = eventQueue.poll();
            String eventName = currentEvent.getEventName().toLowerCase();
            int processNumber = currentEvent.getProcessNumber();
            int eventTime = currentEvent.getEventTime();
            int timeUnits = currentEvent.getTimeUnits();
            Process currentProcess = listOfProcess.get(processNumber);

            //Check the process
            if(eventName.equals("arrival")) {
//                if(currentClock < eventTime)
//                    currentClock += timeUnits;

                readyQueue.add(listOfProcess.get(processNumber));
                currentProcess.setArrivalTime(eventTime);
            }
            else if (eventName.equals("unblock")){
                readyQueue.add(listOfProcess.get(processNumber));
                if(currentClock < eventTime)
                    currentClock += timeUnits;
            }
            else if (eventName.equals("timeout")){
                // add back to the ready queue
                readyQueue.add(currentProcess);
                CPUIdle = true;
            }
            else if (eventName.equals("block")){
                CPUIdle = true;
                eventQueue.add(new Event(processNumber, "unblock", eventTime + timeUnits, timeUnits));
            }
            else if (eventName.equals("running")){
                CPUIdle = false;
                int quantumTime = 1;

                if(listOfProcess.size() != allInQueue && timeUnits > 0){

                    currentProcess.addServiceTime(quantumTime);
                    currentProcess.decreaseCurrentEventIndex();
                    currentProcess.setEventTimeUnit(timeUnits - quantumTime);

                    currentClock += quantumTime;
                    eventQueue.offer(new Event(processNumber, "timeout", eventTime + quantumTime));

                }
                else if(listOfProcess.size() != allInQueue && timeUnits == 0){
                    eventQueue.add(currentProcess.getEvent(currentClock));
                }
                else {

                    currentProcess.addServiceTime(timeUnits);
                    //if(currentClock < eventTime)
                    currentClock += timeUnits;

                    // Generate IO or other event
                    eventQueue.add(currentProcess.getEvent(currentClock));
                }
            }
            else if (eventName.equals("exit")){
                if(currentClock < eventTime)
                    currentClock += timeUnits;

                currentProcess.setFinishTime(eventTime);
                CPUIdle = true;
            }


            if(eventQueue.isEmpty() && !readyQueue.isEmpty()) {
                CPUIdle = true;
                dispatchProcess();
            }
            else
                dispatchProcess();


            if (readyQueue.isEmpty() && eventQueue.isEmpty())
                break;

        }

    }
    private void dispatchProcess(){
        if(CPUIdle && !readyQueue.isEmpty()){

            // CPU time:
            Process process = readyQueue.poll();
            Event event = process.getEvent(currentClock);
            int timeUnits = event.getTimeUnits();
            int eventTime = event.getEventTime();


            // Condition for process starting time
            if(!process.isProcessIsStarted()) {
                process.setProcessStartTime(eventTime);
                process.setProcessIsStarted(true);
                allInQueue++;
            }

            eventQueue.add(event);



        }
    }

    public static class EventComparator implements Comparator<Event>{

        @Override
        public int compare(Event e1, Event e2) {
            if(e1.getEventTime() > e2.getEventTime())
                return 1;
            else if (e1.getEventTime() < e2.getEventTime())
                return -1;
            else {
                if(eventNumber(e1.getEventName()) < eventNumber(e2.getEventName()))
                    return -1;
                else
                    return 1;
            }
        }
        private int eventNumber(String event){
            String eve = event.toLowerCase();

            if (eve.equals("arrival"))
                return 0;
            else if(eve.equals("unblock"))
                return 2;
            else if(eve.equals("timeout"))
                return 3;
            else if(eve.equals("block"))
                return 4;
            else if(eve.equals("running"))
                return 1;
            else if(eve.equals("exit"))
                return 5;
            else
                return -1;
        }
    }

    private class ProcessComparator implements Comparator<Process>{

        @Override
        public int compare(Process p1, Process p2) {
            int p1RemainingTime = p1.getEventTimeUnit();
            int p2RemainingTime = p2.getEventTimeUnit();
            int p1ArrivalTime = p1.getProcessArrivalTime();
            int p2ArrivalTime = p2.getProcessArrivalTime();

            if(p1.getCurrentEventName().equals("arrival"))
                return -1;
            else if (p2.getCurrentEventName().equals("arrival"))
                return 1;
            else if (p1.getCurrentEventName().equals("arrival"))
                return 1;

            if(p1RemainingTime > p2RemainingTime)
                return 1;
            else if (p1RemainingTime < p2RemainingTime)
                return -1;
            else {
                if(p1ArrivalTime < p2ArrivalTime)
                    return -1;
                else
                    return 1;
            }
        }
    }

    @Override
    public void printStatistic() {
        for(Process process : listOfProcess)
            System.out.println(process);
    }
}

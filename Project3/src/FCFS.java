import java.util.*;

public class FCFS implements Scheduler {

    private  Deque<Process> readyQueue = new ArrayDeque<>();
    private  Queue<Event> eventQueue = new PriorityQueue<>(10, new EventComparator());
    private  int currentClock = 0;
    private boolean CPUIdle = true;
    private List<Process> listOfProcess;

    public FCFS(ArrayList<Process> listOfProcess){
          this.listOfProcess = listOfProcess;
          generateTheStatistic();
    }

    public void generateTheStatistic(){
        /*
            1. Initially, Trying to get the first event on each process.
            2. Add those event to the eventQueue.
            3. Get in the while loop.
            4. check in the eventQueue a
            5. pop the earliest one
            6. check the process and add that process to the ready queue
            7. Do something: update the current clock and update process statistic in "Process" class
            8.
         */
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
                    if(currentClock < eventTime)
                        currentClock += timeUnits;

                    readyQueue.add(listOfProcess.get(processNumber));
                    currentProcess.setArrivalTime(eventTime);
                }
                else if (eventName.equals("unblock")){
                    readyQueue.add(listOfProcess.get(processNumber));
                    if(currentClock < eventTime)
                        currentClock += timeUnits;
                }
                else if (eventName.equals("timeout"));
                else if (eventName.equals("block")){
                    CPUIdle = true;
                    eventQueue.add(new Event(processNumber, "unblock", eventTime + timeUnits, timeUnits));
                }
                else if (eventName.equals("running")){
                    CPUIdle = false;
                    currentProcess.addServiceTime(timeUnits);
                    //if(currentClock < eventTime)
                        currentClock += timeUnits;

                    // Generate IO or other event
                        eventQueue.add(currentProcess.getEvent(currentClock));
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
                return 1;
            else if(eve.equals("timeout"))
                return 2;
            else if(eve.equals("block"))
                return 3;
            else if(eve.equals("running"))
                return 4;
            else if(eve.equals("exit"))
                return 5;
            else
                return -1;
        }
    }

    @Override
    public void printStatistic() {
        for(Process process : listOfProcess)
            System.out.println(process);
    }
}

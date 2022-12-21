import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Simulator {
    public static void main(String[] args) throws FileNotFoundException {
        File scheduleFile = new File(args[0]);
        File processFile = new File(args[1]);

        ArrayList<Process> listofProcess = new ArrayList<>();

        Scheduler scheduler = null;

        Scanner sf = new Scanner(scheduleFile);
        Scanner pf = new Scanner(processFile);

        String action;
        int i = 0;
        while(pf.hasNext()){
            String[] arrOfInt = pf.nextLine().split("\\s+");
            System.out.println(arrOfInt);
            listofProcess.add(new Process(i++, toArray(arrOfInt)));
        }
        
        while(sf.hasNext()){
             action = sf.nextLine().toLowerCase();

            if(action.equals("fcfs")){
                scheduler = new FCFS(listofProcess);
            }
            else if(action.equals(("vrr"))) {
                String[] arrOfStr = sf.nextLine().split("=");
                int QuantumTime = Integer.valueOf(arrOfStr[arrOfStr.length - 1]);
                scheduler = new VRR(QuantumTime, listofProcess);

            }
            else if(action.equals("srt")){
                // Get service_given
                String[] arrOfStr = sf.nextLine().split("=");
                boolean service_given = Boolean.valueOf(arrOfStr[arrOfStr.length - 1]);

                // Get Alpha
                arrOfStr = sf.nextLine().split("=");
                double alpha = Double.valueOf(arrOfStr[arrOfStr.length - 1]);

                scheduler = new SRT(service_given, alpha, listofProcess);
            }
            else if(action.equals("hrrn")){
                // Get service_given
                String[] arrOfStr = sf.nextLine().split("=");
                boolean service_given = Boolean.valueOf(arrOfStr[arrOfStr.length - 1]);

                // Get Alpha
                arrOfStr = sf.nextLine().split("=");
                double alpha = Double.valueOf(arrOfStr[arrOfStr.length - 1]);

                scheduler = new HRRN(service_given, alpha, listofProcess);
            }
            else if(action.equals("feedback")){

                // Get num_priorities
                String[] arrOfStr = sf.nextLine().split("=");
                int num_priorities = Integer.valueOf(arrOfStr[arrOfStr.length - 1]);

                // Get guantum
                arrOfStr = sf.nextLine().split("=");
                int quantum = Integer.valueOf(arrOfStr[arrOfStr.length - 1]);

                scheduler = new FEEDBACK(num_priorities, quantum, listofProcess);
            }
            else{
                scheduler = new FCFS(listofProcess);
            }
        }

        scheduler.printStatistic();

    }

    public static ArrayList<Integer> toArray(String[] arr){
        ArrayList<Integer> arrayOfInt = new ArrayList<>();

        for(String o : arr)
            arrayOfInt.add(Integer.valueOf(o));

        return arrayOfInt;
    }
}
/*
    Assignment: Project 2
    Student Name: Bondith Sovann
    Student ID: Bxs210029
    Section #: 502
 */

import java.util.Random;
import java.util.concurrent.Semaphore;
public class Bank {

    // Number of customer (Threads)
    private static int NumOfCustomer = 50;
    // Number of teller (Threads)
    private static int NumOfTeller = 3;

    // Transaction status ( 0 = Deposit ; 1 = Withdrawal )
    private static boolean transactionStatus;

    // Generate Random number
    private static Random randomNum = new Random();

    // Customer id
    private static int customerId;

    // Teller id
    private static String tellerId;

    // Number of peoples that allow into the bank ( Two peoples allow in the bank)
    private static Semaphore door = new Semaphore(2);

    // Get permission from the manager
    private static Semaphore getPermissionFromManager = new Semaphore(1);

    // Access the safe (Only two tellers allow to access the safe)
    private static Semaphore accessSafe = new Semaphore(2);

    // Introduce to teller
    private static Semaphore introduceToTeller = new Semaphore(1);

    // Introduce to Customer
    private static Semaphore introduceToCustomer = new Semaphore(1);

    // Wait customer to select
    private static Semaphore waitTellerToIntroduce = new Semaphore(0);

    // Transaction requested prompt from the Teller
    private static Semaphore transactionRequest = new Semaphore(0);

    private static Semaphore processTransaction = new Semaphore(0);

    // Wait teller to complete the transaction
    private static Semaphore waitTellerToCompleteTransaction = new Semaphore(0);

    // Teller announcement: will let everyone know it is ready to serve
    private static Semaphore customerAnouncement = new Semaphore(0);

    // Bank is closed
    private static Semaphore bankIsClosed = new Semaphore(0);

    public static void main(String[] args) throws InterruptedException {
        Thread[] teller = new Thread[NumOfTeller];

        // Create Threads for Customer (Waiting Until the bank is opened)
        for (int i = 0; i < NumOfCustomer; i++){
            Thread customer = new Thread(new customer());
            customer.start();
        }

        // Create Threads for Teller (When the bank is open and read, the teller will announce to the user)
        for (int i = 0; i < NumOfTeller; i++){
            teller[i] = new Thread(new teller());
            teller[i].start();
            teller[i].setName(Integer.toString(i + 1));
        }

        // Bank is closed after serving all the customer
        bankIsClosed.acquire();
        System.out.println("Bank is closed");
        System.exit(0);
    }

    // Customer class
    private static class customer implements Runnable {

        @Override
        public void run() {
            // Local Variable to store TellerID
            String telId;
            // Local Variable to store CustomerID
            int cusId = (int) Thread.currentThread().getId();

            try {

                // (Door) Allow only two peoples in the bank
                door.acquire();
                    // Wait Teller to call
                    System.out.printf("Customer %d is going to the bank.\n", cusId);
                    System.out.printf("Customer %d is getting in line.\n", cusId);
                    customerAnouncement.acquire();
                    telId = tellerId;

                    // Customer goes to teller and introduce itself
                    introduceToTeller.acquire();
                    System.out.printf("Customer %d goes to Teller %s.\n", cusId, telId);

                    customerId = (int)Thread.currentThread().getId();

                    System.out.printf("Customer %d introduces itself to Teller %s.\n", cusId, telId);
                    waitTellerToIntroduce.release();

                    introduceToTeller.release();

                    // Ask teller for transaction
                    transactionRequest.acquire();
                    System.out.printf("Customer %d asks for a %s transaction.\n", cusId
                                    ,(getTransactionType()) ? "withdrawal" : "deposit");
                    processTransaction.release();

                    // Wait teller to complete the transaction
                    waitTellerToCompleteTransaction.acquire();

                // leave the bank
                door.release();
                System.out.printf("Customer %d exit the bank.\n", cusId);
                Thread.currentThread().join();


            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        //Helper Function: Get random transaction type (Deposit or Withdrawal)
        public static boolean getTransactionType(){
            Random ran = new Random();
              return transactionStatus = ran.nextBoolean();
        }

    }

    // Teller Class
    private static class teller implements Runnable {

        @Override
        public void run() {
            // Local variable for CustomerID
            int cusId;
            // Local variable for TellerID
            String telId = Thread.currentThread().getName();

            //start serve the customer
            try {
                while(true) {

                    // Call the user and Introduce the ID to the customer
                    introduceToCustomer.acquire();
                    tellerId = telId;

                    customerAnouncement.release();

                        // wait customer to introduce
                        waitTellerToIntroduce.acquire();
                        cusId = customerId;
                        NumOfCustomer--;
                    introduceToCustomer.release();

                    // Teller starts serving customer
                    System.out.printf("Teller %s is serving Customer %d.\n", telId, cusId);
                    System.out.printf("Teller %s asks Customer %d: Deposit or Withdrawal.\n", telId, cusId);
                    transactionRequest.release();

                    // Wait for transaction request
                    processTransaction.acquire();
                    System.out.printf("Teller %s is handling the %s transaction for Customer %d.\n", telId
                            ,(transactionStatus) ? "withdrawal" : "deposit", cusId);

                    // Process transaction
                    if(transactionStatus)
                        withDrawalFunction(telId);
                    else
                        depositFunction(telId);

                    // Transaction Completed: notify the customer that it is done.
                    System.out.printf("Teller %s completed the transaction for customer %d.\n", telId, cusId);
                    waitTellerToCompleteTransaction.release();

                    if (NumOfCustomer == 0)
                        bankIsClosed.release();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }



            }

            //Helper Function: perform withdrawal functionality
            public static void withDrawalFunction(String telId) throws InterruptedException {
                // go to manager
                System.out.printf("Teller %s is going to the manager.\n", telId);
                askManager(telId); // Should always get approved.
                goToSafe(telId);    // Go to the safe after getting the approval.
            }

            //Helper Function: perform deposit functionality
            public static void depositFunction(String telId) throws InterruptedException {
                // Go straight to the safe (No need to meet the manager)
                goToSafe(telId);
            }

            //Helper Function: Go to safe functionality (Only two tellers are allowed)
            public static void goToSafe(String telId) throws InterruptedException {
                // Allow two tellers at a time
                System.out.printf("Teller %s is going to the safe.\n", telId);
                accessSafe.acquire();
                System.out.printf("Teller %s is in the safe.\n", telId);
                Thread.sleep(randomNum.nextInt(10, 51));
                accessSafe.release();
            }

            //Helper Function: Go to manager to get an approval (Only one teller is allowed)
            public static void askManager(String telId) throws InterruptedException {
                // Allow one teller at a time
                getPermissionFromManager.acquire();
                System.out.printf("Teller %s is getting the manager's permission.\n", telId);
                System.out.printf("Teller %s is got the manager's permission.\n", telId);
                Thread.sleep(randomNum.nextInt(5, 31));
                getPermissionFromManager.release();
            }
        }
    }


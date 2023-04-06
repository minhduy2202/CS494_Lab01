package old;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class InputTimer {
    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        String line = null;
        long start = System.currentTimeMillis();
        long maxReadTime = 3000; // buffer will be closed after 3 seconds

        // buffer will be closed after 3 seconds
        try {
            while ((line = br.readLine()) != null) {
                boolean active = System.currentTimeMillis() - start < maxReadTime;
                if (!active) {
                    br.close();
                }
                // doingSomethingHere()
                System.out.println("You entered: " + line);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Timer timer = new Timer();
        // int countdown = 3; // Countdown in seconds

        // // Create a TimerTask to stop receiving input after the time limit is up
        // TimerTask stopInputTask = new TimerTask() {
        // @Override
        // public void run() {
        // System.out.println("\nTime's up!");
        // try {
        // br.close();
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // }
        // };

        // // Schedule the TimerTask to run after the countdown time
        // timer.schedule(stopInputTask, countdown * 1000);

        // // Wait for user input until the time limit is up
        // String input = null;
        // while (countdown > 0) {
        // System.out.print("Enter something: ");
        // if (br.) {
        // input = br.readLine();
        // System.out.println("You entered: " + input);
        // }
        // countdown--;
        // }

        // // Cancel the TimerTask since the countdown is complete
        // stopInputTask.cancel();
        // timer.cancel();
    }
}

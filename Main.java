import ui.ConsoleInterface;
import utility.RentalStatusUpdater;

public class Main {

    public static void main(String [] args){

        RentalStatusUpdater updater = new RentalStatusUpdater();
        updater.startScheduledStatusUpdate();

        ConsoleInterface.startApplication();
    }
}
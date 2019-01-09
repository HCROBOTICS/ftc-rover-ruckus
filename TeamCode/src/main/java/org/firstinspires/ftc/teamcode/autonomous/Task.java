package org.firstinspires.ftc.teamcode.autonomous;

public enum Task {
    LOWER("Lowering"), UNLATCH("Unlatching"), TURN_TOWARDS_MINERALS("Doing the rest"), END("Done");

    public String status; // This is reported to the user as Autonomous is running.

    Task(String status) {
        this.status = status;
    }
}

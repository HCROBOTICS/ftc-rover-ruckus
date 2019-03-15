package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

public abstract class Auto extends LinearOpMode {
    public enum Task {
        /* Since Enums are implicitly final, I have to include every darned possible Task in here... */
        LOWER("Lowering"),
        ROTATE("Rotating away from the Lander"),
        LOOK_AT_MINERALS("Using the latest in computer vision to find a plastic block"),
        MANEUVER_LEFT("Going for the mineral on the left"),
        MANEUVER_CENTER("Going for the mineral in the center"),
        MANEUVER_RIGHT("Going for the mineral on the right"),
        MANEUVER_DEPOT("Going to the Depot"),
        MANEUVER_CRATER("Going to the Crater"),
        END("Done"),
        /* These are kept for compatibility with our Drury-era Autos. */
        UNLATCH("Unlatching"),
        TURN_TOWARDS_MINERALS("Doing the rest");

        public String status; // This is reported to the user as Autonomous is running.

        Task(String status) {
            this.status = status;
        }
    }

    public Task task;
}
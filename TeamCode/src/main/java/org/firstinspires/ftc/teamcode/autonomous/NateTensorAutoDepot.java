package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Nate Tensor Depot", group = "Nate")
public class NateTensorAutoDepot extends NateTensorAuto {

    void maneuverDepot() {
        robot.omniWheels.stop_and_reset();
        telemetry.addData("Currently:", "Driving to Depot");
        telemetry.update();

        while (robot.omniWheels.getEncoderAverage() < (340 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 2700)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < (800 * TURN_COEFFICIENT))
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1500)
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(700);
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

        task = Task.MANEUVER_CRATER;
    }
}
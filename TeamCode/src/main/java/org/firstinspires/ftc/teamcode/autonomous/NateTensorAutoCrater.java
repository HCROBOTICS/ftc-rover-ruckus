package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

@Autonomous(name = "Nate Tensor Crater", group = "Nate")
public class NateTensorAutoCrater extends NateTensorAuto {

    void maneuverDepot() {
        robot.omniWheels.reset();
        telemetry.update();

        while ((robot.omniWheels.getEncoderAverage() * TURN_COEFFICIENT) < 500)
            robot.omniWheels.rotate(-ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);


        while ((robot.omniWheels.getEncoderAverage() * TURN_COEFFICIENT) < 250) {
            robot.omniWheels.rotate(-ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (((robot.omniWheels.getEncoderAverage() * TURN_COEFFICIENT) < 250)) {
            robot.omniWheels.rotate(-ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        while (robot.omniWheels.getEncoderAverage() < 1000) {
            robot.omniWheels.go(ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED, ROBOT_SPEED);
        } robot.omniWheels.stop_and_reset();
          sleep(SLEEP_BETWEEN_MOVEMENTS);

        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(SLEEP_BETWEEN_MOVEMENTS);
        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
        sleep(SLEEP_BETWEEN_MOVEMENTS);

        task = Task.MANEUVER_CRATER;
    }

    void maneuverCrater() {
        robot.omniWheels.stop_and_reset();
        telemetry.update();

        while (robot.omniWheels.getEncoderAverage() < 5000)
            robot.omniWheels.go(-ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED, -ROBOT_SPEED);
        robot.omniWheels.stop_and_reset();

        task = Task.END;
    }
}
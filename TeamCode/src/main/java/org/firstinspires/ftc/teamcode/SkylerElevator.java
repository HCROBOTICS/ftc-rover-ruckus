package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

/*
 * This class has been deprecated.
 * Instead, we use the LinearActuator class.
 */

public class SkylerElevator {
    private DcMotor motor = null;
    private static final int ELEVATOR_UP_POSITION = 10000;
    private static final int ELEVATOR_DOWN_POSITION = 0;

    private int desiredPosition = 0;

    public SkylerElevator(DcMotor motor) {
        this.motor = motor;
    }

    public void init() {
        motor.setDirection(DcMotor.Direction.FORWARD);
        motor.setPower(0);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public int elevate(double speed) {
        if (speed > 0)
            desiredPosition = ELEVATOR_UP_POSITION;
        else if (speed < 0)
            desiredPosition = ELEVATOR_DOWN_POSITION;
        motor.setPower(rampSpeed(speed));
        return motor.getCurrentPosition();
    }

    public double lower() {
        return elevate(-1);
    }

    private double rampSpeed(double speed) {
        boolean isSpeedNegative = false;
        if (speed < 0) { isSpeedNegative = true; speed = -speed; }
        int distance = Math.abs(desiredPosition - motor.getCurrentPosition());
        if (distance > 5000) speed = 1;
        else if (distance > 2500) speed = 0.5;
        else if (distance > 1000) speed = 0.25;
        else speed = 0;
        if (isSpeedNegative) speed = -speed;
        return speed;
    }
}

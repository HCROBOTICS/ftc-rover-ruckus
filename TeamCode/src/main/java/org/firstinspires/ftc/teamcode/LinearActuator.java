package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;

public class LinearActuator {
    private DcMotor motor;
    private int desiredPosition = 0;
    private int distance = 1001; // We initialize this to 1000 so that the autonomous will not think that it's at position before it actually is.

    private boolean modeDebug;

    public void setModeDebug(boolean mode) {
        modeDebug = mode;
    }

    private int upPosition = 16500;
    private int downPosition = 0;

    public int getUpPosition() {
        return upPosition;
    }

    public int getDownPosition() {
        return downPosition;
    }

    public void setUpPosition(int upPosition) {
        this.upPosition = upPosition;
    }

    public void setDownPosition(int downPosition) {
        this.downPosition = downPosition;
    }

    public int getElevatorPosition() {
        return motor.getCurrentPosition();
    }

    public int getDesiredPosition() {
        return desiredPosition;
    }

    public int getDistance() {
        return distance;
    }

    public LinearActuator(DcMotor motor, int upperLimit, int lowerLimit) {
        this.motor = motor;
        this.upPosition = upperLimit;
        this.downPosition = lowerLimit;
    }

    public LinearActuator(DcMotor motor) {
        this.motor = motor;
    }

    // Call this after initializing the motor.
    public void init() {
        motor.setPower(0);
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public void zero() {
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    public double elevate(double speed) {
        if (speed > 0)
            desiredPosition = upPosition;
        else if (speed < 0)
            desiredPosition = downPosition;
        motor.setPower(rampSpeed(speed));
        return speed;
    }

    private double rampSpeed(double speed) {
        boolean isSpeedNegative = false;
        if (speed < 0) { isSpeedNegative = true; speed = -speed; }
        distance = Math.abs(desiredPosition - motor.getCurrentPosition());
        if (!modeDebug) {
            if (distance > 5000) speed *= 1;
            else if (distance > 2500) speed *= 0.5;
            else if (distance > 1000) speed *= 0.25;
            else speed = 0;
        }
        if (isSpeedNegative) speed = -speed;
        return speed;
    }
}
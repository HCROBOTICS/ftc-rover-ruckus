package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * The OmniWheels class is an abstraction of the four omni-wheels on our robot.
 * You can move the robot by sending messages to an instance of this class 
 * instead of the four individual motors. You can instantiate it with the 
 * constructor below that takes references to the four motors it should control.
 * Move the robot with the go() method below, and as arguments give it data from
 * the first controller's joysticks.
 */

public class OmniWheels {
    enum DriveMode {STRAFE, JOHN}

    private DcMotor lf = null;
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;

    public DriveMode mode = null;

    // Here's our constructor.
    public OmniWheels(DcMotor lf, DcMotor rf, DcMotor lb, DcMotor rb, DriveMode mode) {
        this.lf = lf;
        this.lb = lb;
        this.rf = rf;
        this.rb = rb;
        this.mode = mode;
    }
    
    // Do some dumb maths.
    public void go(double left_stick_x, double left_stick_y, double right_stick_x) {
        if (mode == DriveMode.STRAFE) {
            lf.setPower(left_stick_y - right_stick_x - left_stick_x);
            rf.setPower(left_stick_y + right_stick_x + left_stick_x);
            lb.setPower(left_stick_y - right_stick_x + left_stick_x);
            rb.setPower(left_stick_y + right_stick_x - left_stick_x);
        } else if (mode == DriveMode.JOHN) {
            lf.setPower(left_stick_y - left_stick_x - right_stick_x);
            rf.setPower(left_stick_y + left_stick_x + right_stick_x);
            lb.setPower(left_stick_y - left_stick_x + right_stick_x);
            rb.setPower(left_stick_y + left_stick_x - right_stick_x);
        }
    }

    public void stop() {
        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
    }
}

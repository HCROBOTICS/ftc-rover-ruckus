package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.hardware.DcMotor;

import static com.qualcomm.robotcore.hardware.DcMotor.RunMode.*;
import static org.firstinspires.ftc.teamcode.OmniWheels.DriveMode.*;

/**
 * The OmniWheels class is an abstraction of the four omni-wheels on our robot.
 * You can move the robot by sending messages to an instance of this class 
 * instead of the four individual motors. You can instantiate it with the 
 * constructor below that takes references to the four motors it should control.
 * Move the robot with the go() method below, and as arguments give it data from
 * the first controller's joysticks.
 */

public class OmniWheels {
    /*
     * The two drivemodes can be used to configure the way you control the driving of the robot. STRAFE is the default.
     * JOHN is a custom one made for John's use, because he is smart and used science to determine the optimal control
     * map for certain maneuvers.
     */
    enum DriveMode {STRAFE, JOHN}

    private DcMotor lf = null;
    private DcMotor rf = null;
    private DcMotor lb = null;
    private DcMotor rb = null;

    public DriveMode mode = STRAFE;

    // Here's our constructor.
    public OmniWheels(DcMotor lf, DcMotor rf, DcMotor lb, DcMotor rb) {
        this.lf = lf;
        this.lb = lb;
        this.rf = rf;
        this.rb = rb;
    }

    public void setMode(DriveMode mode) {
        this.mode = mode;
    }
    
    // Do some dumb maths.
    public void goByDriver(double left_stick_x, double left_stick_y, double right_stick_x) {
        if (mode == STRAFE) {
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

    public void rotate(double speed) {
        lf.setPower(speed);
        rf.setPower(-speed);
        lb.setPower(speed);
        rb.setPower(-speed);
    }

    public void go(double lf, double rf, double lb, double rb) {
        this.lf.setPower(lf);
        this.rf.setPower(rf);
        this.lb.setPower(lb);
        this.rb.setPower(rb);
    }

    public void stop() {
        lf.setPower(0);
        rf.setPower(0);
        lb.setPower(0);
        rb.setPower(0);
    }

    public void reset() {
        lf.setMode(STOP_AND_RESET_ENCODER);
        rf.setMode(STOP_AND_RESET_ENCODER);
        lb.setMode(STOP_AND_RESET_ENCODER);
        rb.setMode(STOP_AND_RESET_ENCODER);
        lf.setMode(RUN_USING_ENCODER);
        rf.setMode(RUN_USING_ENCODER);
        lb.setMode(RUN_USING_ENCODER);
        rb.setMode(RUN_USING_ENCODER);
    }
}

package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.DcMotor;

public class NateArm extends Device {
    private DcMotor lift, slide;

    public NateArm(DcMotor lift, DcMotor slide) {
        this.lift = lift;
        this.slide = slide;
    }

    public void init() {}

    public void stop() {
        lift.setPower(0);
        slide.setPower(0);
    }

    public void liftByDriver(double rate) { lift.setPower(rate); }
    public void slideByDriver(double rate) { slide.setPower(rate); }

    public double getLiftPos() { return lift.getCurrentPosition(); }
    public double getSlidePos() { return slide.getCurrentPosition(); }
}

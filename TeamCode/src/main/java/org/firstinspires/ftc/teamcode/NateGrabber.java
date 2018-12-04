package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class NateGrabber {
    public static final double LEFT_OPEN = 0.5;
    public static final double RIGHT_OPEN = 0.4;
    public static final double MIDDLE_OPEN = 0;
    public static final double LEFT_CLOSED = 0.9;
    public static final double RIGHT_CLOSED = 0;
    public static final double MIDDLE_CLOSED = 0.7;

    public Servo left = null;
    public Servo right = null;
    public Servo middle = null;

    public NateGrabber(Servo left, Servo right, Servo middle) {
        this.left = left;
        this.right = right;
        this.middle = middle;
    }

    public void init() {
        open();
    }

    public void open() {
        left.setPosition(LEFT_OPEN);
        right.setPosition(RIGHT_OPEN);
        middle.setPosition(MIDDLE_CLOSED);
    }

    public void close() {
        left.setPosition(LEFT_CLOSED);
        right.setPosition(RIGHT_CLOSED);
        middle.setPosition(MIDDLE_CLOSED);
    }

    public void stop() {
        left.setPosition(LEFT_OPEN);
        right.setPosition(RIGHT_OPEN);
        middle.setPosition(MIDDLE_OPEN);
    }
}

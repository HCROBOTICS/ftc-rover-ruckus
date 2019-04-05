package org.firstinspires.ftc.teamcode.hardware;

import org.firstinspires.ftc.teamcode.OmniWheels;

public class GyroDriver implements Device{
    private OmniWheels wheels;
    public GyroDriver(OmniWheels wheels) {
        this.wheels = wheels;
    }
    public void init() {}
    public void stop() {}
}

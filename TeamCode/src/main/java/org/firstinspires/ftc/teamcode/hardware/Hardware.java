package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public abstract class Hardware {
    abstract void init(HardwareMap ahwMap);
    abstract void stop();
}

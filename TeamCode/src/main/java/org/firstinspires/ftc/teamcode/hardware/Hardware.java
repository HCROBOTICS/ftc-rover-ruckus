package org.firstinspires.ftc.teamcode.hardware;

import com.qualcomm.robotcore.hardware.HardwareMap;

public interface Hardware {
    abstract void init(HardwareMap ahwMap);
    abstract void stop();
}

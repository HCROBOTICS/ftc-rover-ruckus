package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.Hardware;

public abstract class Auto extends LinearOpMode {
    public Task task;
    public Hardware robot;
}


//lower. rotate, look at minerals, maneuver{l,c,r}
package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.hardware.NateHardware;

import static org.firstinspires.ftc.teamcode.autonomous.JoeyAuto.Task.LOWER;

public class JoeyAuto extends LinearOpMode {
    enum Task {
        LOWER("Lowering"),
        UNLATCH("Unlatching"),
        END("Done");

        public String status;
        Task(String status) {this.status = status;}
    }

    Task task = LOWER;

    NateHardware robot = new NateHardware();

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        telemetry.addData("", "");

        waitForStart();
        while (opModeIsActive()) {
            telemetry.addData("", "");
        }
    }
}

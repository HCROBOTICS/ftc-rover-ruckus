package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.Auto;
import org.firstinspires.ftc.teamcode.autonomous.Task;
import org.firstinspires.ftc.teamcode.hardware.SkylerHardware;

import static org.firstinspires.ftc.teamcode.autonomous.Task.*;

@Autonomous(name="Skyler Autonomous Crater", group ="Skyler")
public class SkylerAutoCrater extends Auto {
    SkylerHardware robot = new SkylerHardware();

    Task task = LOWER;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        telemetry.addData("Robot", "Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
            telemetry.addData("Robot", task.status);
            switch (task) {
                case LOWER: lower(); break;
                case UNLATCH: unlatch(); break;
                case TURN_TOWARDS_MINERALS: turnTowardsMinerals(); break;
                default: break;
            }
            telemetry.update();
        }

        robot.stop();
        telemetry.addData("Robot", "Stopping");
        telemetry.update();
    }

    void lower() {
        if (robot.elevator.getDistance() > 1000) {
            robot.elevator.elevate(1);
            telemetry.addData("Distance",robot.elevator.getDistance() - 1000);
        } else {
            robot.elevator.elevate(0);
            task = Task.UNLATCH;
        }
    }

    void unlatch() {
        telemetry.update();
        waitForAPress();
        robot.omniWheels.goByDriver(0, -0.5, 0);
        sleep(125);
        robot.omniWheels.stop();
         sleep(500);
        waitForAPress();
        task = TURN_TOWARDS_MINERALS;
    }

    void waitForAPress() {
        while (!gamepad1.a);
    }

    void turnTowardsMinerals() {
        telemetry.addData("Robot", "Doing the rest");
        telemetry.update();
        robot.omniWheels.reset();
        waitForAPress();
        //turn once unlatched
        while (robot.lf.getCurrentPosition() < 2200) {robot.omniWheels.rotate(-0.25);}
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(600);
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //drive back
        robot.omniWheels.goByDriver(0,.5,0);
        sleep(200);
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //left turn #1
        robot.omniWheels.goByDriver(0,0,0.5);
        sleep(500);
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //drive forward a bit
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(700);
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //left turn #2
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(150);
        robot.omniWheels.stop();
        sleep(500);
        waitForAPress();
        //drive forward to depot
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1000);
        robot.omniWheels.stop();
        sleep (500);
        waitForAPress();
        //Drop Team Piece Code Goes Here
        //drive backwards to crater
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep (1700);
        robot.omniWheels.stop();

        task = END;
    }
}
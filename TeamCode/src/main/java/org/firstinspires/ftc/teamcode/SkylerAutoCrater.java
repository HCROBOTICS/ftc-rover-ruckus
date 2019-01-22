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

    public static final double SERVO_DROP_POSITION = 1;
    public static final double SERVO_HOLD_POSITION = 0;
    public static final int SLEEP_BETWEEN_TASKS = 500;

    Task task = LOWER;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);

        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

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
        robot.omniWheels.goByDriver(0, -0.5, 0);
        sleep(125);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);
        task = TURN_TOWARDS_MINERALS;
    }



    void turnTowardsMinerals() {
        telemetry.addData("Robot", "Doing the rest");
        telemetry.update();
        robot.omniWheels.reset();

        //turn once unlatched
        while (robot.lf.getCurrentPosition() < 2200) {robot.omniWheels.rotate(-0.25);}
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(700);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive back
        robot.omniWheels.goByDriver(0,.5,0);
        sleep(300);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //left turn #1
        robot.omniWheels.goByDriver(0,0,0.5);
        sleep(500);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward a bit
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1000);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //left turn #2
        robot.omniWheels.goByDriver(0,0,0.5);
        sleep(500);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward to depot
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1500);
        robot.omniWheels.stop();
        sleep (SLEEP_BETWEEN_TASKS);

        //drop team piece
        robot.sweeper.setPower(.5);
        sleep(SLEEP_BETWEEN_TASKS);
        robot.sweeper.setPower(0);

        //drive backwards to crater
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep (2000);
        robot.omniWheels.stop();

        task = END;
            }
}
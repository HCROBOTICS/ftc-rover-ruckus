package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.SkylerHardware;

import static org.firstinspires.ftc.teamcode.SkylerAutoDepot.Task.*;

@Autonomous(name="Skyler Autonomous Depot", group ="Skylar")
public class SkylerAutoDepot extends LinearOpMode {
    SkylerHardware robot = new SkylerHardware();

    public static final double SERVO_DROP_POSITION = 1;
    public static final double SERVO_HOLD_POSITION = 0;

    public static final int SLEEP_BETWEEN_TASKS = 700;

    enum Task {LOWER, UNLATCH, TURN_TOWARDS_MINERALS, END}
    Task task;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        task = LOWER;

        robot.teamPiece.setPosition(SERVO_HOLD_POSITION);

        telemetry.addData("Robot", "Ready");
        telemetry.update();
        waitForStart();

        while (opModeIsActive()) {
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
        while (robot.elevator.getDistance() > 1000) {
            robot.elevator.elevate(1);
            telemetry.addData("Robot","Lowering");
            telemetry.addData("Distance",robot.elevator.getDistance() - 1000);
        }
        robot.elevator.elevate(0);
        task = Task.UNLATCH;
    }

    void unlatch() {
        telemetry.addData("Robot", "Unlatching");
        telemetry.update();
        robot.omniWheels.goByDriver(0, -0.5, 0);
        sleep(125);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);
        task = TURN_TOWARDS_MINERALS;
    }

    void turnTowardsMinerals() {
        telemetry.addData("Robot", "Turning towards the Minerals");
        telemetry.update();
        robot.omniWheels.reset();

        //turn once unlatched
        while (robot.lf.getCurrentPosition() < 2200) {robot.omniWheels.rotate(-0.25);}
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(2000);
        robot.omniWheels.stop();

        //drop team piece
        robot.sweeper.setPower(.5);
        sleep(1000);
        robot.sweeper.setPower(0);

        //turn so back faces
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(450);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive backward
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep(3000);
        robot.omniWheels.stop();

        task = END;
    }
}
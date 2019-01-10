package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.NateHardware;

import static org.firstinspires.ftc.teamcode.NateAutoCrater.Task.*;

@Autonomous(name="Nate Autonomous Crater", group ="Nate")
public class NateAutoCrater extends LinearOpMode {
    NateHardware robot = new NateHardware();

    public static final double SERVO_DROP_POSITION = 0;
    public static final double SERVO_HOLD_POSITION = 1;

    enum Task {LOWER, UNLATCH, TURN_TOWARDS_MINERALS, END}
    Task task;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        task = LOWER;

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
        task = Task.TURN_TOWARDS_MINERALS;
    }

    void unlatch() {
        telemetry.addData("Robot", "Unlatching");
        telemetry.update();
        robot.omniWheels.goByDriver(0, -0.5, 0);
        sleep(125);
        robot.omniWheels.stop();
        sleep(500);
        task = TURN_TOWARDS_MINERALS;
    }

    void turnTowardsMinerals() {
        telemetry.addData("Robot", "Doing the rest");
        telemetry.update();
        robot.omniWheels.reset();
        //turn once unlatched
        while (robot.lf.getCurrentPosition() < 1100) {robot.omniWheels.rotate(-0.25);}
        robot.omniWheels.stop();
        sleep(500);
        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(500);
        robot.omniWheels.stop();
        sleep(500);
        //drive back
        robot.omniWheels.goByDriver(0,.5,0);
        sleep(200);
        robot.omniWheels.stop();
        sleep(500);
        //left turn #1
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(500);
        robot.omniWheels.stop();
        sleep(500);
        //drive forward a bit
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(700);
        robot.omniWheels.stop();
        sleep(500);
        //left turn #2
        robot.omniWheels.goByDriver(0,0,-0.5);
        sleep(150);
        robot.omniWheels.stop();
        sleep(500);
        //drive forward to depot
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1000);
        robot.omniWheels.stop();
        sleep (500);
        robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(500);
        //drive backwards to crater
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep (1700);
        robot.omniWheels.stop();

        task = END;
    }
}
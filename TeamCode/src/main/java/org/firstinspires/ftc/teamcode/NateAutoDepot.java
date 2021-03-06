package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.hardware.NateHardware;

import static org.firstinspires.ftc.teamcode.NateAutoDepot.Task.*;

@Autonomous(name="Nate Non-Tensor Depot", group ="Nate")
public class NateAutoDepot extends LinearOpMode {
    NateHardware robot = new NateHardware();

    public static final double SERVO_DROP_POSITION = .7;
    public static final double SERVO_HOLD_POSITION = 0;
    public static final int SLEEP_BETWEEN_TASKS = 500;

    enum Task {LOWER, UNLATCH, TURN_TOWARDS_MINERALS, END}
    Task task;

    @Override
    public void runOpMode() {
        robot.init(hardwareMap);
        task = LOWER;
        //robot.teamPiece.setPosition(SERVO_HOLD_POSITION);
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
        if (robot.elevator.getDistance() > 1000) {
            robot.elevator.elevate(1);
            telemetry.addData("Robot","Lowering");
            telemetry.addData("Distance",robot.elevator.getDistance() - 1000);
        } else {
            robot.elevator.elevate(0);
            task = Task.TURN_TOWARDS_MINERALS;
        }
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
        while (robot.lf.getCurrentPosition() < 1100) {robot.omniWheels.rotate(-0.25);}
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive forward
        robot.omniWheels.goByDriver(0,-0.5,0);
        sleep(1200);
        robot.omniWheels.stop();

        //drop team piece
        //robot.teamPiece.setPosition(SERVO_DROP_POSITION);
        sleep(SLEEP_BETWEEN_TASKS);

        //turn so back faces
        robot.omniWheels.goByDriver(0,0,0.5);
        sleep(350);
        robot.omniWheels.stop();
        sleep(SLEEP_BETWEEN_TASKS);

        //drive backward
        robot.omniWheels.goByDriver(0,0.5,0);
        sleep(2300);
        robot.omniWheels.stop();

        task = END;
    }
}
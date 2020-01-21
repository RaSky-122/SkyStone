package org.firstinspires.ftc.teamcode.main.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@Autonomous(name = "Wall Park BOTH", group = "both")

public class ParkingWall extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;

    Init init = new Init();
    Motors motors = new Motors();

    @Override
    public void runOpMode() throws InterruptedException {
        init.wheels();

        while(!opModeIsActive() && !isStopRequested()) {
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        if(opModeIsActive()){

            motors.forward(0.5, 1000);
        }
    }

    class Init {
        public void wheels() {

            frontRightMotor = hardwareMap.dcMotor.get("frontRight");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeft");
            backRightMotor = hardwareMap.dcMotor.get("backRight");
            backLeftMotor = hardwareMap.dcMotor.get("backLeft");

            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        }
    }

    class Motors {

        public void stop (){
            frontRightMotor.setPower(0);
            frontLeftMotor.setPower(0);
            backRightMotor.setPower(0);
            backLeftMotor.setPower(0);

            sleep(200);
        }

        public void forward (double power, long target) {
            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(power);
            backRightMotor.setPower(power);
            backLeftMotor.setPower(power);

            sleep(target);
            stop();
        }
    }
}

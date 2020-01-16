package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

@Autonomous(name = "Parking Bridge LEFT", group = "main")

public class Main_AutonomousOpHunedoaraBridge extends LinearOpMode {

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

            motors.forward(0.5, 1450);
            motors.left(0.5, 1200);
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

        public void left (double power, long target){
            frontRightMotor.setPower(power);
            frontLeftMotor.setPower(-power);
            backRightMotor.setPower(-power);
            backLeftMotor.setPower(power);

            sleep(target);
            stop();
        }
    }
}

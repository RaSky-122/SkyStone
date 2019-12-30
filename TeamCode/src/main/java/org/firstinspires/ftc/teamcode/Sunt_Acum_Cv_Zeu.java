package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

@Disabled
@TeleOp
public class Sunt_Acum_Cv_Zeu extends LinearOpMode {

    private DcMotor frontRightMotor;
    private DcMotor frontLeftMotor;
    private DcMotor backRightMotor;
    private DcMotor backLeftMotor;
    private DcMotor liftMotor;
    private Servo catchServo;
    private CRServo liftServo;

    Init init = new Init();
    Motors motors = new Motors();

    @Override
    public void runOpMode() throws InterruptedException {
        init.wheels();
        init.lift();
        while (!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while (opModeIsActive()) {
            motors.Smoothmovement();


            if (gamepad2.right_stick_y < -0.1)
                liftMotor.setPower(0.6);
            else if (gamepad2.right_stick_y > 0.1)
                liftMotor.setPower(-0.6);
            else liftMotor.setPower(0);
        }
    }

    class Servos {
        public void catchServo(){
            if(gamepad2.a){
                catchServo.setPosition(catchServo.getPosition() + 0.01);
                sleep(20);
            }
            else if(gamepad2.y) {
                catchServo.setPosition(catchServo.getPosition() - 0.01);
                sleep(20);
            }
        }
    }

    class Init {

        public void wheels() {
            frontRightMotor = hardwareMap.dcMotor.get("frontRightMotor");
            frontLeftMotor = hardwareMap.dcMotor.get("frontLeftMotor");
            backRightMotor = hardwareMap.dcMotor.get("backRightMotor");
            backLeftMotor = hardwareMap.dcMotor.get("backLeftMotor");

            //////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
            frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

            /////////////////////////////////////////////////////////////////////////////

            frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }

        public void lift() {
            liftMotor = hardwareMap.dcMotor.get("liftMotor");
            catchServo = hardwareMap.servo.get("catchServo");
            liftServo = hardwareMap.crservo.get("liftServo");

            liftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
            liftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }
    }

    class Motors {
        int directie = 0;
        double power = 0.5;

        public void Smoothmovement() {
            if (directie == 0) {
                if (gamepad1.dpad_up) {
                    frontLeftMotor.setPower(power + gamepad1.right_stick_x / 2);
                    frontRightMotor.setPower(power - gamepad1.right_stick_x / 2);
                    backLeftMotor.setPower(power - gamepad1.right_stick_x / 2);
                    backRightMotor.setPower(power + gamepad1.right_stick_x / 2);
                } else if (gamepad1.dpad_down) {
                    frontLeftMotor.setPower(-power + gamepad1.right_stick_x / 2);
                    frontRightMotor.setPower(-power - gamepad1.right_stick_x / 2);
                    backLeftMotor.setPower(-power - gamepad1.right_stick_x / 2);
                    backRightMotor.setPower(-power + gamepad1.right_stick_x / 2);
                } else if (gamepad1.dpad_right) {
                    frontLeftMotor.setPower(power + gamepad1.right_stick_x / 2);
                    frontRightMotor.setPower(-power - gamepad1.right_stick_x / 2);
                    backLeftMotor.setPower(power - gamepad1.right_stick_x / 2);
                    backRightMotor.setPower(-power + gamepad1.right_stick_x / 2);
                } else if (gamepad1.dpad_left) {
                    frontLeftMotor.setPower(-power + gamepad1.right_stick_x / 2);
                    frontRightMotor.setPower(power - gamepad1.right_stick_x / 2);
                    backLeftMotor.setPower(-power - gamepad1.right_stick_x / 2);
                    backRightMotor.setPower(power + gamepad1.right_stick_x / 2);
                } else {
                    frontLeftMotor.setPower(gamepad1.right_stick_x / 2);
                    frontRightMotor.setPower(-gamepad1.right_stick_x / 2);
                    backLeftMotor.setPower(-gamepad1.right_stick_x / 2);
                    backRightMotor.setPower(gamepad1.right_stick_x / 2);
                }

            }

        }

    }
}





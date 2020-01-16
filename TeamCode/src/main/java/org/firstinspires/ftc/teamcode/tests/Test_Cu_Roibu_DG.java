package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import javax.crypto.spec.OAEPParameterSpec;

@TeleOp

public class Test_Cu_Roibu_DG extends LinearOpMode {

    private DcMotor roataStangaJos;
    private DcMotor roataDreaptaJos;
    private DcMotor roataStangaSus;
    private DcMotor roataDreaptaSus;

    private DcMotor liftStanga;
    private DcMotor liftDreapta;

    private Servo ghearaDreapta;
    private Servo ghearaStanga;
    private CRServo gripper;
    private CRServo extensor;

    Init init = new Init();

    Roti roti = new Roti();

    public void runOpMode() {
        init.roti();
        while (!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Astept startul", " mosule");
            telemetry.update();
        }

        while (opModeIsActive()){



            if(gamepad1.left_stick_y <= -0.2)
                roti.fata();

            if(gamepad1.left_stick_y >= 0.2)
                roti.spate();

        }
    }

    class Roti {
        public void fata() {
            roataDreaptaJos.setPower(0.5);
            roataDreaptaSus.setPower(0.5);
            roataStangaJos.setPower(0.5);
            roataStangaSus.setPower(0.5);

        }
        public void spate() {
            roataDreaptaJos.setPower(-0.5);
            roataDreaptaSus.setPower(-0.5);
            roataStangaJos.setPower(-0.5);
            roataStangaSus.setPower(-0.5);
        }
        public void straightLeft() {
            roataDreaptaJos.setPower(-0.5);
            roataDreaptaSus.setPower(0.5);
            roataStangaJos.setPower(0.5);
            roataStangaSus.setPower(-0.5);
        }
        public void straightRight() {
                roataDreaptaJos.setPower(0.5);
                roataDreaptaSus.setPower(-0.5);
                roataStangaJos.setPower(-0.5);
                roataStangaSus.setPower(0.5);
        }
        public void rotireLeft() {
            roataDreaptaJos.setPower(0.5);
            roataDreaptaSus.setPower(0.5);
            roataStangaJos.setPower(-0.5);
            roataStangaSus.setPower(-0.5);
        }
        public void rotireRight() {
            roataDreaptaJos.setPower(-0.5);
            roataDreaptaSus.setPower(-0.5);
            roataStangaJos.setPower(0.5);
            roataStangaSus.setPower(0.5);
        }
        public void stop() {
            roataDreaptaJos.setPower(0);
            roataDreaptaSus.setPower(0);
            roataStangaJos.setPower(0);
            roataStangaSus.setPower(0);
        }
    }

    class Init {
        public void roti() {
            roataDreaptaJos = hardwareMap.dcMotor.get("roataDreaptaJos");
            roataDreaptaSus = hardwareMap.dcMotor.get("roataDreaptaSus");
            roataStangaJos = hardwareMap.dcMotor.get("roataStangaJos");
            roataStangaSus = hardwareMap.dcMotor.get("roataStangaSus");

            roataDreaptaSus.setDirection(DcMotorSimple.Direction.FORWARD);
            roataDreaptaJos.setDirection(DcMotorSimple.Direction.FORWARD);
            roataStangaJos.setDirection(DcMotorSimple.Direction.REVERSE);
            roataStangaSus.setDirection(DcMotorSimple.Direction.REVERSE);

            roataDreaptaSus.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            roataStangaJos.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            roataStangaSus.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            roataDreaptaJos.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        }
    }
}

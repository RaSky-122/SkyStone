package org.firstinspires.ftc.teamcode.learning;

import android.webkit.WebHistoryItem;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

//@Autonomous(name = "Creatia lui Gowga", group = "test")
@Disabled
public class SelfLearn extends LinearOpMode {
    private DcMotor motorFataDreapta;
    private DcMotor motorFataStanga;
    private DcMotor motorSpateDreapta;
    private DcMotor motorSpateStanga;

    Init numeNebun = new Init();
    Mers deplasare = new Mers();
    public void runOpMode(){
        numeNebun.numeMisto();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }


            deplasare.fata(0.3);

            sleep(500);

            deplasare.oprit();
            sleep(1000);
            deplasare.viraj(0.3);

            sleep(500);
            deplasare.oprit();

            sleep(1000);
            deplasare.fata(0.3);

            sleep(500);
            deplasare.oprit();
    }

    class Init{
        public void numeMisto(){
            motorFataDreapta = hardwareMap.dcMotor.get("1");
            motorFataStanga = hardwareMap.dcMotor.get("2");
            motorSpateDreapta = hardwareMap.dcMotor.get("3");
            motorSpateStanga = hardwareMap.dcMotor.get("4");

            motorFataDreapta.setDirection(DcMotorSimple.Direction.FORWARD);
            motorFataStanga.setDirection(DcMotorSimple.Direction.REVERSE);
            motorSpateDreapta.setDirection(DcMotorSimple.Direction.FORWARD);
            motorSpateStanga.setDirection(DcMotorSimple.Direction.REVERSE);

            motorFataDreapta.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorFataStanga.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorSpateDreapta.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            motorSpateStanga.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        }


    }

    class Mers{
        public void oprit(){
            motorFataDreapta.setPower(0);
            motorFataStanga.setPower(0);
            motorSpateDreapta.setPower(0);
            motorSpateStanga.setPower(0);
        }
        public void fata(double power){
            motorFataDreapta.setPower(power);
            motorFataStanga.setPower(power);
            motorSpateDreapta.setPower(power);
            motorSpateStanga.setPower(power);
        }
        public void viraj(double power){
            motorFataDreapta.setPower(power);
            motorFataStanga.setPower(0);
            motorSpateDreapta.setPower(power);
            motorSpateStanga.setPower(0);
        }
    }
}

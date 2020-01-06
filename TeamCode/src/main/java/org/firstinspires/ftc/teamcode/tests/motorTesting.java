package org.firstinspires.ftc.teamcode.tests;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "Motor Testing", group = "test")

public class motorTesting extends LinearOpMode {

    private DcMotor Ion;
    private DcMotor Maria;
    AllAboutIon cevreitu = new AllAboutIon();

    double power = 0.1;

    public void runOpMode(){

        cevreitu.init();

        while(!opModeIsActive() && !isStopRequested()){
            telemetry.addData("Waiting for ", "start.");
            telemetry.update();
        }

        while(opModeIsActive()){
            cevreitu.movement();

            telemetry.addData("Motor1 Power: ", Ion.getPower());
            telemetry.addData("Motor2 Power: ", Maria.getPower());
            //telemetry.addData("Motor Position: ", Ion.getCurrentPosition());
            telemetry.update();
        }
    }
    class AllAboutIon {

        public void init(){
            Ion = hardwareMap.dcMotor.get("Motor1");
            Maria = hardwareMap.dcMotor.get("Motor2");

            Ion.setDirection(DcMotorSimple.Direction.FORWARD);
            Maria.setDirection(DcMotorSimple.Direction.REVERSE);

            Ion.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
            Maria.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

            //Ion.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            //Ion.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        public void movement() {

            if(gamepad1.dpad_left && power >= 0){
                power -= 0.05;
                sleep(200);
            }
            else if(gamepad1.dpad_right && power <= 100) {
                power += 0.05;
                sleep(200);
            }

            if(gamepad1.dpad_up) {
                Ion.setPower(power);
                Maria.setPower(power);
            }
            else if(gamepad1.dpad_down) {
                Ion.setPower(-power);
                Maria.setPower(-power);
            }
            else {
                Ion.setPower(0);
                Maria.setPower(0);
            }
        }
    }
}

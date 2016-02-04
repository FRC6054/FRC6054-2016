
package org.usfirst.frc.team6054.robot;


import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.IterativeRobot;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Victor;
import edu.wpi.first.wpilibj.interfaces.Accelerometer;
import edu.wpi.first.wpilibj.vision.USBCamera;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import java.net.InetAddress;
import java.util.concurrent.TimeUnit;
import com.ni.vision.NIVision;
import edu.wpi.first.wpilibj.BuiltInAccelerometer;
import edu.wpi.first.wpilibj.SensorBase;
import edu.wpi.first.wpilibj.GyroBase;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.tables.ITable;
import edu.wpi.first.wpilibj.tables.TableKeyNotDefinedException;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Solenoid;
import edu.wpi.first.wpilibj.PowerDistributionPanel;
import edu.wpi.first.wpilibj.VictorSP;
import edu.wpi.first.wpilibj.Ultrasonic.Unit;



/**
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the IterativeRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 */



public class Robot extends IterativeRobot {

	DriverStation station = DriverStation.getInstance();
	
	Joystick Left = new Joystick(0);
	Joystick Right = new Joystick(1);
	
	Victor LeftMotor1 = new Victor(0);
	Victor LeftMotor2 = new Victor(1);
	Victor RightMotor1 = new Victor(2);
	Victor RightMotor2 = new Victor(3);
	
	VictorSP rightarmMotor1 = new VictorSP(4);
	VictorSP rightarmMotor2 = new VictorSP(5);
	VictorSP leftarmMotor1 = new VictorSP(6);
	VictorSP leftarmMotor2 = new VictorSP(7);
	VictorSP LEDRing = new VictorSP(8);
	
	Solenoid rightArm = new Solenoid(0);
	Solenoid leftArm = new Solenoid(1);
	
	Servo Swill = new Servo(9);
	
	Ultrasonic fLSonar = new Ultrasonic(0, 1);
	Ultrasonic bLSonar = new Ultrasonic(2, 3);
	Ultrasonic frontSonar = new Ultrasonic(4, 5);
	
	CameraServer server;	
	USBCamera camera = new USBCamera ("cam0");
	NIVision.Image frame = NIVision.imaqCreateImage(NIVision.ImageType.IMAGE_RGB, 0);
	
	Timer Timer = new Timer(); 
	Timer Timer2 = new Timer();
	
	public double doubleabs(double number) {
		if(number < 0) {
			number *= -1;
		}
		return number;
	}

	
	public void robotInit() {

		frontSonar.setEnabled(true);
		bLSonar.setEnabled(true);
		fLSonar.setEnabled(true);
		
    	camera.openCamera();
    	camera.startCapture();
    	camera.getImage(frame);
    	CameraServer.getInstance().setImage(frame);
    	server = CameraServer.getInstance();
    	server.setQuality(50);
    	server.startAutomaticCapture(camera);
    	
		}


	public void disabledInit(){

    }
	
	public void disabledPeriodic() {
	
	}

    public void autonomousInit() {
    	
    	frontSonar.setAutomaticMode(true);
    	frontSonar.updateTable();
    	frontSonar.startLiveWindowMode();
    	
    	fLSonar.setAutomaticMode(true);
    	fLSonar.updateTable();
    	fLSonar.startLiveWindowMode();
    	
    	bLSonar.setAutomaticMode(true);
    	bLSonar.updateTable();
    	bLSonar.startLiveWindowMode();
    	
    	camera.setExposureManual(0);
    	camera.setWhiteBalanceManual(7366);
    	camera.updateSettings();
     	
     	
     	Timer.start();

    }

    public void autonomousPeriodic() {
    	
//    	double LED = 1;
//    	LEDRing.set(LED);
    	
    	double FS = frontSonar.getRangeInches();
    	double BLS = bLSonar.getRangeInches();
    	double FLS = fLSonar.getRangeInches();
    	
    	NetworkTable.setServerMode();
    	NetworkTable datatable = NetworkTable.getTable("datatable");
    	double x = datatable.getNumber("COG_X", 0.0);
    	double y = datatable.getNumber("COG_Y", 0.0);
    	
    	double time = Timer.get();
    	
    	while (time<5) {
    		time = Timer.get();
    		
    		LeftMotor1.set(0.3);
    		LeftMotor2.set(0.3);
    		RightMotor1.set(-0.3);
    		RightMotor2.set(-0.3);
    		
    	}
    	
    	Timer2.start();
    	
    	while (FS>70) {
    		FS = frontSonar.getRangeInches();
        	BLS = bLSonar.getRangeInches();
        	FLS = fLSonar.getRangeInches();
    		
    		LeftMotor1.set(0.3);
    		LeftMotor2.set(0.3);
    		RightMotor1.set(-0.3);
    		RightMotor2.set(-0.3);
    		
    		if (FLS>15) {
    			FS = frontSonar.getRangeInches();
            	BLS = bLSonar.getRangeInches();
            	FLS = fLSonar.getRangeInches();
    			double slowDownConstant = FLS * 0.025;
    			
    			LeftMotor1.set((0.3 - slowDownConstant));
        		LeftMotor2.set((0.3 - slowDownConstant));
        		RightMotor1.set(-0.3);
        		RightMotor2.set(-0.3);
    		}
    		else if (FLS<15) {
    			FS = frontSonar.getRangeInches();
            	BLS = bLSonar.getRangeInches();
            	FLS = fLSonar.getRangeInches();
        		
        		LeftMotor1.set(0.3);
        		LeftMotor2.set(0.3);
        		RightMotor1.set(-0.3);
        		RightMotor2.set(-0.3);
    		}
    		if (BLS>15) {
    			FS = frontSonar.getRangeInches();
            	BLS = bLSonar.getRangeInches();
            	FLS = fLSonar.getRangeInches();
    			double slowDownConstant = FLS * 0.025;
    			
    			LeftMotor1.set(0.3);
        		LeftMotor2.set(0.3);
        		RightMotor1.set(-0.3 + slowDownConstant);
        		RightMotor2.set(-0.3 + slowDownConstant);
    		}
    		else if (BLS<15) {
    			FS = frontSonar.getRangeInches();
            	BLS = bLSonar.getRangeInches();
            	FLS = fLSonar.getRangeInches();
        		
        		LeftMotor1.set(0.3);
        		LeftMotor2.set(0.3);
        		RightMotor1.set(-0.3);
        		RightMotor2.set(-0.3);
    		}
        		
    	}
    	
    	double time2 = Timer2.get();
    	
        while (time2 < 0.8) {
        	time2 = Timer2.get();
        	
        	LeftMotor1.set(0.3);
        	LeftMotor2.set(0.3);                 
        	
        	RightMotor1.set(0);
        	RightMotor2.set(0);
        	
        }
    	
        LEDRing.set(1.0);
        
        while (y>300) {
        	y = datatable.getNumber("COG_Y", 0.0);
        	while (x>200 && x<400) {
        		x = datatable.getNumber("COG_X", 0.0);
        	
        		LeftMotor1.set(0.3);
        		LeftMotor2.set(0.3);                 
    	
        		RightMotor1.set(-0.3);
        		RightMotor2.set(-0.3);
        }
        	
        	while (x > 400) {
        		x = datatable.getNumber("COG_X", 0.0);
    		
        		LeftMotor1.set(0.3);
        		LeftMotor2.set(0.3);                 
        	
        		RightMotor1.set(0);
        		RightMotor2.set(0);
    	}
        	while (x < 200) {
        		x = datatable.getNumber("COG_X", 0.0);
    		
        		LeftMotor1.set(0);
        		LeftMotor2.set(0);                 
        	
        		RightMotor1.set(-0.3);
        		RightMotor2.set(-0.3);
    	}
        }
    		   	
    }


	public void teleopInit() {
		camera.setBrightness(50);
		camera.setWhiteBalanceAuto();
		camera.setExposureAuto();
    	camera.updateSettings();
    
    	Timer.stop();
    	Timer2.stop();
	}

    
    public void teleopPeriodic() {
    	double Rightaxis = Left.getRawAxis(4);
    	double Leftaxis = Left.getRawAxis(0);                 //Variables for control
    	double Righttrigger = (Left.getRawAxis(3) * -1);    
    	double Lefttrigger = Left.getRawAxis(2);
    	double Rightspeed = Left.getRawAxis(3);
    	double Leftspeed = Left.getRawAxis(3);
    	double RightspeedReverse = Left.getRawAxis(2);        //Variables for control
    	double LeftspeedReverse = Left.getRawAxis(2);
    	double SwillP;
    	boolean rbON = true;
    	boolean lbON = true;
    	boolean ybON = true;
    	boolean bbON = true;
    	boolean abON = true;
    	double LED = 1.0;
    	
    	if (abON = Left.getRawButton(1) && LED == 1) {
    		LEDRing.set(0);
    		LED = 0;
    	}
    
    	else if (abON = Left.getRawButton(1)) {
    		LEDRing.set(1);
    		LED = 1;
    	}
    	
    	
    	if (doubleabs(Rightaxis) < .1) {
    		Rightaxis = 0;                                 //deadzone
    	}
    	while (Left.getRawAxis(4) > 0.2 || Left.getRawAxis(4) < -0.2)  {
    		LeftMotor1.set((Left.getRawAxis(4)));
        	LeftMotor2.set((Left.getRawAxis(4)));
        	                                                    //zeroturn
    		RightMotor1.set((Left.getRawAxis(4)));
        	RightMotor2.set((Left.getRawAxis(4)));
    	}
    	
    	if (Leftaxis > 0.1) {
    		Leftaxis *= -1;                     //forward turning
    		Rightspeed *= Leftaxis + 1;
    	}
    	
    	else if (Leftaxis < -0.1) {
    		Leftspeed *= Leftaxis + 1;          //forward turning
    	}
    	
    	
    	
    	while (Lefttrigger > 0.1) {
    		Lefttrigger = Left.getRawAxis(2);
    		Leftaxis = Left.getRawAxis(0);
    		RightspeedReverse = Left.getRawAxis(2);        //Variables for control
        	LeftspeedReverse = Left.getRawAxis(2);
        	
    		if (Leftaxis > 0.1) {   
    			Leftaxis *= -1; 
    			RightspeedReverse *= Leftaxis + 1;
    	}
    	
    		else if (Leftaxis < -0.1) {
    			LeftspeedReverse *= Leftaxis + 1;          //forward turning
    	}
    		LeftMotor1.set(((LeftspeedReverse) * -1));
    		LeftMotor2.set(((LeftspeedReverse) * -1));
    		
    		RightMotor1.set((RightspeedReverse));
    		RightMotor2.set((RightspeedReverse));
    	}

    	
    	
    	LeftMotor1.set(Leftspeed);
    	LeftMotor2.set(Leftspeed);                  //setting motors
    	
		RightMotor1.set(Rightspeed * -1);
    	RightMotor2.set(Rightspeed * -1);           //setting motors
    	
    	if (rbON = Left.getRawButton(6)) {
    		Swill.set(0.2);
    	}
    	
    	if (lbON = Left.getRawButton(5)) {
    		Swill.set(0);
    	}
    	
    	if (ybON = Left.getRawButton(4)) {
    		Swill.set(0.1);
    	}
    	if (bbON = Left.getRawButton(2)) {
    		Swill.set(1);
    	}
    	
    }

   

	public void testPeriodic() {

     	
    	
    }
}	
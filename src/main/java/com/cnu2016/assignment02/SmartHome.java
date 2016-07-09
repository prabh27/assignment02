package com.cnu2016.assignment02;

import java.util.Timer;
import java.util.TimerTask;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class SmartHome {
    public static void main(String... args) {
        AirConditioner airConditioner = new AirConditioner();
        ArrayList<Appliance> list = new ArrayList<Appliance>();
        WaterHeater waterHeater = new WaterHeater();
        CookingOven cookingOven = new CookingOven();
        list.add(airConditioner);
        list.add(waterHeater);
        list.add(cookingOven);
        try {  
			File file = new File("./inputFile");
			FileReader fileReader = new FileReader(file);
			BufferedReader bufferedReader = new BufferedReader(fileReader);
			String line;
			long t = 0;
			while ((line = bufferedReader.readLine()) != null) {
			    System.out.println(line);
			    if(line.charAt(0) == '1')
			    {
			       if(line.charAt(2) == '1')
			       {
			         list.get(0).ScheduleEvent(list.get(0), list, t + 100);
			         t = t + 100;
		           }
			    }
			    else if(line.charAt(0) == '2')
			    {
			       if(line.charAt(2) == '1')
			       {
			         list.get(1).ScheduleEvent(list.get(1), list, t + 100);
			         t = t + 100;
		           }
			    }
			    else if(line.charAt(0) == '3')
			    {
			       if(line.charAt(2) == '1')
			       {
			         list.get(2).ScheduleEvent(list.get(2), list, t + 100);
			         t = t + 100;
		           }
			    }
			    
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		} 
        }

    
}

class Appliance {
    public boolean state = false;
    public void ScheduleEvent(Appliance modifiedAppliance, ArrayList<Appliance> list, long delayTime) {
        TimerTask tasknew = new MyTimerTask(modifiedAppliance, list);
        Timer timer = new Timer();
        timer.schedule(tasknew, delayTime); 
    }
    
}

class MyTimerTask extends TimerTask {
    List<Appliance> list = new ArrayList<Appliance>();
    public MyTimerTask(Appliance modifiedAppliance, ArrayList<Appliance> list) {
        if(modifiedAppliance.state == true)
        {
            modifiedAppliance.state = false;

        }
        else
        {
            modifiedAppliance.state = true;
        }
        this.list = list;
    }
    
    public void run() {
        System.out.print("Airconditioner State: ");
        System.out.println(list.get(0).state);
        System.out.print("WaterHeater State: ");
        System.out.println(list.get(1).state); 
        System.out.print("CookingOven: ");
        System.out.println(list.get(2).state);
        return;
    }
}

class AirConditioner extends Appliance {
    public AirConditioner() {
        super();
    }
}

class CookingOven extends Appliance {
    public CookingOven() {
        super();
    }
}

class WaterHeater extends Appliance {
    public WaterHeater() {
        super();
    }
}

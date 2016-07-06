package com.cnu2016.assignment02;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class SmartHomeTest {
    @Test
  public void checkTestCase() {
       SmartHome s = new SmartHome();
       AirConditioner airConditioner = new AirConditioner();
        WaterHeater waterHeater = new WaterHeater();
        CookingOven cookingOven = new CookingOven();
        s.list.add(airConditioner);
        s.list.add(waterHeater);
        s.list.add(cookingOven);
        s.list.get(0).ScheduleEvent(s.list.get(0),10);
        s.list.get(1).ScheduleEvent(s.list.get(1),10);
        s.list.get(2).ScheduleEvent(s.list.get(2),10);
        s.list.get(1).ScheduleEvent(s.list.get(1), 15);
      try {
        Thread.sleep(2000);                 
    } catch(InterruptedException ex) {
        Thread.currentThread().interrupt();
    }
      assertEquals(true, s.list.get(0).state);
      assertEquals(false, s.list.get(1).state);
      assertEquals(true, s.list.get(2).state);      
  }
}



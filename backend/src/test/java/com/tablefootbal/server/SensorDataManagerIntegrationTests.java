package com.tablefootbal.server;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.dto.ReadingDto;
import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorDataManager;
import com.tablefootbal.server.events.SensorTrackingScheduler;
import com.tablefootbal.server.events.SensorUpdateEvent;
import com.tablefootbal.server.readings.SensorReadings;
import com.tablefootbal.server.service.SensorService;
import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.*;

import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
@TestPropertySource("classpath:readings.properties")
public class SensorDataManagerIntegrationTests extends TestCase {
    @Value("${readings.axis}")
    CalibrationStructure.Axis axis;

    @Value("${readings.window_size}")
    private int WINDOW_SIZE;

    @Value("${readings.threshold}")
    private double THRESHOLD;

    @Value("${readings.minAboveThresholdCount}")
    private int MIN_ABOVE_THRESHOLD_COUNT;

    @Value("${readings.max_readings}")
    private int MAX_READINGS;

    @Value("${readings.number_of_states_to_swap}")
    private int statesToSwap;

    @InjectMocks
    SensorDataManager manager;

    @Mock
    SensorTrackingScheduler scheduler;

    @Mock
    SensorService sensorService;

    private AlgorithmParameters algorithmParameters;

    private SensorUpdateEvent event;


    @Before
    public void init() {
        Sensor sensor = new Sensor();
        sensor.setId("11:11:11:11:11:11");
        sensor.setLastNotificationDate(new Date());
        sensor.setOnline(true);
        sensor.setOccupied(true);
        sensor.setFloor(1);
        sensor.setRoom(111);

        algorithmParameters = new AlgorithmParameters();
        algorithmParameters.setTHRESHOLD(THRESHOLD);
        algorithmParameters.setMAX_READINGS(MAX_READINGS);
        algorithmParameters.setWINDOW_SIZE(WINDOW_SIZE);
        algorithmParameters.setMIN_ABOVE_THRESHOLD_COUNT(MIN_ABOVE_THRESHOLD_COUNT);
        algorithmParameters.setNUM_OF_STATES_TO_SWAP(statesToSwap);
        algorithmParameters.setAxis(axis);

        event = mock(SensorUpdateEvent.class);
        when(event.getSource()).thenReturn(sensor);

        doNothing().when(scheduler).startTracking(Mockito.anyString());
        doNothing().when(sensorService).setOccupied(anyString(), anyBoolean());
//
//		ReflectionTestUtils.setField(manager, "THRESHOLD", THRESHOLD);
//		ReflectionTestUtils.setField(manager, "MAX_READINGS", MAX_READINGS);
//		ReflectionTestUtils.setField(manager, "WINDOW_SIZE", WINDOW_SIZE);

        manager.setAlgorithmParameters(algorithmParameters);
    }

    //	@Test
//	public void givenAverageLowerThanThreshold_thenSensorInactive()
//	{
//		when(event.getReading()).thenReturn(
//				new ReadingDto(new double[]{}, new double[]{}, new double[]{}, System.currentTimeMillis()));
//
//		manager.onApplicationEvent(event);
//
//		Sensor sensor = (Sensor) event.getSource();
//
//		Assert.assertFalse(sensor.isOccupied());
//	}
//
    @Test
    public void givenUpdateEventPublished_thenTaskTrackerStarts() {
        when(event.getReading()).thenReturn(
                new ReadingDto(new double[]{}, new double[]{}, new double[]{}, System.currentTimeMillis()));

        manager.onApplicationEvent(event);

        Mockito.verify(scheduler, times(1)).startTracking(anyString());
    }

    @Test
    public void testStateChange() {

        Sensor sensor = new Sensor();
        sensor.setId("11:11:11:11:11:11");
        sensor.setOccupied(false);
        sensor.getCalibrationStructure().setCalibrationFlag(false);
        sensor.getCalibrationStructure().setMaxValue(1.00);
        sensor.getCalibrationStructure().setMinValue(0.00);
        sensor.getCalibrationStructure().setThreshold(0.20);
        sensor.getCalibrationStructure().setAxis(CalibrationStructure.Axis.Z);

        double[] x_test = {2.221, 3.332, 4.332, 0.00, 0.00};
        double[] y_test = {8.221, -3.332, 9.332, 0.00, 0.00};
        double[] z_test = {100.00, 100.00, 100.0, 100.00, 100.00};

        manager.getAlgorithmParameters().setMIN_ABOVE_THRESHOLD_COUNT(1);
        manager.getReadingsMap().put("11:11:11:11:11:11", new SensorReadings(5));

        when(event.getSource()).thenReturn(sensor);
        when(event.getReading()).thenReturn(
                new ReadingDto(x_test, y_test, z_test, System.currentTimeMillis()));

        manager.onApplicationEvent(event);
        Assert.assertTrue(sensor.isOccupied());

        z_test = new double[] {0.10,0.10,0.10,0.10,0.10};
        when(event.getReading()).thenReturn(
                new ReadingDto(x_test, y_test, z_test, System.currentTimeMillis()));

        manager.onApplicationEvent(event);
        Assert.assertTrue(sensor.isOccupied());

        manager.onApplicationEvent(event);
        Assert.assertFalse(sensor.isOccupied());
    }

    @Test
    public void givenArraysAdded_thenArraysPresentInTheMap() {
        double[] x_test = {2.221, 3.332, 4.332};
        double[] y_test = {8.221, -3.332, 9.332};
        double[] z_test = {-2.221, -3.332, 0.332};

        when(event.getReading()).thenReturn(
                new ReadingDto(x_test, y_test, z_test, System.currentTimeMillis()));

        manager.onApplicationEvent(event);
        Sensor sensor = (Sensor) event.getSource();
        sensor.getCalibrationStructure().setCalibrationFlag(false);
        String id = sensor.getId();

        Map<String, SensorReadings> readingMap = manager.getReadingsMap();
        Assert.assertNotNull(readingMap);

        SensorReadings readings = readingMap.get(id);
        Assert.assertNotNull(readings);

        for (int i = 0; i < 3; i++) {
            SensorReadings.Reading reading = readings.getReadings().get(i);
            Assert.assertEquals(reading.getX(), x_test[i], 0.05);
            Assert.assertEquals(reading.getY(), y_test[i], 0.05);
            Assert.assertEquals(reading.getZ(), z_test[i], 0.05);
        }
    }

    @Test
    public void givenCalibrationFlagSet_thenPerformCalibrationCalledAndFlagCleared() {
        Sensor sensor = new Sensor();
        sensor.setId("11:11:11:11:11:11");
        sensor.setOccupied(false);
        sensor.setFloor(1);
        sensor.setRoom(111);

        CalibrationStructure calibrationStructure = new CalibrationStructure();

        sensor.setCalibrationStructure(calibrationStructure);
        calibrationStructure.setCalibrationFlag(true);

        LinkedList<SensorReadings.Reading> readings = new LinkedList<>(Arrays.asList(
                new SensorReadings.Reading(1.3, 0, 0, 0),
                new SensorReadings.Reading(1.1, 0, 0, 0),
                new SensorReadings.Reading(1.45, 0, 0, 0),
                new SensorReadings.Reading(1.41, 0, 0, 0),
                new SensorReadings.Reading(1.46, 0, 0, 0),
                new SensorReadings.Reading(1.99, 0, 0, 0),
                new SensorReadings.Reading(1.45, 0, 0, 0),
                new SensorReadings.Reading(1.45, 0, 0, 0)));

        SensorReadings sensorReadings = new SensorReadings(MAX_READINGS);
        sensorReadings.setReadings(readings);
        manager.getReadingsMap().put(sensor.getId(), sensorReadings);

        ArgumentCaptor<Sensor> sensorCaptor = ArgumentCaptor.forClass(Sensor.class);

        double[] readingArray = {};

        ReadingDto readingDto = new ReadingDto(readingArray, readingArray, readingArray, 0);
        manager.onApplicationEvent(new SensorUpdateEvent(sensor, readingDto));

        verify(sensorService).save(sensorCaptor.capture());
        calibrationStructure = sensorCaptor.getValue().getCalibrationStructure();
        Assert.assertFalse(calibrationStructure.isCalibrationFlag());
    }


}

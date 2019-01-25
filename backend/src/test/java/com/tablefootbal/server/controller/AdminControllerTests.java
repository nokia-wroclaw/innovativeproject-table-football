package com.tablefootbal.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorDataManager;
import com.tablefootbal.server.events.SensorTrackingScheduler;
import com.tablefootbal.server.service.AlgorithmConfigurationService;
import com.tablefootbal.server.service.SensorService;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@RunWith(SpringRunner.class)
@WithMockUser(username = "admin", password = "admin", roles = "ADMIN")
public class AdminControllerTests {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    SensorDataManager sensorDataManager;

    @Autowired
    SensorTrackingScheduler trackingScheduler;

    @MockBean
    SensorService sensorService;

    @MockBean
    AlgorithmConfigurationService configurationService;

    @Autowired
    AlgorithmParameters algorithmParameters;

    @Before
    public void init() {
        Mockito.doNothing().when(sensorService).updateSensorInformation(Mockito.any(Sensor.class));
        Mockito.doNothing().when(sensorService).save(Mockito.any(Sensor.class));
        Mockito.doNothing().when(sensorService).markAllForCalibration();
        Mockito.doNothing().when(configurationService).saveAlgorithmParameters(Mockito.any(AlgorithmParameters.class));
        Mockito.when(sensorDataManager.getAlgorithmParameters()).thenReturn(algorithmParameters);
    }

    @Test
    public void shouldConfirmAdmin() throws Exception {
        mockMvc.perform(get("/admin/").secure(true)).andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Admin confirmed"));
    }

    @Test
    public void assertUpdateSensorCalledForEachSensor() throws Exception {
        List<Sensor> sensors = new ArrayList<>();
        sensors.add(new Sensor("11:11:11:11:11:11", false, true, new Date()));
        sensors.add(new Sensor("22:22:22:22:22:22", false, true, new Date()));

        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(sensors);

        mockMvc.perform(post("/admin/")
                .secure(true)
                .content(json)
                .contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        Mockito.verify(sensorService, Mockito.times(2))
                .updateSensorInformation(Mockito.any(Sensor.class));
    }

    @Test
    public void assertCalibrationFlagSetWhenValidIdProvided() throws Exception {
        String id = "11:11:11:11:11:11";
        Sensor sensor = new Sensor(id, false, true, new Date());
        sensor.getCalibrationStructure().setCalibrationFlag(false);
        Mockito.when(sensorService.findById("11:11:11:11:11:11"))
                .thenReturn(Optional.of(sensor));

        mockMvc.perform(post("/admin/calibration/{id}", id).secure(true))
                .andExpect(status().isOk());

        Assert.assertTrue(sensor.getCalibrationStructure().isCalibrationFlag());
        Mockito.verify(sensorService, Mockito.times(1))
                .save(Mockito.any(Sensor.class));
    }

    @Test
    public void assertBadRequestReturnedWhenInvalidIdProvided() throws Exception {
        String id = "11:11:11:11:11:11";
        Mockito.when(sensorService.findById(Mockito.anyString()))
                .thenReturn(Optional.empty());

        mockMvc.perform(post("/admin/calibration/{id}", id).secure(true))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void assertBadRequestReturnedWhenInvalidJsonBody() throws Exception {
        String badJson = "{{[That Json is bad][]}{}";
        mockMvc.perform(post("/admin/calibration/algorithm/").secure(true)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(badJson))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void assertProvidedParametersAreUpdated() throws Exception {

        String json = "{" + "\"threshold\" : 0.1," +
                "\"window_size\" : 5," +
                "\"seconds_till_offline\" : 90" +
                "}";

        mockMvc.perform(post("/admin/calibration/algorithm/").secure(true)
                .contentType(MediaType.APPLICATION_JSON_UTF8).content(json))
                .andDo(print())
                .andExpect(status().isOk());

        Assert.assertEquals(0.1, sensorDataManager.getAlgorithmParameters().getTHRESHOLD(), 0.01);
        Assert.assertEquals(5, sensorDataManager.getAlgorithmParameters().getWINDOW_SIZE());
        Assert.assertEquals(90, trackingScheduler.getSECONDS_TILL_OFFLINE());

        Mockito.verify(sensorService, Mockito.times(1)).markAllForCalibration();
        Mockito.verify(configurationService, Mockito.times(1))
                .saveAlgorithmParameters(Mockito.any());
    }


}
















































package com.tablefootbal.server.controller;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.entity.CalibrationStructure;
import com.tablefootbal.server.entity.Sensor;
import com.tablefootbal.server.events.SensorDataManager;
import com.tablefootbal.server.events.SensorTrackingScheduler;
import com.tablefootbal.server.exceptions.customExceptions.InvalidJsonException;
import com.tablefootbal.server.exceptions.customExceptions.SensorNotFoundException;
import com.tablefootbal.server.service.AlgorithmConfigurationService;
import com.tablefootbal.server.service.SensorService;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Locale;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping("/admin")
public class AdminController {

    final
    private SensorService sensorService;

    final private MessageSource messageSource;

    private final SensorDataManager dataManager;

    private final AlgorithmConfigurationService algorithmConfigurationService;

    private final SensorTrackingScheduler trackingScheduler;

    @Autowired
    public AdminController(SensorService sensorService, MessageSource messageSource,
                           SensorDataManager dataManager,
                           AlgorithmConfigurationService algorithmConfigurationService, SensorTrackingScheduler trackingScheduler) {
        this.sensorService = sensorService;
        this.messageSource = messageSource;
        this.dataManager = dataManager;
        this.algorithmConfigurationService = algorithmConfigurationService;
        this.trackingScheduler = trackingScheduler;
    }

    @GetMapping("/")
    @CrossOrigin
    public String ConfirmAdmin() {
        return "Admin confirmed";
    }

    @PostMapping
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public void updateSensors(@RequestBody List<Sensor> sensors) {
        for (Sensor sensor : sensors) {
            sensorService.updateSensorInformation(sensor);
        }
    }

    @PostMapping("/calibration/{sensorId}")
    @CrossOrigin
    public void setCalibrationFlag(@PathVariable("sensorId") String sensorId) {
        Optional<Sensor> sensorOptional = sensorService.findById(sensorId);
        if (sensorOptional.isPresent()) {
            Sensor sensor = sensorOptional.get();
            sensor.getCalibrationStructure().setCalibrationFlag(true);
            sensorService.save(sensor);
        } else {
            throw new SensorNotFoundException(messageSource.getMessage(
                    "exception.sensorNotFound", new Object[]{sensorId}, Locale.getDefault()));
        }
    }

    @PostMapping("/calibration/algorithm/")
    @CrossOrigin
    @ResponseStatus(HttpStatus.OK)
    public void changeAlgorithmParameters(@RequestBody String jsonAlgorithmParameters) {

        AlgorithmParameters algorithmParameters = dataManager.getAlgorithmParameters();
        try {
            JSONObject jsonObject = new JSONObject(jsonAlgorithmParameters);

            if (jsonObject.has("threshold"))
                algorithmParameters.setTHRESHOLD(jsonObject.getDouble("threshold"));
            if (jsonObject.has("window_size"))
                algorithmParameters.setWINDOW_SIZE(jsonObject.getInt("window_size"));
            if (jsonObject.has("axis"))
                algorithmParameters.setAxis(CalibrationStructure.Axis.valueOf(jsonObject.getString("axis")));
            if (jsonObject.has("peaks_count"))
                algorithmParameters.setMIN_ABOVE_THRESHOLD_COUNT(jsonObject.getInt("peaks_count"));
            if (jsonObject.has("max_readings"))
                algorithmParameters.setMAX_READINGS(jsonObject.getInt("max_readings"));
            if (jsonObject.has("states_to_swap"))
                algorithmParameters.setNUM_OF_STATES_TO_SWAP(jsonObject.getInt("states_to_swap"));
            if (jsonObject.has("seconds_till_offline"))
                trackingScheduler.setSECONDS_TILL_OFFLINE(jsonObject.getInt("seconds_till_offline"));

        } catch (JSONException e) {
            log.error("Error parsing json received at /admin/calibration/algorithm/\n" + e.getMessage());
            throw new InvalidJsonException(
                    messageSource.getMessage("exception.invalid_json",
                            new Object[]{e.getMessage()}, Locale.getDefault()));
        }

        algorithmConfigurationService.saveAlgorithmParameters(algorithmParameters);
        sensorService.markAllForCalibration();
    }
}

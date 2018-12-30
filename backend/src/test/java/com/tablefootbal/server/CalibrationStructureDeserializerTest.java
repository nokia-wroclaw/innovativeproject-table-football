package com.tablefootbal.server;

import com.tablefootbal.server.dsp.AlgorithmParameters;
import com.tablefootbal.server.entity.CalibrationStructure;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CalibrationStructureDeserializerTest {

    @Test
    public void testPresentPropertiesExtractedFormJson() throws JSONException {
        String json = "{\"threshold\":0.05,\"window_size\":5,\"axis\":'X'}";

        JSONObject jsonObject = new JSONObject(json);
        double threshold = jsonObject.getDouble("threshold");
        int window_size = jsonObject.getInt("window_size");
        CalibrationStructure.Axis axis = CalibrationStructure.Axis.valueOf(jsonObject.getString("axis"));

        Assert.assertEquals(0.05, threshold, 0.01);
        Assert.assertEquals(5, window_size);
        Assert.assertEquals(axis, CalibrationStructure.Axis.X);
    }

    @Test
    public void testMissingPropertiesAreNotUpdated() throws JSONException {
        String json = "{\"threshold\":0.05,\"window_size\":5,\"axis\":'X'}";
        AlgorithmParameters algorithmParameters = new AlgorithmParameters();
        algorithmParameters.setAxis(CalibrationStructure.Axis.Z);
        algorithmParameters.setTHRESHOLD(0.1);
        algorithmParameters.setWINDOW_SIZE(9);
        algorithmParameters.setMAX_READINGS(20);
        algorithmParameters.setMIN_ABOVE_THRESHOLD_COUNT(10);

        JSONObject jsonObject = new JSONObject(json);

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

        Assert.assertEquals(0.05, algorithmParameters.getTHRESHOLD(), 0.01);
        Assert.assertEquals(5, algorithmParameters.getWINDOW_SIZE());
        Assert.assertEquals(CalibrationStructure.Axis.X, algorithmParameters.getAxis());
        Assert.assertEquals(20, algorithmParameters.getMAX_READINGS());
        Assert.assertEquals(10, algorithmParameters.getMIN_ABOVE_THRESHOLD_COUNT());
    }
}

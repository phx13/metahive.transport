package com.metahive.transport.server.controller;


import com.metahive.transport.server.entity.Coach;
import com.metahive.transport.server.service.CoachService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author phx
 * @since 2021-11-29
 */
@RestController
@CrossOrigin
@RequestMapping("/server/coach")
public class CoachController {
    @Autowired
    private CoachService coachService;

    @GetMapping("/all")
    public List<Coach> selectCoach() {
        return coachService.list();
    }

    @PostMapping("/save")
    public boolean saveCoach(@RequestBody HashMap<String, ArrayList<Map>> body) {
        if (body.get("insertRecords") != null && body.get("insertRecords").size() > 0) {
            body.get("insertRecords").forEach((insertRecord) -> {
                Coach coach = new Coach();
                coach.setUsername(insertRecord.get("username") == null ? "" : insertRecord.get("username").toString());
                coach.setType(insertRecord.get("type") == null ? "" : insertRecord.get("type").toString());
                coach.setFromName(insertRecord.get("fromName") == null ? "" : insertRecord.get("fromName").toString());
                coach.setToName(insertRecord.get("toName") == null ? "" : insertRecord.get("toName").toString());
                coach.setFromCoord(insertRecord.get("fromCoord") == null ? "" : insertRecord.get("fromCoord").toString());
                coach.setToCoord(insertRecord.get("toCoord") == null ? "" : insertRecord.get("toCoord").toString());
                coach.setCurrentCoord(insertRecord.get("currentCoord") == null ? "" : insertRecord.get("currentCoord").toString());
                coach.setOil(insertRecord.get("oil") == null ? 0d : Double.parseDouble(insertRecord.get("oil").toString()));
                coach.setPressure(insertRecord.get("pressure") == null ? 0d : Double.parseDouble(insertRecord.get("pressure").toString()));
                coach.setTemperature(insertRecord.get("temperature") == null ? 0d : Double.parseDouble(insertRecord.get("temperature").toString()));
                coach.setHumidity(insertRecord.get("humidity") == null ? 0d : Double.parseDouble(insertRecord.get("humidity").toString()));
                coachService.save(coach);
            });
        } else if (body.get("removeRecords") != null && body.get("removeRecords").size() > 0) {
            body.get("removeRecords").forEach((removeRecord) -> coachService.removeById(removeRecord.get("id").toString()));
        } else if (body.get("updateRecords") != null && body.get("updateRecords").size() > 0) {
            body.get("updateRecords").forEach((updateRecord) -> {
                Coach coach = coachService.getById(updateRecord.get("id").toString());
                coach.setUsername(updateRecord.get("username") == null ? "" : updateRecord.get("username").toString());
                coach.setType(updateRecord.get("type") == null ? "" : updateRecord.get("type").toString());
                coach.setFromName(updateRecord.get("fromName") == null ? "" : updateRecord.get("fromName").toString());
                coach.setToName(updateRecord.get("toName") == null ? "" : updateRecord.get("toName").toString());
                coach.setFromCoord(updateRecord.get("fromCoord") == null ? "" : updateRecord.get("fromCoord").toString());
                coach.setToCoord(updateRecord.get("toCoord") == null ? "" : updateRecord.get("toCoord").toString());
                coach.setCurrentCoord(updateRecord.get("currentCoord") == null ? "" : updateRecord.get("currentCoord").toString());
                coach.setOil(updateRecord.get("oil") == null ? 0d : Double.parseDouble(updateRecord.get("oil").toString()));
                coach.setPressure(updateRecord.get("pressure") == null ? 0d : Double.parseDouble(updateRecord.get("pressure").toString()));
                coach.setTemperature(updateRecord.get("temperature") == null ? 0d : Double.parseDouble(updateRecord.get("temperature").toString()));
                coach.setHumidity(updateRecord.get("humidity") == null ? 0d : Double.parseDouble(updateRecord.get("humidity").toString()));
                coachService.updateById(coach);
            });
        }

        return true;
    }
}


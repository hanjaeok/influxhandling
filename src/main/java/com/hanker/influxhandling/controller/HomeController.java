package com.hanker.influxhandling.controller;


import com.hanker.influxhandling.util.JsonRead;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@Controller
public class HomeController {

    @RequestMapping(value="/boot", method= RequestMethod.GET)
    public String boot(Model model){


        return "boot";
    }

    @RequestMapping(value="/view")
    public String view(){
        return "view";
    }

    @RequestMapping(value="/inputData")
    public String inputData(HttpServletRequest req, Model model){

        String setDate = req.getParameter("date");
        String setTime = req.getParameter("time");
        String specDt = setDate + "%20" + setTime + ":00";
        String pigId = req.getParameter("pigId");


        JsonRead jsonRead = new JsonRead();
        JSONObject tmpJson;
        try{
            String url = "url";
            JSONObject json = jsonRead.readJsonFromUrl(url);
            JSONArray jsonArray = json.getJSONArray("data");
            List<HashMap<String, Object>> listMap = new ArrayList<HashMap<String, Object>>();
            for(int i = 0 ; i < jsonArray.length() ; i++){
                HashMap<String, Object> map = new HashMap<String ,Object>();
                tmpJson = jsonArray.getJSONObject(i);
                String value = tmpJson.getString("time");
                String[] tmpValue = value.split("T");

                String dateTime = tmpValue[0] + " " + tmpValue[1].split("\\+")[0];
                System.out.println(dateTime);
                map.put("time", dateTime);
                map.put("lie", tmpJson.getString("lie"));
                map.put("sit", tmpJson.getString("sit"));
                map.put("stand", tmpJson.getString("stand"));
                map.put("pigId", tmpJson.getString("pig_id"));
                map.put("num", i);

                listMap.add(map);
            }
            model.addAttribute("data", listMap);

        } catch(Exception e){
            e.printStackTrace();
        }
        return "jsonView";
    }
}

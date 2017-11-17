package com.rsoi.kursach.rest;

import com.rsoi.kursach.models.Team;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class TeamController {
        
    @Value("${name}") String name;
    
    @RequestMapping("/teams")
    public @ResponseBody List<Team> getTeams(){
        
        List<Team> list = new ArrayList<>();
        
        Team team = new Team();
        team.setId(0l);
        team.setLocation("Harlem");
        team.setName("Globetrotters");
        list.add(team);
        
        team.setId(1l);
        team.setLocation("Washiton");
        team.setName("Generals");
        list.add(team);
        
        return list;
    }
    
}

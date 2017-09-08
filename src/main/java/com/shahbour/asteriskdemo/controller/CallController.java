package com.shahbour.asteriskdemo.controller;


import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.tools.RestException;
import com.shahbour.asteriskdemo.utility.ARIServiceImpl;
import com.shahbour.asteriskdemo.utility.Hangup;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@RestController
@Slf4j
public class CallController {


    @Autowired
    ARIServiceImpl ariService;

    @Autowired
    ScheduledExecutorService taskScheduler;

    @GetMapping(path = "/call")
    public ResponseEntity<Void> call(@RequestParam(required = false,defaultValue = "9613050252") String from ,
                                     @RequestParam(required = false,defaultValue = "9613050252") String to) {
        try {
            log.debug("Trying to Call {} to {}",from,to);
            String endpoint = "SIP/SonusUK/" + to;
            ActionChannels actionChannels = ariService.getAri().getActionImpl(ActionChannels.class);
            Map<String,String> variables = new HashMap<>();
            variables.put("originateStasis",ariService.getOriginateStasis());
            variables.put("callId",from + "-" + to);
            Channel returnChannel = actionChannels.originate(endpoint,"1002","default",1,"","","",from,30,variables,"","","","");
            taskScheduler.schedule(new Hangup(returnChannel.getId(),ariService),10, TimeUnit.SECONDS);
            ariService.getAri().closeAction(actionChannels);
        } catch (RestException e) {
            log.debug("Error in originating call",e);
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unknown Error",e);
            return ResponseEntity.badRequest().build();
        }
        return ResponseEntity.ok().build();
    }
}

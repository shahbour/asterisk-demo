package com.shahbour.asteriskdemo.utility;

import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.tools.ARIException;
import ch.loway.oss.ari4java.tools.RestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Hangup implements Runnable {

    private final String channelId;
    private final ARIServiceImpl ariService;

    public Hangup(String channelId, ARIServiceImpl ariService) {
        this.channelId = channelId;
        this.ariService = ariService;
    }


    @Override
    public void run() {
        log.debug("hangup the call {}",channelId);
        try {
            ActionChannels actionChannels = ariService.getAri().getActionImpl(ActionChannels.class);
            actionChannels.hangup(channelId, "normal");
            ariService.getAri().closeAction(actionChannels);
        } catch (RestException e) {
            log.error("{}",e);
        } catch (ARIException e) {
            log.error("{}",e);
        }
    }
}

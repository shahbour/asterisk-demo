package com.shahbour.asteriskdemo.utility;

import ch.loway.oss.ari4java.generated.ActionChannels;
import ch.loway.oss.ari4java.generated.Channel;
import ch.loway.oss.ari4java.generated.Message;
import ch.loway.oss.ari4java.generated.ari_2_0_0.models.StasisStart_impl_ari_2_0_0;
import ch.loway.oss.ari4java.tools.AriCallback;
import ch.loway.oss.ari4java.tools.RestException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CallDump  implements AriCallback<Message> {

    public CallDump(ARIServiceImpl ariService) {
        this.ariService = ariService;
    }

    private final ARIServiceImpl ariService;

    @Override
    public void onSuccess(Message message) {
        log.trace("received Message {}", message);
        if(message instanceof StasisStart_impl_ari_2_0_0) {
            try {

                StasisStart_impl_ari_2_0_0 stasisStart = (StasisStart_impl_ari_2_0_0) message;
                Channel channel = stasisStart.getChannel();
                String channelId = channel.getId();
                String applicationName = stasisStart.getApplication();
                log.info("received message with channelId {} on application {}", channelId,applicationName);
                if(applicationName.equals(ariService.getTerminateStasis())) {
                    ActionChannels actionChannels = ariService.getAri().getActionImpl(ActionChannels.class);
                    actionChannels.answer(channelId);
                    //actionChannels.startMoh(channelId,"");
                    ariService.getAri().closeAction(actionChannels);
                }

            } catch (Exception e) {
                log.error("Unknown Error",e);
            }
        }
    }

    @Override
    public void onFailure(RestException e) {
        log.warn("failure on registering App ",e);
    }
}

package com.shahbour.asteriskdemo.utility;


import ch.loway.oss.ari4java.ARI;
import ch.loway.oss.ari4java.AriFactory;
import ch.loway.oss.ari4java.AriVersion;
import ch.loway.oss.ari4java.generated.ActionEvents;
import ch.loway.oss.ari4java.tools.ARIException;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.net.URISyntaxException;

@Service("ARIService")
@ConfigurationProperties(prefix = "asterisk")
@Slf4j
public class ARIServiceImpl {

    @Getter
    @Setter
    private String url,username,password,originateStasis,terminateStasis;

    @Getter
    private ARI ari;
    private ActionEvents actionEvents;

    @PostConstruct
    private void connect() throws ARIException, URISyntaxException {
        log.info("Connecting to Asterisk ARI");
        ari = AriFactory.nettyHttp(url, username, password, AriVersion.ARI_2_0_0);
        actionEvents = ari.getActionImpl(ActionEvents.class);
        actionEvents.eventWebsocket(terminateStasis + "," + originateStasis,false,new CallDump(this));
    }

    @PreDestroy
    private void destroy() throws ARIException {
        log.info("destroying ARIService");
        ari.closeAction(actionEvents);
    }

}

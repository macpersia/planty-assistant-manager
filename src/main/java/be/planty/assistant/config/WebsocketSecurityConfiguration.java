package be.planty.assistant.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static be.planty.assistant.security.AuthoritiesConstants.*;

@Configuration
public class WebsocketSecurityConfiguration extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    @Override
    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
        messages
            .nullDestMatcher()
            .authenticated()
            .simpDestMatchers("/topic/tracker")
            .hasAuthority(ADMIN)

            // Added by Hadi
//            .simpDestMatchers("/topic/pairing.req*").hasAuthority(AGENT)
//            .simpSubscribeDestMatchers("/topic/pairing.res*").hasAuthority(AGENT)
            .simpDestMatchers("/topic/pairing-requests*").hasAuthority(AGENT)
            .simpSubscribeDestMatchers("/user/queue/pairing-responses*").hasAuthority(AGENT)

            // Added by Hadi
////            .simpDestMatchers("/topic/action.req").hasAuthority(SKILL)
////            .simpSubscribeDestMatchers("/topic/action.req").hasAuthority(AGENT)
//            .simpDestMatchers("/topic/action.req").hasAnyAuthority(SKILL, AGENT)
//            .simpSubscribeDestMatchers("/topic/action.req").hasAnyAuthority(AGENT, SKILL)
            .simpDestMatchers("/topic/action-requests*").hasAnyAuthority(SKILL)
            .simpSubscribeDestMatchers("/user/queue/action-requests*").hasAnyAuthority(AGENT)

            // Added by Hadi
////            .simpDestMatchers("/topic/action.res").hasAuthority(AGENT)
////            .simpSubscribeDestMatchers("/topic/action.res").hasAuthority(SKILL)
//            .simpDestMatchers("/topic/action.res").hasAnyAuthority(AGENT, SKILL)
//            .simpSubscribeDestMatchers("/topic/action.res").hasAnyAuthority(SKILL, AGENT)
            .simpDestMatchers("/topic/action-responses*").hasAnyAuthority(AGENT)
            .simpSubscribeDestMatchers("/user/queue/action-responses*").hasAnyAuthority(SKILL)
            // matches any destination that starts with /topic/
            // (i.e. cannot send messages directly to /topic/)
            // (i.e. cannot subscribe to /topic/messages/* to get messages sent to
            // /topic/messages-user<id>)

            .simpDestMatchers("/topic/**").authenticated()
            .simpDestMatchers("/queue/**").authenticated()
            .simpDestMatchers("/user/**").authenticated()
            .simpDestMatchers("/user/queue/**").authenticated()

            // message types other than MESSAGE and SUBSCRIBE
            .simpTypeMatchers(SimpMessageType.MESSAGE, SimpMessageType.SUBSCRIBE)
            .denyAll()
            // catch all
            .anyMessage()
            .denyAll();
    }

    /**
     * Disables CSRF for Websockets.
     */
    @Override
    protected boolean sameOriginDisabled() {
        return true;
    }
}

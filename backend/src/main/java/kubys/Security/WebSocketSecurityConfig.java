package kubys.Security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.messaging.MessageSecurityMetadataSourceRegistry;
import org.springframework.security.config.annotation.web.socket.AbstractSecurityWebSocketMessageBrokerConfigurer;

import static org.springframework.messaging.simp.SimpMessageType.*;

@Configuration
public class WebSocketSecurityConfig extends AbstractSecurityWebSocketMessageBrokerConfigurer {

    protected void configureInbound(MessageSecurityMetadataSourceRegistry messages) {
//        messages
//                .simpTypeMatchers(CONNECT).permitAll();
//        messages
//                .simpDestMatchers("/broker/*", "/user/*").authenticated();
        messages
                .anyMessage().permitAll();
//        messages
//                .nullDestMatcher().authenticated()
//                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
//                .simpDestMatchers("/**").hasRole("USER")
//                .simpSubscribeDestMatchers("/user/**").hasRole("USER")
//                .simpTypeMatchers(MESSAGE, SUBSCRIBE).permitAll()//denyAll()
//                .anyMessage().permitAll();//.denyAll();
    }
}
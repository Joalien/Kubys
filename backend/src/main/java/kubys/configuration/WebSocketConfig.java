package kubys.configuration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import kubys.Map.Position;
import kubys.Player.Breed;
import kubys.Player.Player;
import kubys.Player.PlayerDao;
import kubys.User.User;
import kubys.User.UserDao;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;


// https://stackoverflow.com/questions/45405332/websocket-authentication-and-authorization-in-spring

@Configuration
@EnableWebSocketMessageBroker
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private UserDao userDao;
    private PlayerDao playerDao;

    @Autowired
    public WebSocketConfig(UserDao userDao, PlayerDao playerDao) {
        this.userDao = userDao;
        this.playerDao = playerDao;
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*")
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                                   Map attributes) {

                        log.info("Before handshake");
                        if (request instanceof ServletServerHttpRequest) {
                            ServletServerHttpRequest servletRequest = (ServletServerHttpRequest) request;
                            HttpSession session = servletRequest.getServletRequest().getSession();
                            attributes.put("sessionId", session.getId());
                        }
                        return true;
                    }
                    @Override
                    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                               Exception ex) {
                    }
                });
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        //BROADCAST TO ALL USER WHO SUBSCRIBED
        config.enableSimpleBroker("/broker", "/error", "/getAllMap", "/getPlayers", "/getSpells", "/setPlayer");
        config.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(new ChannelInterceptor() {
            @Override
            public Message<?> preSend(Message<?> message, MessageChannel channel) {
                StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
                if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                    final String token = accessor.getFirstNativeHeader("login");
                    final UsernamePasswordAuthenticationToken user = getAuthenticatedOrFail(token);

                    accessor.setUser(user);
                }
                return message;
            }
        });
    }

    private UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String token) throws AuthenticationException {
        if (token == null || token.trim().length() == 0) {
            throw new AuthenticationCredentialsNotFoundException("token was null or empty.");
        }

        User u = null;

//        log.info("token : "+token);
        try {
            // idToken comes from the client app (shown above)
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdTokenAsync(token).get();
            UserRecord user = FirebaseAuth.getInstance().getUserAsync(firebaseToken.getUid()).get();

            log.info("1"+user.getUid());

            //Get user from db
            Optional<User> optionalUser = userDao.findById(user.getUid());
            if(optionalUser.isEmpty()) {
                u = User.builder()
                        .uid(user.getUid())
                        .displayName("Georges")
                        .players(List.of(Player.builder()
//                                .spells(Spells.getSpells())
                                .breed(Breed.DWARF)
                                .level(1)
                                .name("Harison")
                                .pa(10)
                                .pm(5)
                                .position(Position.builder().x(0).y(5).z(0).build())
                                .build()))
                        .build();
                u.getPlayers().toArray(new Player[0])[0].setUser(u);

                log.info("2"+u);
                userDao.save(u);
                log.info("2.5");
                System.out.println(playerDao.findAll());
                System.out.println(userDao.findAll());
                log.info("2.6");
                optionalUser = userDao.findById(user.getUid());
                log.info("3"+optionalUser.toString());

            }else u = optionalUser.get();

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException(e.getMessage());
        }

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                u.getUid(),
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
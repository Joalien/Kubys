package kubys.configuration;

import com.google.api.core.ApiFuture;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import kubys.Player.Player;
import kubys.Spell.Dwarf;
import kubys.User.User;
import kubys.User.UserDao;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Collections;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

// https://stackoverflow.com/questions/45405332/websocket-authentication-and-authorization-in-spring
@Configuration
@EnableWebSocketMessageBroker
@Slf4j
@AllArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private UserDao userDao;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/connect").setAllowedOrigins("*") //FIXME CSRF vulnerability ?
                .addInterceptors(new HandshakeInterceptor() {
                    @Override
                    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
                                                   Map attributes) {
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
        config.enableSimpleBroker("/broker", "/error", "/getAllMap", "/getPlayers", "/getSpells", "/setPlayer", "/fight", "/getSpellPoints");
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

    private UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String token) {
        if (token == null || token.trim().length() == 0) {
            throw new AuthenticationCredentialsNotFoundException("token was null or empty.");
        }

        String userId;
        ApiFuture<FirebaseToken> future = FirebaseAuth.getInstance().verifyIdTokenAsync(token);
        try {
            userId = future.get().getUid();
        } catch (ExecutionException | InterruptedException e) {
            throw new BadCredentialsException(e.getMessage());
        }

        //Get user from db
        Optional<User> optionalUser = userDao.findById(userId);
        if (optionalUser.isEmpty()) { // TODO move this out of this file !
            User user = User.builder().uid(userId).displayName("Alexandre Dumas").build();
            // TODO let people create their own characters
            user.setPlayers(
                    Stream.of("Athos", "Porthos", "Aramis")
                            .map(name -> Player.builder()
                                    .user(user)
                                    .name(name)
                                    .level(1)
                                    .pa(10)
                                    .pm(5)
                                    .characteristics(Set.of(new Dwarf()))
                                    .build())
                            .collect(Collectors.toList()));
            userDao.save(user);
        }

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }
}
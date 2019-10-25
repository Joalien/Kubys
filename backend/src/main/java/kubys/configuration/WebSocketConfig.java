package kubys.configuration;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import kubys.Player.Breed;
import kubys.Player.Player;
import kubys.Player.PlayerDao;
import kubys.Spell.Spell;
import kubys.Spell.SpellPlayer;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.server.HandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.*;

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

    private UsernamePasswordAuthenticationToken getAuthenticatedOrFail(String token) {
        if (token == null || token.trim().length() == 0) {
            throw new AuthenticationCredentialsNotFoundException("token was null or empty.");
        }

        String userId;
        try {
            // Execute a call to google API to fetch public key, then decrypt the JWT token with it
            FirebaseToken firebaseToken = FirebaseAuth.getInstance().verifyIdTokenAsync(token).get();
            UserRecord user = FirebaseAuth.getInstance().getUserAsync(firebaseToken.getUid()).get();
            userId = user.getUid();

            //Get user from db
            Optional<User> optionalUser = userDao.findById(userId);
            log.info("1.5:"+optionalUser);
            if(optionalUser.isEmpty()) {
                User u = User.builder().uid(userId).displayName("Alexandre Dumas").build();
                log.info("No user find, creating 3 empty players");
                // TODO let people create their own characters
                u.setPlayers(List.of(
                        Player.builder()
                                .breed(Breed.DWARF)
                                .user(u)
                                .level(1)
                                .name("Athos")
                                .pa(10)
                                .pm(5)
                                .build(),
                        Player.builder()
                                .breed(Breed.DWARF)
                                .user(u)
                                .level(1)
                                .name("Porthos")
                                .pa(10)
                                .pm(5)
                                .build(),
                        Player.builder()
                                .breed(Breed.DWARF)
                                .user(u)
                                .level(1)
                                .name("Aramis")
                                .pa(10)
                                .pm(5)
                                .build()
                ));
                //Add random spell to all new player
                u.getPlayers().forEach(player -> player.setSpellsPlayer(
                        Set.of(getRandomSpell(player))));

                log.info("2"+u);
                userDao.save(u);
                log.info("2.5");
                System.out.println(playerDao.findAll());
                System.out.println(userDao.findAll());
                log.info("2.6");
                optionalUser = userDao.findById(userId);
                log.info("3"+optionalUser.toString());

            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new BadCredentialsException(e.getMessage());
        }

        // null credentials, we do not pass the password along
        return new UsernamePasswordAuthenticationToken(
                userId,
                null,
                Collections.singleton((GrantedAuthority) () -> "USER") // MUST provide at least one role
        );
    }

    private SpellPlayer getRandomSpell(Player player) {
        return SpellPlayer.builder()
                .level(1)
                .player(player)
                .spell_id(Math.round(Math.random() * Spell.getSpells().size()))
                .build();
    }
}
package co.com.powerup.api.config;

import co.com.powerup.model.token.TokenClaims;
import co.com.powerup.model.token.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter implements WebFilter {

    private final TokenProvider tokenProvider;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        String token = resolveToken(exchange);

        if (token !=  null && tokenProvider.validateToken(token)) {
            TokenClaims claims = tokenProvider.getAllClaimsFromToken(token);
            String role = claims.getRole();
            String username = claims.getEmail();

            UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                    username, null, Collections.singletonList(new SimpleGrantedAuthority(role)));

            return chain.filter(exchange)
                    .contextWrite(ReactiveSecurityContextHolder.withAuthentication(auth));
        }
        return chain.filter(exchange);
    }

    private String resolveToken(ServerWebExchange exchange) {
        String bearerToken = exchange.getRequest().getHeaders().getFirst("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

}

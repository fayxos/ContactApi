package com.projects.contact_api.config;

import com.projects.contact_api.model.User;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.access.intercept.RequestAuthorizationContext;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.function.Supplier;

@Component
public class UserSecurity implements AuthorizationManager<RequestAuthorizationContext> {

    @Override
    public AuthorizationDecision check(Supplier authenticationSupplier, RequestAuthorizationContext ctx) {
        // get {userId} from the request
        Integer userId = Integer.parseInt(ctx.getVariables().get("userId"));
        Authentication authentication = (Authentication) authenticationSupplier.get();
        return new AuthorizationDecision(hasUserId(authentication, userId));
    }
    public boolean hasUserId(Authentication authentication, Integer userId) {
        if(authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"))) return true;

        User authenticatedUser = (User)authentication.getPrincipal();
        return Objects.equals(authenticatedUser.getId(), userId);
    }

}

package fr.cethalesbdx.billetterie.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String MEMBRE = "ROLE_MEMBRE";

    public static final String SUPER_MEMBRE = "ROLE_SUPER_MEMBRE";


    private AuthoritiesConstants() {
    }
}

package pizza.restaurant.security.config;

import java.util.Arrays;

/**
 * This class contains roles and permissions
 */
public class AccessControlList {
    public static final String[] PUBLIC_ENDPOINTS = {
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/auth/tokens",
            "/auth/register-customers",
            "/menus/list",
            "/ws/**"
    };

    /**
     * Common permissions
     */
    public static final String[] COMMON_PERMISSIONS = {
            "/auth/tokens/refresh"
    };

    /**
     * Customer permissions
     */
    public static final String[] CUSTOMER_PERMISSIONS = {
            Arrays.toString(COMMON_PERMISSIONS),
            "/orders/customer/create",
            "/orders/my-orders"
    };

    private AccessControlList() {
        // Utility classes should not have public constructors
    }

}

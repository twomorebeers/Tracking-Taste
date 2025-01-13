package auth;

import database.dao.UserDAO;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthenticationService {
    private static final Map<String, UserSession> sessions = new HashMap<>();
    private static final int SESSION_TIMEOUT_MINUTES = 30;
    
    public static class UserSession {
        private final String username;
        private final int userId;
        private final int roleId;
        private final long createdAt;
        
        public UserSession(String username, int userId, int roleId) {
            this.username = username;
            this.userId = userId;
            this.roleId = roleId;
            this.createdAt = System.currentTimeMillis();
        }
        
        public boolean isValid() {
            long now = System.currentTimeMillis();
            return (now - createdAt) < (SESSION_TIMEOUT_MINUTES * 60 * 1000);
        }
        
        public String getUsername() { return username; }
        public int getUserId() { return userId; }
        public int getRoleId() { return roleId; }
    }
    
    public static String hashPassword(String password, String salt) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(salt.getBytes());
            byte[] hashedPassword = md.digest(password.getBytes());
            return Base64.getEncoder().encodeToString(hashedPassword);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
    
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
    
    public static String login(String username, String password) {
        // In a real application, you would:
        // 1. Retrieve the user's salt and hashed password from the database
        // 2. Hash the provided password with the salt
        // 3. Compare with stored hash
        // For this example, we'll just check if the user exists
        
        if (UserDAO.userExists(username)) {
            // Create a new session
            String sessionId = UUID.randomUUID().toString();
            sessions.put(sessionId, new UserSession(username, 1, 2)); // Hardcoded user ID and role for example
            return sessionId;
        }
        return null;
    }
    
    public static boolean logout(String sessionId) {
        return sessions.remove(sessionId) != null;
    }
    
    public static UserSession getSession(String sessionId) {
        UserSession session = sessions.get(sessionId);
        if (session != null && session.isValid()) {
            return session;
        }
        sessions.remove(sessionId);
        return null;
    }
    
    public static boolean isAuthenticated(String sessionId) {
        return getSession(sessionId) != null;
    }
    
    public static boolean hasRole(String sessionId, int requiredRoleId) {
        UserSession session = getSession(sessionId);
        return session != null && session.getRoleId() == requiredRoleId;
    }
}

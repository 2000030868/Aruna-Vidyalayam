//package com.arunavidyalayam.school.controller;
//
//import com.arunavidyalayam.school.dto.LoginDto;
//import com.arunavidyalayam.school.model.User;
//import com.arunavidyalayam.school.service.UserService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserController {
//
//    @Autowired
//    private UserService userService;
//
//    // Add new user
//    @PostMapping
//    public User createUser(@RequestBody User user) {
//        return userService.saveUser(user);
//    }
//
//    // Get all users
//    @GetMapping
//    public List<User> getAllUsers() {
//        return userService.getAllUsers();
//    }
//
//    // Get user by username
//    @GetMapping("/id/{id}")
//    public User getUserById_v2(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    @GetMapping("/{id}")
//    public User getUserById(@PathVariable Long id) {
//        return userService.getUserById(id);
//    }
//
//    // Update existing user by ID
//    @PutMapping("/{id}")
//    public User updateUser(@PathVariable Long id, @RequestBody User user) {
//        return userService.updateUser(id, user);
//    }
//
//    // Delete user by ID
//    @DeleteMapping("/{id}")
//    public String deleteUser(@PathVariable Long id) {
//        userService.deleteUser(id);
//        return "User deleted successfully!";
//    }
//
//
//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
//        User user = userService.getUserByUsername(dto.getUsername());
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        // If you're storing plain text (not recommended) use:
//        // if (!user.getPassword().equals(dto.getPassword())) { ... }
//
//        // Preferred: if you store hashed passwords, use BCrypt:
//        if (!passwordMatches(dto.getPassword(), user.getPassword())) {
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        // Optionally build a token (JWT) or return user info
//        Map<String, Object> resp = new HashMap<>();
//        resp.put("id", user.getId());
//        resp.put("username", user.getUsername());
//        // resp.put("token", jwtToken);
//
//        return ResponseEntity.ok(resp);
//    }
//
//    private boolean passwordMatches(String rawPassword, String storedHash) {
//        try {
//            // if you use BCrypt:
//            return org.springframework.security.crypto.bcrypt.BCrypt.checkpw(rawPassword, storedHash);
//        } catch (Exception e) {
//            // fallback to plain compare if not hashed (not recommended)
//            return rawPassword.equals(storedHash);
//        }
//    }
//
//
//
//}




package com.arunavidyalayam.school.controller;

import com.arunavidyalayam.school.dto.LoginDto;
import com.arunavidyalayam.school.model.Role;
import com.arunavidyalayam.school.model.User;
import com.arunavidyalayam.school.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    // Use BCryptPasswordEncoder for matching hashed passwords
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ------------------------
    // CRUD endpoints
    // ------------------------

    // Create new user (password hashing is handled in UserServiceImpl)
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user) {
        User saved = userService.saveUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    // Get all users
    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    // Get user by id (single canonical route)
    @GetMapping("/{id}")
    public ResponseEntity<User> getUserById(@PathVariable Long id) {
        User u = userService.getUserById(id);
        return ResponseEntity.ok(u);
    }

    // Update existing user by ID
    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@PathVariable Long id, @RequestBody User user) {
        User updated = userService.updateUser(id, user);
        return ResponseEntity.ok(updated);
    }

    // Delete user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        Map<String, String> resp = new HashMap<>();
        resp.put("message", "User deleted successfully!");
        return ResponseEntity.ok(resp);
    }

    // ------------------------
    // Login endpoint (admin only)
    // ------------------------

//    @PostMapping("/login")
//    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
//        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username and password required");
//        }
//
//        // 1) Find user
//        User user = userService.getUserByUsername(dto.getUsername());
//        if (user == null) {
//            // Debug hint for dev logs (do NOT expose to client)
//            System.out.println("[LOGIN DEBUG] user not found for username=" + dto.getUsername());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        // 2) Check stored password presence
//        String stored = user.getPassword();
//        if (stored == null || stored.isBlank()) {
//            System.out.println("[LOGIN DEBUG] user has no stored password: id=" + user.getId());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        // 3) Detect if stored value looks like BCrypt hash
//        boolean looksBcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
//        System.out.println("[LOGIN DEBUG] user found id=" + user.getId() + " bcrypt=" + looksBcrypt);
//
//        // 4) Password match
//        boolean matches;
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        try {
//            if (looksBcrypt) {
//                matches = encoder.matches(dto.getPassword(), stored);
//            } else {
//                // fallback to plain compare
//                matches = dto.getPassword().equals(stored);
//            }
//        } catch (Exception ex) {
//            System.out.println("[LOGIN DEBUG] password match threw: " + ex.getMessage());
//            matches = false;
//        }
//
//        if (!matches) {
//            System.out.println("[LOGIN DEBUG] password mismatch for user id=" + user.getId());
//            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
//        }
//
//        // 5) Role check
//        if (!isAdmin(user)) {
//            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: admin role required");
//        }
//
//        Map<String, Object> resp = new HashMap<>();
//        resp.put("id", user.getId());
//        resp.put("username", user.getUsername());
//        resp.put("role", user.getRole() != null ? user.getRole().name() : null);
//        return ResponseEntity.ok(resp);
//    }




    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginDto dto) {
        // Basic validation
        if (dto == null || dto.getUsername() == null || dto.getPassword() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("username and password required");
        }

        String rawUsername = dto.getUsername().trim();
        String rawPassword = dto.getPassword();

        System.out.println("[LOGIN DEBUG] attempt username='" + rawUsername + "'");

        // 1) Try case-sensitive lookup first
        User user = userService.getUserByUsername(rawUsername);

        // 2) If not found, attempt case-insensitive lookup (optional repository method)
        if (user == null) {
            try {
                // This assumes you create a repository method findByUsernameIgnoreCase
                user = ((com.arunavidyalayam.school.repository.UserRepository) userService)
                        .findByUsernameIgnoreCase(rawUsername);
            } catch (ClassCastException | NoSuchMethodError ignored) {
                // repository method may not exist — ignore
            } catch (Exception ex) {
                System.out.println("[LOGIN DEBUG] error trying ignore-case lookup: " + ex.getMessage());
            }
        }

        if (user == null) {
            System.out.println("[LOGIN DEBUG] user not found for username='" + rawUsername + "'");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        String stored = user.getPassword();
        System.out.println("[LOGIN DEBUG] user found id=" + user.getId() + " storedPasswordPresent=" + (stored != null && !stored.isBlank()));

        if (stored == null || stored.isBlank()) {
            System.out.println("[LOGIN DEBUG] stored password empty for user id=" + user.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Detect bcrypt-like prefix
        boolean looksLikeBcrypt = stored.startsWith("$2a$") || stored.startsWith("$2b$") || stored.startsWith("$2y$");
        System.out.println("[LOGIN DEBUG] bcrypt-looking=" + looksLikeBcrypt);

        boolean matches = false;
        try {
            if (looksLikeBcrypt) {
                org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder enc = new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder();
                matches = enc.matches(rawPassword, stored);
            } else {
                // fallback plain compare
                matches = rawPassword.equals(stored);
            }
        } catch (Exception ex) {
            System.out.println("[LOGIN DEBUG] password verification threw: " + ex.getMessage());
            matches = false;
        }

        if (!matches) {
            System.out.println("[LOGIN DEBUG] password mismatch for user id=" + user.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }

        // Role check (allow teacher or admin as needed)
        if (!isAdmin(user) && !isTeacher(user)) {
            System.out.println("[LOGIN DEBUG] user has no usable role for login id=" + user.getId());
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        Map<String, Object> resp = new HashMap<>();
        resp.put("id", user.getId());
        resp.put("username", user.getUsername());
        resp.put("role", user.getRole() != null ? user.getRole().name() : null);

        System.out.println("[LOGIN DEBUG] login success id=" + user.getId() + " role=" + resp.get("role"));
        return ResponseEntity.ok(resp);
    }




    // helper - teacher check
    private boolean isTeacher(User user) {
        if (user == null) return false;
        try {
            return user.getRole() == com.arunavidyalayam.school.model.Role.TEACHER;
        } catch (Exception ignored) {}
        try {
            String r = String.valueOf(user.getRole());
            return "TEACHER".equalsIgnoreCase(r) || "ROLE_TEACHER".equalsIgnoreCase(r);
        } catch (Exception ignored) {}
        return false;
    }


    // Helper - determines if the user has ADMIN role.
    // Works with your Role enum (single-role setup). If you later move to a collection of roles,
    // update this helper accordingly.
    private boolean isAdmin(User user) {
        if (user == null) return false;

        // If User.role is Role enum
        try {
            Role r = user.getRole();
            if (r == Role.ADMIN) return true;
        } catch (ClassCastException ignored) {
            // fall through for other possible role representations
        }

        // If role stored as String in model (defensive)
        try {
            Object roleObj = user.getRole();
            if (roleObj != null) {
                String roleStr = roleObj.toString();
                if ("ADMIN".equalsIgnoreCase(roleStr) || "ROLE_ADMIN".equalsIgnoreCase(roleStr)) {
                    return true;
                }
            }
        } catch (Exception ignored) {
        }

        return false;
    }
}

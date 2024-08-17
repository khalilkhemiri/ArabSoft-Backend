package com.basicauth.app.controller;

import com.basicauth.app.dto.ReqRes;
import com.basicauth.app.entity.LoginCreds;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import com.basicauth.app.repository.RegisterNewUserRepository;
import com.basicauth.app.service.AuthService;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@CrossOrigin(origins = "http://angular-container")
public class LoginController {


    @Autowired
    private AuthService authService;
    @Autowired
    private RegisterNewUserRepository userProfileRepository;
    @PostMapping("/signup")
    public ResponseEntity<ReqRes> signUp(@RequestBody ReqRes signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }
    @PostMapping("/signin")
    public ResponseEntity<ReqRes> signIn(@RequestBody ReqRes signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/refresh")
    public ResponseEntity<ReqRes> refreshToken(@RequestBody ReqRes refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @PostMapping("/signout/{userId}")
    public ResponseEntity<ReqRes> signOut(@PathVariable Long userId) {
        return ResponseEntity.ok(authService.signOut(userId));
    }
    @GetMapping("/role/{role}")
    public List<UserProfile> getUsersByRole(@PathVariable Role role) {
        return authService.getUsersByRole(role);
    }

    @PutMapping("/role/{userId}")
    public UserProfile updateUserRole(@PathVariable Long userId, @RequestParam Role newRole) {
        return authService.updateUserRole(userId, newRole);
    }
    @PostMapping("/assignPersonnelToChef")
    public String assignPersonnelToChef(@RequestParam Long personnelId, @RequestParam Long chefId) {
        return authService.assignPersonnelToChef(personnelId, chefId);
    }
    @PutMapping("/unassign/{id}")
    public ResponseEntity<Map<String, String>> unassignPersonnel(@PathVariable Long id) {
        Map<String, String> response = new HashMap<>();
        try {
            String message = authService.unassignPersonnelFromChef(id);
            response.put("message", message);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("error", "An error occurred");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
        }
    }

    @GetMapping("/all")
    public List<UserProfile> getAllUsersWithChefs() {
        return authService.getAllUsersWithChefs();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ReqRes> deleteUser(@PathVariable Long id) {
        ReqRes response = authService.deleteUserById(id);
        return new ResponseEntity<>(response, HttpStatus.valueOf(response.getStatusCode()));
    }
    @GetMapping("/chef/{chefHierarchiqueId}")
    public List<UserProfile> getPersonnelsByChef(@PathVariable Long chefHierarchiqueId) {
        return authService.getPersonnelsByChefHierarchique(chefHierarchiqueId);
    }
    @GetMapping("/personnel/count/{idchef}")
    public ResponseEntity<Long> countPersonnelByChefHierarchique(@PathVariable Long idchef) {
        Long count = userProfileRepository.countPersonnelByChefHierarchique(idchef);
        return ResponseEntity.ok(count);
    }
    @PostMapping("/sendMeetingLink")
    public ResponseEntity<?> sendMeetingLink(@RequestParam Long chefHierarchiqueId, @RequestParam String meetingLink) {
        try {
            authService.sendMeetingLinkToEmployees(chefHierarchiqueId, meetingLink);
            return ResponseEntity.ok("Meeting link sent successfully.");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error sending meeting link.");
        }
    }

}
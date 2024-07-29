package com.basicauth.app.service;

import com.basicauth.app.dto.ReqRes;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import com.basicauth.app.repository.RegisterNewUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class AuthService {

    @Autowired
    private RegisterNewUserRepository ourUserRepo;
    @Autowired
    private JWTUtils jwtUtils;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {

            if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
                // Si un utilisateur avec cet email existe déjà, afficher un message d'erreur
                resp.setStatusCode(400); // Bad Request
                resp.setMessage("Email already exists");
                return resp;
            }

            UserProfile users = new UserProfile();
            users.setEmail(registrationRequest.getEmail());
            users.setName(registrationRequest.getName());
            users.setNumber(registrationRequest.getNumber());
            users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            users.setRole(Role.EMPLOYE); // Explicitly set role to null


            UserProfile ourUserResult = ourUserRepo.save(users);
            if (ourUserResult != null && ourUserResult.getId() > 0) {
                resp.setUsers(ourUserResult);
                resp.setMessage("User Saved Successfully");
                resp.setStatusCode(200);
            }
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }

    public ReqRes signIn(ReqRes signinRequest) {
        ReqRes response = new ReqRes();

        try {
            // Retrieve user by email
            var userOptional = ourUserRepo.findByEmail(signinRequest.getEmail());
            if (userOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found.");
                return response;
            }

            var user = userOptional.get();
            String storedPassword = user.getPassword();

            // Check if the stored password is encoded (BCrypt format starts with $2a$)
            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean isPasswordEncoded = storedPassword.startsWith("$2a$");

            // Authenticate the user
            if (isPasswordEncoded) {
                // Use BCrypt to match the password
                if (!passwordEncoder.matches(signinRequest.getPassword(), storedPassword)) {
                    response.setStatusCode(401); // Unauthorized
                    response.setMessage("Invalid email or password.");
                    return response;
                }
            } else {
                // Handle plain text password authentication
                if (!storedPassword.equals(signinRequest.getPassword())) {
                    response.setStatusCode(401); // Unauthorized
                    response.setMessage("Invalid email or password.");
                    return response;
                }

                // Encode the plain text password and update the user's password in the database
                user.setPassword(passwordEncoder.encode(signinRequest.getPassword()));
                ourUserRepo.save(user);
            }

            // Generate JWT tokens
            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            // Prepare the response
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
            response.setUsers(user);

            // Update user's online status
            user.setOnline(true);
            ourUserRepo.save(user);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


    public ReqRes refreshToken(ReqRes refreshTokenReqiest) {
        ReqRes response = new ReqRes();
        String ourEmail = jwtUtils.extractUsername(refreshTokenReqiest.getToken());
        UserProfile users = ourUserRepo.findByEmail(ourEmail).orElseThrow();
        if (jwtUtils.isTokenValid(refreshTokenReqiest.getToken(),  users)) {
            var jwt = jwtUtils.generateToken((UserDetails) users);
            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshTokenReqiest.getToken());
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Refreshed Token");
        }
        response.setStatusCode(500);
        return response;
    }

    public List<UserProfile> getUsersByRole(Role role) {
        return ourUserRepo.findByRole(role);
    }

    public UserProfile updateUserRole(Long userId, Role newRole) {
        UserProfile user = ourUserRepo.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        user.setRole(newRole);
        return ourUserRepo.save(user);
    }

    public String assignPersonnelToChef(Long personnelId, Long chefId) {
        Optional<UserProfile> personnelOpt = ourUserRepo.findById(personnelId);
        Optional<UserProfile> chefOpt = ourUserRepo.findById(chefId);

        if (personnelOpt.isPresent() && chefOpt.isPresent()) {
            UserProfile personnel = personnelOpt.get();
            UserProfile chef = chefOpt.get();

            if (chef.getRole() == Role.CHEF) {
                personnel.setRole(Role.PERSONNEL);
                personnel.setChefHierarchique(chef);
                ourUserRepo.save(personnel);
                return "Personnel assigned to chef successfully.";
            } else {
                return "Selected user is not a CHEF.";
            }
        } else {
            return "Personnel or Chef not found.";
        }
    }
    public List<UserProfile> getAllByRole(Role role) {
        return ourUserRepo.findByRole(role);
    }



    public List<UserProfile> getAllUsersWithChefs() {
        return ourUserRepo.findAll();
    }
}

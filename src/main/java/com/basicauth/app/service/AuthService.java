package com.basicauth.app.service;

import com.basicauth.app.dto.ReqRes;
import com.basicauth.app.entity.UserProfile;
import com.basicauth.app.enums.Role;
import com.basicauth.app.repository.RegisterNewUserRepository;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
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
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

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
    @Autowired
    private JavaMailSender mailSender;







    public ReqRes signUp(ReqRes registrationRequest) {
        ReqRes resp = new ReqRes();
        try {

            if (ourUserRepo.findByEmail(registrationRequest.getEmail()).isPresent()) {
                resp.setStatusCode(400);
                resp.setMessage("Email already exists");
                return resp;
            }

            UserProfile users = new UserProfile();
            users.setEmail(registrationRequest.getEmail());
            users.setName(registrationRequest.getName());
            users.setNumber(registrationRequest.getNumber());
            users.setPassword(passwordEncoder.encode(registrationRequest.getPassword()));
            users.setRole(Role.EMPLOYE);


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
            var userOptional = ourUserRepo.findByEmail(signinRequest.getEmail());
            if (userOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found.");
                return response;
            }

            var user = userOptional.get();
            String storedPassword = user.getPassword();

            BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
            boolean isPasswordEncoded = storedPassword.startsWith("$2a$");

            if (isPasswordEncoded) {
                if (!passwordEncoder.matches(signinRequest.getPassword(), storedPassword)) {
                    response.setStatusCode(401);
                    response.setMessage("Invalid email or password.");
                    return response;
                }
            } else {
                if (!storedPassword.equals(signinRequest.getPassword())) {
                    response.setStatusCode(401);
                    response.setMessage("Invalid email or password.");
                    return response;
                }

                user.setPassword(passwordEncoder.encode(signinRequest.getPassword()));
                ourUserRepo.save(user);
            }

            var jwt = jwtUtils.generateToken(user);
            var refreshToken = jwtUtils.generateRefreshToken(new HashMap<>(), user);

            response.setStatusCode(200);
            response.setToken(jwt);
            response.setRefreshToken(refreshToken);
            response.setExpirationTime("24Hr");
            response.setMessage("Successfully Signed In");
            response.setUsers(user);

            user.setOnline(true);
            ourUserRepo.save(user);

        } catch (Exception e) {
            response.setStatusCode(500);
            response.setError(e.getMessage());
        }
        return response;
    }


    public ReqRes signOut(Long userId) {
        ReqRes response = new ReqRes();

        try {
            var userOptional = ourUserRepo.findById(userId);
            if (userOptional.isEmpty()) {
                response.setStatusCode(404);
                response.setMessage("User not found.");
                return response;
            }

            var user = userOptional.get();

            jwtUtils.invalidateToken(user.getToken());

            user.setOnline(false);
            ourUserRepo.save(user);
            response.setStatusCode(200);
            response.setMessage("Successfully Signed Out");

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

    public String unassignPersonnelFromChef(Long personnelId) {
        Optional<UserProfile> personnelOpt = ourUserRepo.findById(personnelId);

        if (personnelOpt.isPresent()) {
            UserProfile personnel = personnelOpt.get();

            if (personnel.getChefHierarchique() != null) {
                personnel.setChefHierarchique(null);
                personnel.setRole(Role.EMPLOYE);

                ourUserRepo.save(personnel);
                return "Personnel has been successfully unassigned from the chef.";
            } else {
                return "Personnel is not assigned to any chef.";
            }
        } else {
            return "Personnel not found.";
        }
    }
    public List<UserProfile> getAllByRole(Role role) {
        return ourUserRepo.findByRole(role);
    }

    public List<UserProfile> getPersonnelsByChefHierarchique(Long chefHierarchiqueId) {
        return ourUserRepo.findByChefHierarchiqueId(chefHierarchiqueId);
    }

    public List<UserProfile> getAllUsersWithChefs() {
        return ourUserRepo.findAll();
    }
    public ReqRes deleteUserById(Long userId) {
        ReqRes resp = new ReqRes();
        try {
            var userOptional = ourUserRepo.findById(userId);
            if (userOptional.isEmpty()) {
                resp.setStatusCode(404);
                resp.setMessage("User not found.");
                return resp;
            }

            ourUserRepo.deleteById(userId);

            resp.setStatusCode(200);
            resp.setMessage("User deleted successfully.");
        } catch (Exception e) {
            resp.setStatusCode(500);
            resp.setError(e.getMessage());
        }
        return resp;
    }



    public void sendEmail(String to, String body) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("khalilkhemiri681@gmail.com");
        message.setTo(to);
        message.setSubject("meeting");
        message.setText(body);

        mailSender.send(message);
    }

    public void sendMeetingLinkToEmployees(Long chefHierarchiqueId, String meetingLink) throws MessagingException {
        List<UserProfile> employees = getPersonnelsByChefHierarchique(chefHierarchiqueId);
        for (UserProfile employee : employees) {
            sendEmail(employee.getEmail(), meetingLink);
        }
    }




}

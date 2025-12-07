package com.wings.WingsSecurity.service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.wings.WingsSecurity.entity.Leave;
import com.wings.WingsSecurity.entity.LeaveRequest;
import com.wings.WingsSecurity.entity.Role;
import com.wings.WingsSecurity.entity.Status;
import com.wings.WingsSecurity.entity.User;
import com.wings.WingsSecurity.repository.LeaveRepository;
import com.wings.WingsSecurity.repository.UserRepository;

@Service
public class LeaveService {

    @Autowired
    LeaveRepository leaveRepository;

    @Autowired
    UserRepository userRepository;

    public ResponseEntity<Object> addLeaveRequest(String username, LeaveRequest leaveRequest) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (!role.getRole().equals("USER")) {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

            Leave leave = new Leave();

            leave.setAppiedOn(leaveRequest.getAppliedOn());
            leave.setCategory(leaveRequest.getCategory());
            leave.setDescription(leaveRequest.getDescription());
            leave.setNoOfDays(leaveRequest.getNoOfDays());
            leave.setStatus(Status.PENDING);
            // customization of user
            leave.setUser(userDB);

            Leave savedLeave = leaveRepository.save(leave);

            return ResponseEntity.status(201).body(savedLeave);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    public ResponseEntity<Object> updateUserComment(String username, int leaveId, String comment) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (!role.getRole().equals("USER")) {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

            Optional<Leave> leaveOptn = leaveRepository.findById(leaveId);

            if (leaveOptn.isEmpty()) {
                return ResponseEntity.status(401).body("Leave does not exist : LeaveService");
            }

            Leave leave = leaveOptn.get();
            User user = leave.getUser();

            if (!userDB.getEmail().equals(user.getEmail())) {
                return ResponseEntity.status(401).body("You do not own this leave : LeaveService");
            }

            // Update the comments
            leave.setDescription(comment);

            Leave savedLeave = leaveRepository.save(leave);

            return ResponseEntity.ok(savedLeave);

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    public ResponseEntity<Object> getLeaves(String username) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (role.getRole().equals("ADMIN")) {
                return ResponseEntity.ok(leaveRepository.findAll());
            } else if (role.getRole().equals("USER")) {
                List<Leave> customLeave = leaveRepository.findByUserEmail(username);
                return ResponseEntity.ok(customLeave);
            } else {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    public ResponseEntity<Object> updateLeaveStatus(String username, int leaveId, String status) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (!role.getRole().equals("ADMIN")) {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

            Optional<Leave> leaveOptn = leaveRepository.findById(leaveId);

            if (leaveOptn.isEmpty()) {
                return ResponseEntity.status(401).body("Leave does not exist : LeaveService");
            }

            Leave leave = leaveOptn.get();

            if (!(status.equalsIgnoreCase("APPROVED") || status.equalsIgnoreCase("REJECTED"))) {
                return ResponseEntity.status(401).body("Invalid Status : LeaveService");
            }
            leave.setStatus(Status.valueOf(status.toUpperCase()));
            leaveRepository.save(leave);

            return ResponseEntity.ok("Leave status updated : " + status.toUpperCase());

        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    public ResponseEntity<Object> deleteLeave(String username, int id) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (!role.getRole().equals("ADMIN")) {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

            Optional<Leave> leaveOptn = leaveRepository.findById(id);

            if (leaveOptn.isEmpty()) {
                return ResponseEntity.status(401).body("Leave does not exist : LeaveService");
            }

            Leave leave = leaveOptn.get();
            leaveRepository.delete(leave);

            return ResponseEntity.ok("Leave deleted succesfully");
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    public ResponseEntity<Object> getLeavesByNoOfDays(String username, int noOfDays) {
        try {
            Optional<User> userOptn = getUserByUsername(username);

            if (userOptn.isEmpty()) {
                // Sanity check #1 : User does not exists
                return ResponseEntity.status(404).body("User does not exists : LeaveService");
            }

            User userDB = userOptn.get();
            Role role = userDB.getRole();
            // Sanity check #2 : Role sanity check
            if (!role.getRole().equals("ADMIN")) {
                return ResponseEntity.status(401).body("Unauthorized : LeaveService");
            }

            List<Leave> leaves = leaveRepository.findByNoOfDaysGreaterThan(noOfDays);
            Set<String> usernames = new HashSet<>();

            for (Leave leave : leaves) {
                usernames.add(leave.getUser().getEmail());
            }

            return ResponseEntity.ok(usernames);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Error in Server : " + e.getMessage());
        }
    }

    private Optional<User> getUserByUsername(String emailId) {
        return userRepository.findByEmail(emailId);
    }
}

package com.wings.WingsSecurity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.wings.WingsSecurity.entity.LeaveRequest;
import com.wings.WingsSecurity.service.LeaveService;
import com.wings.WingsSecurity.util.JwtUtil;

@RestController
@RequestMapping("/leave")
public class LeaveController {

    @Autowired
    JwtUtil jwtUtil;

    @Autowired
    LeaveService leaveService;

    @PostMapping("/user")
    public ResponseEntity<Object> addLeaveRequest(@RequestHeader("Authorization") String authHeader,
            @RequestBody LeaveRequest leaveRequest) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.addLeaveRequest(username, leaveRequest);
    }

    @PutMapping("/comment")
    public ResponseEntity<Object> updateUserComment(@RequestHeader("Authorization") String authHeader,
            @RequestParam int leaveId, @RequestParam String comment) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.updateUserComment(username, leaveId, comment);
    }

    @GetMapping("/user")
    public ResponseEntity<Object> getLeaves(@RequestHeader("Authorization") String authHeader) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.getLeaves(username);
    }

    @PutMapping("/status")
    public ResponseEntity<Object> updateLeaveStatus(@RequestHeader("Authorization") String authHeader,
            @RequestParam int leaveId, @RequestParam String status) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.updateLeaveStatus(username, leaveId, status);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteLeave(@RequestHeader("Authorization") String authHeader, @PathVariable int id) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.deleteLeave(username, id);
    }

    @GetMapping("/days")
    public ResponseEntity<Object> getLeavesByNoOfDays(@RequestHeader("Authorization") String authHeader,
            @RequestParam int noOfDays) {
        String jwt = authHeader.substring(7);
        String username = jwtUtil.extractUsername(jwt);
        return leaveService.getLeavesByNoOfDays(username, noOfDays);
    }

}

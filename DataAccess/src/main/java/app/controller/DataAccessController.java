package app.controller;

import dto.*;
import app.service.DataAccessService;
import lombok.RequiredArgsConstructor;
import dbmodel.Group;
import dbmodel.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import javax.naming.AuthenticationException;

@RestController
@RequestMapping("/api/v1/data")
@RequiredArgsConstructor
public class DataAccessController {

    private final DataAccessService dataAccessService;

    @PostMapping("/auth/login")
    public ResponseEntity<String> login(@RequestBody AuthenticationRequest request) {
        try {
            return ResponseEntity.ok(dataAccessService.authenticate(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (AuthenticationException | BadCredentialsException | UsernameNotFoundException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/auth/prolong")
    public ResponseEntity<String> prolong(@RequestBody ValidationRequest request) {
        try {
            return ResponseEntity.ok(dataAccessService.prolong(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (AuthenticationException | BadCredentialsException | UsernameNotFoundException exception){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/all")
    public ResponseEntity<User[]> getUsers(){
        try {
            System.out.println("/user/all");
            return ResponseEntity.ok(dataAccessService.getUsers());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/user/all-logins")
    public ResponseEntity<String[]> getUsersLogins(){
        try {
            System.out.println("/user/all-logins");
            return ResponseEntity.ok(dataAccessService.getUsersLogins());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/user/get")
    public ResponseEntity<User> getUser(@RequestBody UserDetailsRequest request) {
        try {
            System.out.println("/user/get");
            return ResponseEntity.ok(dataAccessService.getUser(request));
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/user/add")
    public ResponseEntity<String> addUser(@RequestBody RegistrationRequest request) {
        try {
            System.out.println("/user/add");
            return ResponseEntity.ok(dataAccessService.addUser(request));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage() + " " + exception.getClass().getName());
        }
    }

    @DeleteMapping("/user/delete")
    public ResponseEntity<String> deleteUser(@RequestBody UserDetailsRequest request){
        try {
            System.out.println("/user/delete");
            return ResponseEntity.ok(dataAccessService.deleteUser(request));
        } catch (IllegalArgumentException | IllegalStateException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage() + " " + exception.getClass().getName());
        }
    }

    @GetMapping("/group/all-names")
    public ResponseEntity<String[]> getGroupsNames(){
        try {
            System.out.println("/group/all-names");
            return ResponseEntity.ok(dataAccessService.getGroupsNames());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/group/all")
    public ResponseEntity<Group[]> getGroups(){
        try {
            System.out.println("/group/all");
            return ResponseEntity.ok(dataAccessService.getGroups());
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/group/add")
    public ResponseEntity<String> addGroup(@RequestBody Group group){
        try {
            System.out.println("/group/add");
            dataAccessService.addGroup(group);
            return ResponseEntity.ok("The group was successfully added");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @PostMapping("/group/update")
    public ResponseEntity<String> updateGroup(@RequestBody Group group){
        try {
            System.out.println("/group/update");
            dataAccessService.updateGroup(group);
            return ResponseEntity.ok("The group was successfully updated");
        } catch (IllegalArgumentException exception) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

}

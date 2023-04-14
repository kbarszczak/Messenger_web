package app.controller;

import app.service.ConversationService;
import dto.CreateGroupRequest;
import dto.NewMessageRequest;
import dto.PoorMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/conv")
@RequiredArgsConstructor
public class ConversationController {

    private final ConversationService conversationService;

    @GetMapping("/users/all")
    public ResponseEntity<String[]> getUsers(){
        try{
            System.out.println("/users/all");
            return ResponseEntity.status(HttpStatus.OK).body(conversationService.getUsersLogins());
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/group/all")
    public ResponseEntity<String[]> getGroups(){
        try{
            System.out.println("/group/all");
            return ResponseEntity.status(HttpStatus.OK).body(conversationService.getGroupsNames());
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/group/create")
    public ResponseEntity<String> createGroup(@RequestBody CreateGroupRequest request){
        try{
            System.out.println("/group/create");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            conversationService.createGroup(request, auth.getName());
            return ResponseEntity.status(HttpStatus.OK).body("Group was successfully created");
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @GetMapping("/group/id")
    public ResponseEntity<String> getGroupId(@RequestParam("name") String name){
        try{
            System.out.println("/group/id");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.status(HttpStatus.OK).body(conversationService.getGroupId(name, auth.getName()));
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

    @GetMapping("/group/messages")
    public ResponseEntity<PoorMessage[]> getGroupMessages(@RequestParam("id") String id){
        try{
            System.out.println("/group/messages");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            return ResponseEntity.status(HttpStatus.OK).body(conversationService.getGroupMessages(id, auth.getName()));
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/message")
    public ResponseEntity<String> addMessage(@RequestBody NewMessageRequest request){
        try{
            System.out.println("/message");
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            conversationService.addMessage(request.getMessage(), request.getGroupId(), auth.getName());
            return ResponseEntity.status(HttpStatus.OK).body("Message successfully sent");
        } catch (IllegalStateException | IllegalArgumentException exception){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(exception.getMessage());
        } catch (Exception exception){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(exception.getMessage());
        }
    }

}

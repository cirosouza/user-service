package br.edu.infnet.user_service.controller;

import br.edu.infnet.user_service.exception.ResourceNotFoundException;
import br.edu.infnet.user_service.model.User;
import br.edu.infnet.user_service.payload.DetailPayload;
import br.edu.infnet.user_service.service.implementation.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/users")
public class UserController {

    Logger logger = LoggerFactory.getLogger(UserController.class);

    final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping()
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAll();

        if(users.isEmpty()) {
            logger.info("Não há usuários a serem apresentados!");
            return ResponseEntity.noContent().build();
        } else {
          return ResponseEntity.ok(users);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getUserById(@PathVariable int id) {
        try {
            Optional<User> userFound = userService.getById(id);
            logger.info("Usuário com id " + id + " encontrado!");
            return ResponseEntity.status(HttpStatus.OK).body(userFound);
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado usuario com id: " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }

    @PostMapping()
    public ResponseEntity<User> addUser(@RequestBody User user) {
        User newUser = userService.create(user);
        logger.info("Criado usuario com id " + newUser.getId());
        return ResponseEntity.status(HttpStatus.CREATED).body(newUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@RequestBody User user, @PathVariable int id) {
        try{
            User updatedUser = userService.update(id, user);
            logger.info("Atualizado usuario com id " + updatedUser.getId());
            return ResponseEntity.status(HttpStatus.OK).body(updatedUser);
        } catch (ResourceNotFoundException e) {
            logger.error("Não encontrado usuario com o id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(e.getMessage()));
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable int id) {
        try {
            userService.delete(id);
            logger.info("Deletado usuario com id " + id);
            return ResponseEntity.status(HttpStatus.OK).body(new DetailPayload("Deletado com sucesso"));
        } catch (ResourceNotFoundException ex) {
            logger.error("Não encontrado usuário com id " + id);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new DetailPayload(ex.getMessage()));
        }
    }
}

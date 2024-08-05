package br.edu.infnet.user_service.controller;

import br.edu.infnet.user_service.exception.ResourceNotFoundException;
import br.edu.infnet.user_service.model.User;
import br.edu.infnet.user_service.payload.DetailPayload;
import br.edu.infnet.user_service.service.implementation.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Retorna uma lista de usuários")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ok",
                    content = {@Content(mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = User.class)))}),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
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

    @Operation(summary = "Retorna um usuário pelo seu ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) }) })
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

    @Operation(summary = "Cria um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Post publicado com sucesso", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = User.class)) })
    })
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

    @Operation(summary = "Deleta um usuário")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deletado com sucesso!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) }),
            @ApiResponse(responseCode = "404", description = "Usuário não encontrado!", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = DetailPayload.class)) })
    })
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

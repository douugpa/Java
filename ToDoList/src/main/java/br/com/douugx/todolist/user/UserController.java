package br.com.douugx.todolist.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import at.favre.lib.crypto.bcrypt.BCrypt;

@RestController
@RequestMapping("/users")

public class UserController {

  @Autowired
  private IUserRepository userRepository;

  @SuppressWarnings("rawtypes")
  @PostMapping("/")
  public ResponseEntity createUser(@RequestBody UserModel UserModel) {
    var user = userRepository.findByUsername(UserModel.getUsername());

    if (user != null) {
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Usuário já cadastrado!");
    }

    var passwordHashed = BCrypt.withDefaults().hashToString(12, UserModel.getPassword().toCharArray());

    UserModel.setPassword(passwordHashed);

    var userCreated = this.userRepository.save(UserModel);
    return ResponseEntity.status(HttpStatus.CREATED).body(userCreated);
  }
}

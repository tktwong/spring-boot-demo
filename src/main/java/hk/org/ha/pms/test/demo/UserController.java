package hk.org.ha.pms.test.demo;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/hello")
    String sayHello() {
        return "hello world!!";
    }

    @RolesAllowed("admin")
    @PostMapping("/users")
    User createUser(@Valid @RequestBody User user) {
        return userService.create(user);
    }

    @RolesAllowed({"user","admin"})
    @GetMapping("/users")
    List<User> getAll() {
       return userService.findAll();
    }

    @RolesAllowed("admin")
    @PutMapping("/users")
    User updateUser(@Valid @RequestBody User userDetail) {
        return userService.update(userDetail);
    }

    @RolesAllowed("admin")
    @DeleteMapping("/users/{id}")
    Map<String, Boolean> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        Map<String, Boolean> response = new HashMap<>();
        response.put("deleted", Boolean.TRUE);
        return response;
    }
}

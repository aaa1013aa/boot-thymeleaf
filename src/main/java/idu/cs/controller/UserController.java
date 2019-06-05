package idu.cs.controller;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;

import idu.cs.domain.User;
import idu.cs.entity.UserEntity;
import idu.cs.exception.ResourceNotFoundException;
import idu.cs.repository.UserRepository;
import idu.cs.service.UserService;

@Controller 
// Spring Famework에게 이 클래스로 부터 작성된 객체는 Controller 역할을 함을 알려줌
// Spring 이 이 클래스로부터 Bean 객체를 생성해서 등록할 수 있음
public class UserController { 
   @Autowired UserService userService;
   
   @GetMapping("/")
   public String home(Model model) {
      return "index";
   }
   
   @GetMapping("/user-login")
   public String getLoginFrom(Model model) {
      return "login";
   }
   
   @GetMapping("/user-logout")
   public String getLogoutFrom(Model model) {
      return "logout";
   }
   
   @PostMapping("/login")
   public String loginUser(@Valid UserEntity user, HttpSession session) {
      System.out.println("login process : " + user.getUserId());
      User sessionUser = userService.getUserByUserId(user.getUserId());
      if(sessionUser == null) {
         System.out.println("id error");
         return "redirect:/user-login";
      }
      if(!sessionUser.getUserPw().equals(user.getUserPw())) {
         System.out.println("pw error");
         return "redirect:/user-login";
      }
      session.setAttribute("user", sessionUser);
      return "redirect:/";
   } 
   @GetMapping("/logout")
   public String logoutUser(HttpSession session) {
      session.removeAttribute("user");
      return "redirect:/";
   }
   @GetMapping("/users")
   public String getAllUser(Model model, HttpSession session) {
      model.addAttribute("users", userService.getUsers());
      return "userlist";
   }
   
   @GetMapping("/user-regist")
   public String getRegForm(Model model) {
      return "register";
   }

   
   @PostMapping("/users")
   public String createUser(@Valid User user, Model model) {
       userService.saveUser(user);
      return "redirect:/users"; // get 방식으로 해당 url로 재지정함
   }
   @GetMapping("/user-update-form")
   public String getUpdateForm(Model model, HttpSession session) {
	   User user = (User) session.getAttribute("user");
	   model.addAttribute("user", user);
	   return "info";
   }
   /*
   @GetMapping("/users/{id}")
   public String getUserById(@PathVariable(value = "id") Long userId, Model model)
         throws ResourceNotFoundException {
      UserEntity user = userRepo.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found for this id :: " + userId));
      model.addAttribute("user", user);
      
      return "user";
      //return ResponseEntity.ok().body(user);
   }
   
   @GetMapping("/users/fn")
   public String getUserByName(@Param(value = "name") String name, Model model)
         throws ResourceNotFoundException {
      List<UserEntity> users = userRepo.findByName(name);
      model.addAttribute("users", users);
      return "userlist";
   }
   */
   @PutMapping("/users/{id}")
   public String updateUser(@PathVariable(value = "id") Long id, 
		   @Valid User updateUser, Model model) {
      User user = userService.getUserById(id);
     // user.setUserId(updateUser.getUserId());
      user.setUserPw(updateUser.getUserPw());
      user.setName(updateUser.getName());
      user.setCompany(updateUser.getCompany());
      userService.updateUser(user);
      return "redirect:/users";
   }
   /*
   @DeleteMapping("/users/{id}")
   public String deleteUser(@PathVariable(value = "id") Long userId, Model model) {
      UserEntity user = userRepo.findById(userId).get();
      userRepo.delete(user);
      model.addAttribute("name", user.getName());
      return "user-deleted";
   }
   */
}
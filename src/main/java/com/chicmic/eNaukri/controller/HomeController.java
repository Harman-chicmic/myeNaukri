package com.chicmic.eNaukri.controller;

import com.chicmic.eNaukri.Dto.UsersDto;
import com.chicmic.eNaukri.model.Job;
import com.chicmic.eNaukri.model.PasswordResetToken;
import com.chicmic.eNaukri.model.Users;
import com.chicmic.eNaukri.repo.UsersRepo;
import com.chicmic.eNaukri.service.JobService;
import com.chicmic.eNaukri.service.PasswordResetService;
import com.chicmic.eNaukri.service.UserServiceImpl;
import com.chicmic.eNaukri.service.UsersService;
import jakarta.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import static com.chicmic.eNaukri.ENaukriApplication.passwordEncoder;


@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class HomeController {

    private final UsersRepo usersRepo;
    private final UsersService usersService;
    private final UserServiceImpl userService;
    private final JobService jobService;
    private final PasswordResetService passwordResetService;

    @GetMapping
    public String homePage(){
        System.out.println("1");
        return "In Home Page";
    }
//    @GetMapping("login-page")
//    public String loginPage(){
//        return "in Login Page";
//    }
//    @PostMapping("login")
//    public String userLogin(@RequestBody Map<Object,Object> map){
//        return "login successful";
//    }
    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<String> register(@Valid Users dto, @RequestParam(required = false,value = "imgFile") MultipartFile imgFile,
                                           @RequestParam(value = "resumeFile",required = false) MultipartFile resumeFile) throws IOException {
        usersService.register(dto, imgFile, resumeFile);
        return ResponseEntity.ok("User registered successfully");
    }
    @PostMapping("/updateProfile")
    public void updateProfile(UsersDto user, @RequestParam(value="resumeFile",required = false)MultipartFile resumeFile, @RequestParam(value="imgFile",required = false)MultipartFile imgFile) throws IOException {
        usersService.updateUser(user,imgFile,resumeFile);
    }
    @GetMapping("logout-user")
    public String logout(HttpServletRequest request, HttpServletResponse response){
        userService.logout(request,response);
        return "Logout Successful";
    }
    @GetMapping("forgot-password")
    public String forgotPassword(){
        return "forgot password?";
    }
    @PostMapping("forgot-password")
    public boolean sendForgotPaswdLink(@RequestParam String email){
        return true;
    }

    @GetMapping("jobs")
    public List<Job> displayJobs(@RequestParam(required = false,name = "q") String query,
                                 @RequestParam(required = false,name = "location") String location,
                                 @RequestParam(required = false,name = "type") String jobType,
                                 @RequestParam(required = false,name = "postedOn") String postedOn,
                                 @RequestParam(required = false,name = "remoteHybridOnsite") String remoteHybridOnsite){
        return jobService.displayFilteredPaginatedJobs(query,location,jobType,postedOn,remoteHybridOnsite);
    }

    @GetMapping("{jobId}/listInterestedApplicants")
    public Collection<?> listInterestedApplicants(@PathVariable("jobId")Long jobId){
        return jobService.listInterestedApplicants(jobId);
    }

    @PostMapping("/set-new-password")
    public ResponseEntity<String> setPassword(HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        String email = request.getParameter("email");
        Users user = userService.getUserByEmail(email);
        passwordResetService.createPasswordResetTokenForUser(user);
        return ResponseEntity.ok("Mail sent");
    }
    @GetMapping("/enterNewPassword/{token}/{uuid}")
    public String Enter(HttpServletRequest
                                request, @PathVariable("token") String token, @PathVariable("uuid") String uuid, Model model) {
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=InvalidToken";
        }
        model.addAttribute("token", token);
        return "forgotPasswordForm";
    }
    @PostMapping("/enterNewPassword/{token}/{uuid}")
    public String resetPassword(HttpServletRequest
                                        request,@PathVariable("token") String token,@PathVariable("uuid") String uuid){
        PasswordResetToken passwordResetRequest = passwordResetService.findByToken(token);
        Users user = userService.getUserByUuid(uuid);
        System.out.println(user);
        String newPassword=request.getParameter("password");
        if (passwordResetRequest == null || passwordResetRequest.getExpiryDate().isBefore(LocalDateTime.now())) {
            return "redirect:/login?error=InvalidToken";
        }
        user.setPassword(passwordEncoder().encode(newPassword));
        usersRepo.save(user);
        passwordResetService.delete(passwordResetRequest);
        return "user-login";
    }
    @PostMapping("/new")
    @ResponseBody
    public ResponseEntity<String> nwe(@RequestParam("jobId") Long jobId){
        jobService.getUsersWithMatchingSkills(jobId);
        return ResponseEntity.ok("k");
    }
}

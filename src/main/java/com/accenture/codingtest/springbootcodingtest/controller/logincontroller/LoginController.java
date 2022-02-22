package com.accenture.codingtest.springbootcodingtest.controller.logincontroller;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/login")
public class LoginController {

    @RequestMapping("/success")
    public ResponseEntity<String> login(HttpSession session2) {
        return new ResponseEntity<String>("Successfully Logged in", new HttpHeaders(), HttpStatus.OK);
    }
    @RequestMapping("/logout")
    public ResponseEntity<String> logout(HttpSession session2) {
        session2.invalidate();
        return new ResponseEntity<String>("Successfully Logged out", new HttpHeaders(), HttpStatus.OK);

    }
}

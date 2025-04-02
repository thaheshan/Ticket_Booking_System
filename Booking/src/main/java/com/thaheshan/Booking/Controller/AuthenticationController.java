//package com.thaheshan.Booking.Controller;
//
//import com.thaheshan.Booking.Service.Authentication.AuthService;
//import com.thaheshan.Booking.dto.SignupRequestDTO;
//import com.thaheshan.Booking.dto.UserDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.HttpStatusCode;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.ResponseBody;
//
//public class AuthenticationController {
//
//    @Autowired
//    private AuthService authService;
//
//    public ResponseEntity<?>  SignupClient(@ResponseBody SignupRequestDTO SignupRequestDto) {
//
//        if (authService.isPresentByEmail(SignupRequestDto.getEmail()) ){
//
//            return new ResponseEntity<>("The Client Already Exist with this email: ", HttpStatus.NOT_ACCEPTABLE);
//
//
//
//        }
//
//        UserDto CreatedUser = authService.
//
//
//    }
//
//
//}

//package com.thaheshan.Booking.Service.Authentication;
//
//import com.thaheshan.Booking.Entity.User;
//import com.thaheshan.Booking.Enums.UserRole;
//import com.thaheshan.Booking.Repository.UserRepository;
//import com.thaheshan.Booking.dto.SignupRequestDTO;
//import com.thaheshan.Booking.dto.UserDto;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//@Service
//public class AuthenticationServiceImp extends AuthService {
//
//    @Autowired
//    private UserRepository userRepository;
//
//    // Method to handle user signup
//    public UserDto signupClient(SignupRequestDTO signupRequestDTO) {
//        User newUser = new User();
//
//        // Set user properties from the DTO
//        newUser.setFirstName(signupRequestDTO.getFirstName());
//        newUser.setLastName(signupRequestDTO.getLastName());
//        newUser.setEmailAddress(signupRequestDTO.getEmailAddress());
//        newUser.setUserPassword(signupRequestDTO.getUserPassword());
//        newUser.setPhoneNumber(signupRequestDTO.getPhoneNumber());
//
//        // Set the role to CUSTOMER
//        newUser.setRole(UserRole.CUSTOMER);
//
//        // Save the new user and return the corresponding DTO
//        User savedUser = userRepository.save(newUser);
//        return new UserDto(savedUser);  // Assuming you have a constructor in UserDto to convert from User entity.
//    }
//
//    // Method to check if the email already exists
//    public boolean isPresentByEmail(String email) {
//        return userRepository.existsByEmailAddress(email);  // Custom method in UserRepository to check by email.
//    }
//}

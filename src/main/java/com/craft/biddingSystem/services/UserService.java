package com.craft.biddingSystem.services;

import com.craft.biddingSystem.exception.UserNotFoundException;
import com.craft.biddingSystem.models.User;
import com.craft.biddingSystem.models.enums.Role;
import com.craft.biddingSystem.repository.UserRepository;
import com.craft.biddingSystem.request.SignUpRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

// Class that implements the business logic of the users
@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, BCryptPasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public User createUser(SignUpRequest userIn){
        User user = new User();
        user.setUsername(userIn.getUsername());
        user.setEmail(userIn.getEmail());
        user.setPassword(passwordEncoder.encode(userIn.getPassword()));
        user.getRoles().add(Role.ROLE_USER);

        return userRepository.save(user);
    }

    public List<User> getFirst10Users(){
        Pageable topTen = PageRequest.of(0,10);
        return userRepository.findFirstWithPageable(topTen);
    }

    public User getUserById(Long id){
        return userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User cannot be found with id: " + id));
    }

    public List<User> getUsersByKeyword(String keyword){
        return userRepository.findAllByUsernameContains(keyword);
    }

    public User updateUser(User user){
        user.getRoles().add(Role.ROLE_USER);
        return userRepository.save(user);
    }

    public User getCurrentUser(Principal principal){
        return getUserByPrincipal(principal);
    }

    private User getUserByPrincipal(Principal principal) {
        String name = principal.getName();
        return userRepository.findUserByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with name " + name));
    }
}

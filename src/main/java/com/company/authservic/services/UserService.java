package com.company.authservic.services;

import com.company.authservic.models.User;
import com.company.authservic.models.dto.UserDTO;
import com.company.authservic.repositories.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

     private final UserRepository userRepository;
     private final ModelMapper modelMapper;
     private final PasswordEncoder passwordEncoder;


     private UserDTO convertToUserDTO(User person){
          return modelMapper.map(person, UserDTO.class);
     }
     private User convertToUser(UserDTO personDTO){
          return modelMapper.map(personDTO, User.class);
     }

     public UserDTO getProfile(String email) {
          User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));
          return convertToUserDTO(user);
     }

     public UserDTO updateUser(String email, UserDTO userDTO) {
          User oldUser = userRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

          oldUser.setFirstName(userDTO.getFirstName());
          oldUser.setLastName(userDTO.getLastName());
          oldUser.setPhone(userDTO.getPhone());
            oldUser.setUsername(userDTO.getUsername());
            userRepository.save(oldUser);
            return convertToUserDTO(oldUser);
     }

     @Transactional
     public void updatePassword(String email, String oldPassword, String newPassword) {
          User user = userRepository.findByEmail(email)
                  .orElseThrow(() -> new RuntimeException("User with email " + email + " not found"));

          if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
               throw new RuntimeException("Invalid password");
          }

          if (passwordEncoder.matches(newPassword, user.getPassword())) {
               throw new IllegalArgumentException("New password must be different from the old one");
          }

          // Оновлюємо та зберігаємо
          user.setPassword(passwordEncoder.encode(newPassword));
          userRepository.save(user);

          // Додатково: інвалідуємо refresh-token або оновлюємо дату зміни пароля
     }

/*
     public List<PersonDTO> getAllPerson() {
          return personRepository.findAll().stream().map(this::convertToPersonDTO)
                  .collect(Collectors.toList());
     }

     public void blockUser(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setAccountNonLocked(false);
          personRepository.save(person);
     }


     public void unblockUser(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setAccountNonLocked(true);
          personRepository.save(person);
     }

     public void enableUser(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setEnabled(true);
          personRepository.save(person);
     }

     public void disableUser(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setEnabled(false);
          personRepository.save(person);
     }

     public void setAdmin(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setRole("ROLE_ADMIN");
          personRepository.save(person);
     }

     public void setUser(String email) {
          Person person = personRepository.findByEmail(email)
                  .orElseThrow(() -> new PersonNotFoundException("User with email " + email + " not found"));
          person.setRole("ROLE_USER");
          personRepository.save(person);
     }*/
}

package com.hackathon.hackathon_management_system.service;

import com.hackathon.hackathon_management_system.entity.Payment;
import com.hackathon.hackathon_management_system.entity.Team;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.repository.PaymentRepository;
import com.hackathon.hackathon_management_system.repository.SubmissionRepository;
import com.hackathon.hackathon_management_system.repository.TeamRepository;
import com.hackathon.hackathon_management_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final PaymentRepository paymentRepository;
    private final SubmissionRepository submissionRepository;
    private final TeamRepository teamRepository;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder,
                       PaymentRepository paymentRepository, SubmissionRepository submissionRepository,
                       TeamRepository teamRepository) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.paymentRepository = paymentRepository;
        this.submissionRepository = submissionRepository;
        this.teamRepository = teamRepository;
    }

    public User register(String name, String email, String password, String role) {
        User user = new User(name, email, passwordEncoder.encode(password), role);
        return userRepository.save(user);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public User findById(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new RuntimeException("User not found"));
    }

    public List<User> findAll() { return userRepository.findAll(); }

    @Transactional
    public void deleteUser(Long id) {
        User user = findById(id);
        Team team = user.getTeam();

        paymentRepository.findByUserId(id).forEach(p -> paymentRepository.delete(p));

        if (team != null) {
            team.getMembers().remove(user);
            if (team.getLeader() != null && team.getLeader().getId().equals(id)) {
                team.setLeader(null);
            }
            teamRepository.save(team);
        }

        user.setTeam(null);
        userRepository.save(user);
        userRepository.deleteById(id);
    }

    public boolean checkPassword(String raw, String hash) {
        return passwordEncoder.matches(raw, hash);
    }
}

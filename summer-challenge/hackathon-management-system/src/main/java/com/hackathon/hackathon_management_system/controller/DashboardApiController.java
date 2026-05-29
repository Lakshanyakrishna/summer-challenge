package com.hackathon.hackathon_management_system.controller;

import com.hackathon.hackathon_management_system.entity.*;
import com.hackathon.hackathon_management_system.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api")
public class DashboardApiController {

    private final UserService userService;
    private final TeamService teamService;
    private final PaymentService paymentService;
    private final SubmissionService submissionService;
    private final HackathonService hackathonService;

    public DashboardApiController(UserService userService, TeamService teamService,
                                  PaymentService paymentService, SubmissionService submissionService,
                                  HackathonService hackathonService) {
        this.userService = userService;
        this.teamService = teamService;
        this.paymentService = paymentService;
        this.submissionService = submissionService;
        this.hackathonService = hackathonService;
    }

    @GetMapping("/auth/me")
    public ResponseEntity<?> me(Authentication auth) {
        if (auth == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Not found"));
        Map<String, Object> resp = new HashMap<>();
        resp.put("email", user.getEmail()); resp.put("name", user.getName());
        resp.put("role", user.getRole()); resp.put("hasPaid", user.isHasPaid());
        resp.put("teamId", user.getTeam() != null ? user.getTeam().getId() : null);
        return ResponseEntity.ok(resp);
    }

    @GetMapping("/hackathons")
    public ResponseEntity<?> hackathons() {
        hackathonService.updateStatuses();
        return ResponseEntity.ok(hackathonService.findAll().stream().map(h -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", h.getId()); m.put("title", h.getTitle());
            m.put("registrationFee", h.getRegistrationFee());
            return m;
        }).toList());
    }

    @PostMapping("/payments/create-order")
    public ResponseEntity<?> createOrder(Authentication auth, @RequestBody Map<String, Long> body) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (user.isHasPaid()) return ResponseEntity.badRequest().body(Map.of("error", "Already paid"));

        Hackathon hackathon = hackathonService.findById(body.get("hackathonId"));
        Payment payment = paymentService.createOrder(user, hackathon);
        return ResponseEntity.ok(Map.of("orderId", payment.getRazorpayOrderId(), "amount", payment.getAmount(), "paymentId", payment.getId()));
    }

    @PostMapping("/payments/verify")
    public ResponseEntity<?> verifyPayment(Authentication auth, @RequestBody Map<String, String> body) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        paymentService.verifyPayment(body.get("razorpay_order_id"), body.get("razorpay_payment_id"), body.get("razorpay_signature"));
        return ResponseEntity.ok(Map.of("success", true, "hasPaid", true));
    }

    @GetMapping("/teams/my")
    public ResponseEntity<?> myTeams(Authentication auth) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        List<Team> teams = teamService.findByLeader(user.getId());
        List<Map<String, Object>> result = teams.stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getId()); m.put("teamName", t.getTeamName());
            m.put("teamCode", t.getTeamCode()); m.put("stageStatus", t.getStageStatus());
            m.put("hackathonId", t.getHackathon().getId());
            m.put("hackathonTitle", t.getHackathon().getTitle());
            m.put("memberCount", t.getMembers().stream().filter(u -> u.getId() != null).count());
            return m;
        }).toList();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/teams/create")
    public ResponseEntity<?> createTeam(Authentication auth, @RequestBody Map<String, Object> body) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (!user.isHasPaid()) return ResponseEntity.badRequest().body(Map.of("error", "Payment required"));
        if (user.getTeam() != null) return ResponseEntity.badRequest().body(Map.of("error", "Already in a team"));

        Hackathon hackathon = hackathonService.findById(Long.valueOf(body.get("hackathonId").toString()));
        Team team = teamService.createTeam((String) body.get("teamName"), user, hackathon);
        return ResponseEntity.ok(Map.of("teamId", team.getId(), "teamCode", team.getTeamCode(), "teamName", team.getTeamName()));
    }

    @PostMapping("/teams/join")
    public ResponseEntity<?> joinTeam(Authentication auth, @RequestBody Map<String, String> body) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));
        if (!user.isHasPaid()) return ResponseEntity.badRequest().body(Map.of("error", "Payment required"));
        if (user.getTeam() != null) return ResponseEntity.badRequest().body(Map.of("error", "Already in a team"));

        Team team = teamService.joinTeam(body.get("teamCode"), user);
        return ResponseEntity.ok(Map.of("teamId", team.getId(), "teamName", team.getTeamName(), "teamCode", team.getTeamCode()));
    }

    @PostMapping("/submissions/submit")
    public ResponseEntity<?> submit(Authentication auth, @RequestBody Map<String, Object> body) {
        User user = userService.findByEmail(auth.getName()).orElse(null);
        if (user == null) return ResponseEntity.status(401).body(Map.of("error", "Unauthorized"));

        Long teamId = Long.valueOf(body.get("teamId").toString());
        Long hackathonId = Long.valueOf(body.get("hackathonId").toString());
        int stage = Integer.parseInt(body.get("stage").toString());
        Team team = teamService.findById(teamId);

        if (!team.getLeader().getId().equals(user.getId()))
            return ResponseEntity.badRequest().body(Map.of("error", "Only leader can submit"));
        if (team.getStageStatus() != stage)
            return ResponseEntity.badRequest().body(Map.of("error", "Stage not active"));
        if (team.getMembers().stream().filter(u -> u.getId() != null).count() < 3)
            return ResponseEntity.badRequest().body(Map.of("error", "Need 3 members"));

        submissionService.submit(team, team.getHackathon(), stage,
                (String) body.getOrDefault("videoUrl", ""),
                (String) body.getOrDefault("pptUrl", ""),
                (String) body.getOrDefault("githubUrl", ""),
                (String) body.getOrDefault("deployUrl", ""));
        return ResponseEntity.ok(Map.of("success", true));
    }
}

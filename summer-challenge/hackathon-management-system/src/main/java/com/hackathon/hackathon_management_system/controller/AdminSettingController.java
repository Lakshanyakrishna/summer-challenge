package com.hackathon.hackathon_management_system.controller;

import com.hackathon.hackathon_management_system.entity.*;
import com.hackathon.hackathon_management_system.service.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/admin")
public class AdminSettingController {

    private final SettingService settingService;
    private final StageConfigService stageConfigService;
    private final PrizeService prizeService;
    private final AnnouncementService announcementService;
    private final ProblemStatementService problemStatementService;
    private final TeamService teamService;
    private final HackathonService hackathonService;
    private final UserService userService;
    private final RuleService ruleService;

    public AdminSettingController(SettingService settingService, StageConfigService stageConfigService,
                                   PrizeService prizeService, AnnouncementService announcementService,
                                   ProblemStatementService problemStatementService,
                                   TeamService teamService, HackathonService hackathonService,
                                   UserService userService, RuleService ruleService) {
        this.settingService = settingService;
        this.stageConfigService = stageConfigService;
        this.prizeService = prizeService;
        this.announcementService = announcementService;
        this.problemStatementService = problemStatementService;
        this.teamService = teamService;
        this.hackathonService = hackathonService;
        this.userService = userService;
        this.ruleService = ruleService;
    }

    // ===== Settings =====
    @GetMapping("/settings")
    public ResponseEntity<?> allSettings() {
        return ResponseEntity.ok(settingService.findAll().stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId()); m.put("key", s.getSettingKey());
            m.put("value", s.getSettingValue()); m.put("category", s.getCategory());
            m.put("label", s.getLabel()); m.put("fieldType", s.getFieldType());
            m.put("options", s.getOptions());
            return m;
        }).toList());
    }

    @GetMapping("/settings/{category}")
    public ResponseEntity<?> settingsByCategory(@PathVariable String category) {
        return ResponseEntity.ok(settingService.findByCategory(category));
    }

    @PutMapping("/settings/{key}")
    public ResponseEntity<?> updateSetting(@PathVariable String key, @RequestBody Map<String, String> body) {
        settingService.set(key, body.get("value"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Teams =====
    @GetMapping("/teams")
    public ResponseEntity<?> allTeams() {
        return ResponseEntity.ok(teamService.findAll().stream().map(t -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", t.getId()); m.put("teamName", t.getTeamName());
            m.put("teamCode", t.getTeamCode()); m.put("stageStatus", t.getStageStatus());
            m.put("leaderName", t.getLeader() != null ? t.getLeader().getName() : "N/A");
            m.put("hackathonTitle", t.getHackathon().getTitle());
            m.put("memberCount", t.getMembers().stream().filter(u -> u.getId() != null).count());
            return m;
        }).toList());
    }

    @PostMapping("/teams/{id}/promote")
    public ResponseEntity<?> promoteTeam(@PathVariable Long id, @RequestBody Map<String, Integer> body) {
        teamService.promoteTeam(id, body.get("stage"));
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/teams/{id}/disqualify")
    public ResponseEntity<?> disqualifyTeam(@PathVariable Long id) {
        teamService.findById(id).setStageStatus(-1);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @PostMapping("/teams/{id}/restore")
    public ResponseEntity<?> restoreTeam(@PathVariable Long id) {
        teamService.findById(id).setStageStatus(1);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/teams/{id}")
    public ResponseEntity<?> deleteTeam(@PathVariable Long id) {
        teamService.promoteTeam(id, -1);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Hackathons =====
    @GetMapping("/hackathons/{id}")
    public ResponseEntity<?> getHackathon(@PathVariable Long id) {
        Hackathon h = hackathonService.findById(id);
        Map<String, Object> m = new HashMap<>();
        m.put("id", h.getId()); m.put("title", h.getTitle());
        m.put("description", h.getDescription());
        m.put("startDate", h.getStartDate() != null ? h.getStartDate().toString() : null);
        m.put("endDate", h.getEndDate() != null ? h.getEndDate().toString() : null);
        m.put("location", h.getLocation()); m.put("status", h.getStatus());
        m.put("registrationFee", h.getRegistrationFee());
        m.put("mode", h.getMode()); m.put("meetingLink", h.getMeetingLink());
        m.put("maxTeams", h.getMaxTeams()); m.put("minTeamSize", h.getMinTeamSize());
        m.put("maxTeamSize", h.getMaxTeamSize()); m.put("bannerImage", h.getBannerImage());
        m.put("registrationDeadline", h.getRegistrationDeadline() != null ? h.getRegistrationDeadline().toString() : null);
        m.put("stage1Deadline", h.getStage1Deadline() != null ? h.getStage1Deadline().toString() : null);
        m.put("stage2Deadline", h.getStage2Deadline() != null ? h.getStage2Deadline().toString() : null);
        m.put("stage3Deadline", h.getStage3Deadline() != null ? h.getStage3Deadline().toString() : null);
        m.put("resultDate", h.getResultDate() != null ? h.getResultDate().toString() : null);
        return ResponseEntity.ok(m);
    }

    @PostMapping("/hackathons")
    public ResponseEntity<?> createHackathon(@RequestBody Map<String, Object> body) {
        Hackathon h = new Hackathon();
        h.setTitle((String) body.get("title"));
        h.setDescription((String) body.get("description"));
        h.setLocation((String) body.getOrDefault("location", "Online"));
        h.setStatus("UPCOMING");
        h.setRegistrationFee(Double.parseDouble(body.getOrDefault("registrationFee", 249).toString()));
        if (body.get("startDate") != null) h.setStartDate(java.time.LocalDate.parse((String) body.get("startDate")));
        if (body.get("endDate") != null) h.setEndDate(java.time.LocalDate.parse((String) body.get("endDate")));
        return ResponseEntity.ok(Map.of("id", hackathonService.create(h).getId()));
    }

    @PutMapping("/hackathons/{id}")
    public ResponseEntity<?> updateHackathon(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Hackathon h = hackathonService.findById(id);
        if (body.containsKey("title")) h.setTitle((String) body.get("title"));
        if (body.containsKey("description")) h.setDescription((String) body.get("description"));
        if (body.containsKey("location")) h.setLocation((String) body.get("location"));
        if (body.containsKey("registrationFee")) h.setRegistrationFee(Double.parseDouble(body.get("registrationFee").toString()));
        if (body.containsKey("status")) h.setStatus((String) body.get("status"));
        if (body.containsKey("startDate")) h.setStartDate(java.time.LocalDate.parse((String) body.get("startDate")));
        if (body.containsKey("endDate")) h.setEndDate(java.time.LocalDate.parse((String) body.get("endDate")));
        if (body.containsKey("mode")) h.setMode((String) body.get("mode"));
        if (body.containsKey("meetingLink")) h.setMeetingLink((String) body.get("meetingLink"));
        if (body.containsKey("maxTeams")) h.setMaxTeams(body.get("maxTeams") != null ? Integer.parseInt(body.get("maxTeams").toString()) : null);
        if (body.containsKey("minTeamSize")) h.setMinTeamSize(body.get("minTeamSize") != null ? Integer.parseInt(body.get("minTeamSize").toString()) : null);
        if (body.containsKey("maxTeamSize")) h.setMaxTeamSize(body.get("maxTeamSize") != null ? Integer.parseInt(body.get("maxTeamSize").toString()) : null);
        if (body.containsKey("bannerImage")) h.setBannerImage((String) body.get("bannerImage"));
        if (body.containsKey("registrationDeadline")) h.setRegistrationDeadline(body.get("registrationDeadline") != null ? java.time.LocalDate.parse((String) body.get("registrationDeadline")) : null);
        if (body.containsKey("stage1Deadline")) h.setStage1Deadline(body.get("stage1Deadline") != null ? java.time.LocalDate.parse((String) body.get("stage1Deadline")) : null);
        if (body.containsKey("stage2Deadline")) h.setStage2Deadline(body.get("stage2Deadline") != null ? java.time.LocalDate.parse((String) body.get("stage2Deadline")) : null);
        if (body.containsKey("stage3Deadline")) h.setStage3Deadline(body.get("stage3Deadline") != null ? java.time.LocalDate.parse((String) body.get("stage3Deadline")) : null);
        if (body.containsKey("resultDate")) h.setResultDate(body.get("resultDate") != null ? java.time.LocalDate.parse((String) body.get("resultDate")) : null);
        hackathonService.create(h);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/hackathons/{id}")
    public ResponseEntity<?> deleteHackathon(@PathVariable Long id) {
        hackathonService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Stages =====
    @GetMapping("/stages")
    public ResponseEntity<?> allStages() { return ResponseEntity.ok(stageConfigService.findAll()); }

    @PostMapping("/stages")
    public ResponseEntity<?> createStage(@RequestBody Map<String, Object> body) {
        StageConfig sc = new StageConfig();
        sc.setStageNumber(Integer.parseInt(body.get("stageNumber").toString()));
        sc.setStageName((String) body.get("stageName"));
        sc.setRequiredFields((String) body.getOrDefault("requiredFields", ""));
        sc.setActive(true);
        return ResponseEntity.ok(Map.of("id", stageConfigService.save(sc).getId()));
    }

    @PutMapping("/stages/{id}")
    public ResponseEntity<?> updateStage(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        StageConfig sc = stageConfigService.findById(id);
        if (body.containsKey("stageName")) sc.setStageName((String) body.get("stageName"));
        if (body.containsKey("requiredFields")) sc.setRequiredFields((String) body.get("requiredFields"));
        if (body.containsKey("active")) sc.setActive(Boolean.parseBoolean(body.get("active").toString()));
        stageConfigService.save(sc);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/stages/{id}")
    public ResponseEntity<?> deleteStage(@PathVariable Long id) {
        stageConfigService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Prizes =====
    @GetMapping("/prizes")
    public ResponseEntity<?> allPrizes() { return ResponseEntity.ok(prizeService.findAll()); }

    @PostMapping("/prizes")
    public ResponseEntity<?> createPrize(@RequestBody Map<String, Object> body) {
        Prize p = new Prize();
        p.setTitle((String) body.get("title"));
        p.setAmount(Double.parseDouble(body.get("amount").toString()));
        p.setDescription((String) body.getOrDefault("description", ""));
        p.setType((String) body.getOrDefault("type", "cash"));
        p.setRank(Integer.parseInt(body.getOrDefault("rank", 0).toString()));
        return ResponseEntity.ok(Map.of("id", prizeService.save(p).getId()));
    }

    @PutMapping("/prizes/{id}")
    public ResponseEntity<?> updatePrize(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Prize p = prizeService.findAll().stream().filter(x -> x.getId().equals(id)).findFirst().orElse(null);
        if (p == null) return ResponseEntity.badRequest().body(Map.of("error", "Not found"));
        if (body.containsKey("title")) p.setTitle((String) body.get("title"));
        if (body.containsKey("amount")) p.setAmount(Double.parseDouble(body.get("amount").toString()));
        if (body.containsKey("description")) p.setDescription((String) body.get("description"));
        if (body.containsKey("type")) p.setType((String) body.get("type"));
        if (body.containsKey("rank")) p.setRank(Integer.parseInt(body.get("rank").toString()));
        prizeService.save(p);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/prizes/{id}")
    public ResponseEntity<?> deletePrize(@PathVariable Long id) { prizeService.delete(id); return ResponseEntity.ok(Map.of("success", true)); }

    // ===== Announcements =====
    @GetMapping("/announcements")
    public ResponseEntity<?> allAnnouncements() { return ResponseEntity.ok(announcementService.findAll()); }

    @PostMapping("/announcements")
    public ResponseEntity<?> createAnnouncement(@RequestBody Map<String, Object> body) {
        Announcement a = new Announcement();
        a.setTitle((String) body.get("title"));
        a.setMessage((String) body.get("message"));
        a.setType((String) body.getOrDefault("type", "info"));
        if (body.containsKey("scheduledAt")) a.setScheduledAt(java.time.LocalDateTime.parse((String) body.get("scheduledAt")));
        announcementService.save(a);
        return ResponseEntity.ok(Map.of("id", a.getId()));
    }

    @DeleteMapping("/announcements/{id}")
    public ResponseEntity<?> deleteAnnouncement(@PathVariable Long id) { announcementService.delete(id); return ResponseEntity.ok(Map.of("success", true)); }

    // ===== Stats =====
    @GetMapping("/stats")
    public ResponseEntity<?> stats() {
        return ResponseEntity.ok(Map.of(
            "hackathons", hackathonService.findAll().size(),
            "teams", teamService.findAll().size(),
            "users", userService.findAll().size()
        ));
    }

    // ===== Users =====
    @GetMapping("/users")
    public ResponseEntity<?> allUsers() {
        return ResponseEntity.ok(userService.findAll().stream().map(u -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", u.getId()); m.put("name", u.getName());
            m.put("email", u.getEmail()); m.put("role", u.getRole());
            m.put("hasPaid", u.isHasPaid());
            m.put("teamId", u.getTeam() != null ? u.getTeam().getId() : null);
            return m;
        }).toList());
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        if (id == 1) return ResponseEntity.badRequest().body(Map.of("error", "Cannot delete admin"));
        userService.deleteUser(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Problem Statements =====
    @GetMapping("/problems")
    public ResponseEntity<?> allProblems() { return ResponseEntity.ok(problemStatementService.findAll()); }

    @GetMapping("/problems/by-hackathon/{hackathonId}")
    public ResponseEntity<?> problemsByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(problemStatementService.findByHackathon(hackathonId));
    }

    @PostMapping("/problems")
    public ResponseEntity<?> createProblem(@RequestBody Map<String, Object> body) {
        ProblemStatement ps = new ProblemStatement();
        ps.setHackathonId(Long.valueOf(body.get("hackathonId").toString()));
        ps.setTitle((String) body.get("title"));
        ps.setDescription((String) body.getOrDefault("description", ""));
        ps.setTechStack((String) body.getOrDefault("techStack", ""));
        ps.setDifficulty((String) body.getOrDefault("difficulty", "medium"));
        ps.setIcon((String) body.getOrDefault("icon", "bi-journal-code"));
        return ResponseEntity.ok(Map.of("id", problemStatementService.save(ps).getId()));
    }

    @PutMapping("/problems/{id}")
    public ResponseEntity<?> updateProblem(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        ProblemStatement ps = problemStatementService.findAll().stream()
                .filter(p -> p.getId().equals(id)).findFirst().orElse(null);
        if (ps == null) return ResponseEntity.badRequest().body(Map.of("error", "Not found"));
        if (body.containsKey("title")) ps.setTitle((String) body.get("title"));
        if (body.containsKey("description")) ps.setDescription((String) body.get("description"));
        if (body.containsKey("techStack")) ps.setTechStack((String) body.get("techStack"));
        if (body.containsKey("difficulty")) ps.setDifficulty((String) body.get("difficulty"));
        if (body.containsKey("icon")) ps.setIcon((String) body.get("icon"));
        problemStatementService.save(ps);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/problems/{id}")
    public ResponseEntity<?> deleteProblem(@PathVariable Long id) {
        problemStatementService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }

    // ===== Rules =====
    @GetMapping("/rules/{hackathonId}")
    public ResponseEntity<?> rulesByHackathon(@PathVariable Long hackathonId) {
        return ResponseEntity.ok(ruleService.findByHackathon(hackathonId));
    }

    @PostMapping("/rules")
    public ResponseEntity<?> createRule(@RequestBody Map<String, Object> body) {
        Rule r = new Rule();
        r.setHackathonId(Long.valueOf(body.get("hackathonId").toString()));
        r.setContent((String) body.get("content"));
        if (body.containsKey("sortOrder")) r.setSortOrder(Integer.valueOf(body.get("sortOrder").toString()));
        return ResponseEntity.ok(Map.of("id", ruleService.save(r).getId()));
    }

    @PutMapping("/rules/{id}")
    public ResponseEntity<?> updateRule(@PathVariable Long id, @RequestBody Map<String, Object> body) {
        Rule r = ruleService.findById(id);
        if (body.containsKey("content")) r.setContent((String) body.get("content"));
        if (body.containsKey("sortOrder")) r.setSortOrder(Integer.valueOf(body.get("sortOrder").toString()));
        ruleService.save(r);
        return ResponseEntity.ok(Map.of("success", true));
    }

    @DeleteMapping("/rules/{id}")
    public ResponseEntity<?> deleteRule(@PathVariable Long id) {
        ruleService.delete(id);
        return ResponseEntity.ok(Map.of("success", true));
    }
}

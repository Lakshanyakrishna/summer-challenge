package com.hackathon.hackathon_management_system.controller;

import com.hackathon.hackathon_management_system.entity.Hackathon;
import com.hackathon.hackathon_management_system.entity.User;
import com.hackathon.hackathon_management_system.service.HackathonService;
import com.hackathon.hackathon_management_system.service.PaymentService;
import com.hackathon.hackathon_management_system.service.ProblemStatementService;
import com.hackathon.hackathon_management_system.service.PrizeService;
import com.hackathon.hackathon_management_system.service.RuleService;
import com.hackathon.hackathon_management_system.service.SettingService;
import com.hackathon.hackathon_management_system.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Controller
public class PageController {

    private final HackathonService hackathonService;
    private final UserService userService;
    private final PaymentService paymentService;
    private final ProblemStatementService problemStatementService;
    private final PrizeService prizeService;
    private final SettingService settingService;
    private final RuleService ruleService;

    public PageController(HackathonService hackathonService, UserService userService,
                          PaymentService paymentService, ProblemStatementService problemStatementService,
                          PrizeService prizeService, SettingService settingService,
                          RuleService ruleService) {
        this.hackathonService = hackathonService;
        this.userService = userService;
        this.paymentService = paymentService;
        this.problemStatementService = problemStatementService;
        this.prizeService = prizeService;
        this.settingService = settingService;
        this.ruleService = ruleService;
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/hackathons";
    }

    @GetMapping("/hackathons")
    public String hackathons(Model model, Authentication auth) {
        hackathonService.updateStatuses();
        model.addAttribute("hackathons", hackathonService.findAll());
        if (auth != null && auth.isAuthenticated()) {
            User user = userService.findByEmail(auth.getName()).orElse(null);
            if (user != null && !"ADMIN".equals(user.getRole())) {
                Map<Long, Boolean> registered = new HashMap<>();
                for (Hackathon h : hackathonService.findAll()) {
                    registered.put(h.getId(), paymentService.hasPaid(user.getId(), h.getId()));
                }
                model.addAttribute("registeredMap", registered);
            }
        }
        return "hackathons";
    }

    @GetMapping("/hackathons/{id}")
    public String hackathonDetail(@PathVariable Long id, Model model) {
        model.addAttribute("hackathon", hackathonService.findById(id));
        model.addAttribute("problems", problemStatementService.findByHackathon(id));
        model.addAttribute("prizes", prizeService.findAll());
        model.addAttribute("rules", ruleService.findByHackathon(id));
        model.addAttribute("teamMinMembers", settingService.get("team.minMembers", "3"));
        model.addAttribute("teamMaxMembers", settingService.get("team.maxMembers", "5"));
        return "hackathon-detail";
    }

    @GetMapping("/admin/hackathons/{id}")
    public String adminHackathonDetail(@PathVariable Long id, Model model) {
        model.addAttribute("hackathon", hackathonService.findById(id));
        model.addAttribute("problems", problemStatementService.findByHackathon(id));
        model.addAttribute("prizes", prizeService.findAll());
        model.addAttribute("rules", ruleService.findByHackathon(id));
        model.addAttribute("teamMinMembers", settingService.get("team.minMembers", "3"));
        model.addAttribute("teamMaxMembers", settingService.get("team.maxMembers", "5"));
        return "hackathon-detail";
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        return "dashboard";
    }

    @GetMapping("/team")
    public String teamPage(Model model) {
        return "team";
    }

    @GetMapping("/submission")
    public String submissionPage(Model model) {
        return "submission";
    }

    @GetMapping("/payment")
    public String paymentPage(Model model, @RequestParam(required = false) Long hackathonId) {
        model.addAttribute("hackathonId", hackathonId);
        if (hackathonId != null) {
            model.addAttribute("hackathon", hackathonService.findById(hackathonId));
        }
        return "payment";
    }

    @GetMapping("/register")
    public String registerPage() {
        return "register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name, @RequestParam String email,
                           @RequestParam String password, Model model) {
        if (userService.findByEmail(email).isPresent()) {
            return "redirect:/register?error";
        }
        userService.register(name, email, password, "PARTICIPANT");
        return "redirect:/login?registered";
    }

    @GetMapping("/admin/teams")
    public String adminTeams(Model model) {
        return "admin/teams";
    }

    @GetMapping("/admin/hackathons")
    public String adminHackathons(Model model) {
        model.addAttribute("hackathons", hackathonService.findAll());
        return "admin/hackathons";
    }

    @GetMapping("/admin/settings")
    public String adminSettings() {
        return "admin/settings";
    }
}

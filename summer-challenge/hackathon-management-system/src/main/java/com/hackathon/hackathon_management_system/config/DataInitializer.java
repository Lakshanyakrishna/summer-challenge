package com.hackathon.hackathon_management_system.config;

import com.hackathon.hackathon_management_system.entity.*;
import com.hackathon.hackathon_management_system.repository.*;
import com.hackathon.hackathon_management_system.service.SettingService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final HackathonRepository hackathonRepository;
    private final PasswordEncoder passwordEncoder;
    private final SettingService settingService;
    private final StageConfigRepository stageConfigRepository;
    private final PrizeRepository prizeRepository;
    private final ProblemStatementRepository problemStatementRepository;

    public DataInitializer(UserRepository userRepository, HackathonRepository hackathonRepository,
                           PasswordEncoder passwordEncoder, SettingService settingService,
                           StageConfigRepository stageConfigRepository, PrizeRepository prizeRepository,
                           ProblemStatementRepository problemStatementRepository) {
        this.userRepository = userRepository;
        this.hackathonRepository = hackathonRepository;
        this.passwordEncoder = passwordEncoder;
        this.settingService = settingService;
        this.stageConfigRepository = stageConfigRepository;
        this.prizeRepository = prizeRepository;
        this.problemStatementRepository = problemStatementRepository;
    }

    @Override
    public void run(String... args) {
        if (!userRepository.existsByEmail("admin@hackathon.com")) {
            User admin = new User("Administrator", "admin@hackathon.com",
                    passwordEncoder.encode("Admin@123"), "ADMIN");
            userRepository.save(admin);
        }

        if (hackathonRepository.count() == 0) {
            Hackathon h = new Hackathon();
            h.setTitle("Summer Hackathon 2026");
            h.setDescription("Build something amazing this summer. Open to all participants.");
            h.setStartDate(LocalDate.now());
            h.setEndDate(LocalDate.now().plusDays(30));
            h.setLocation("Online");
            h.setStatus("UPCOMING");
            h.setRegistrationFee(249);
            hackathonRepository.save(h);
        }

        initSettings();
        initStages();
        initPrizes();
        initProblems();
    }

    private void initProblems() {
        if (problemStatementRepository.count() > 0) return;
        Hackathon h = hackathonRepository.findAll().get(0);
        ProblemStatement p1 = new ProblemStatement();
        p1.setHackathonId(h.getId()); p1.setTitle("AI-Powered Assistant");
        p1.setDescription("Build an intelligent assistant that helps users automate daily tasks using natural language processing");
        p1.setDifficulty("medium"); p1.setTechStack("Python, LLM, React"); p1.setIcon("bi-robot");
        problemStatementRepository.save(p1);

        ProblemStatement p2 = new ProblemStatement();
        p2.setHackathonId(h.getId()); p2.setTitle("Decentralized Marketplace");
        p2.setDescription("Create a blockchain-based marketplace for digital assets with smart contract integration");
        p2.setDifficulty("hard"); p2.setTechStack("Solidity, Web3, React"); p2.setIcon("bi-globe");
        problemStatementRepository.save(p2);

        ProblemStatement p3 = new ProblemStatement();
        p3.setHackathonId(h.getId()); p3.setTitle("Health & Fitness Tracker");
        p3.setDescription("Develop a cross-platform mobile app for tracking health metrics and providing personalized fitness plans");
        p3.setDifficulty("easy"); p3.setTechStack("Flutter, Firebase, ML Kit"); p3.setIcon("bi-phone");
        problemStatementRepository.save(p3);
    }

    private void initStages() {
        if (stageConfigRepository.count() > 0) return;
        stageConfigRepository.save(new StageConfig(1, "Idea Submission", "description,videoUrl"));
        stageConfigRepository.save(new StageConfig(2, "Prototype Development", "githubUrl,videoUrl"));
        stageConfigRepository.save(new StageConfig(3, "Final Pitch", "pptUrl,demoUrl,videoUrl"));
    }

    private void initPrizes() {
        if (prizeRepository.count() > 0) return;
        Prize p1 = new Prize(); p1.setTitle("Grand Prize"); p1.setAmount(100000); p1.setRank(1); p1.setType("cash"); p1.setDescription("Winner takes all"); prizeRepository.save(p1);
        Prize p2 = new Prize(); p2.setTitle("Runner Up"); p2.setAmount(50000); p2.setRank(2); p2.setType("cash"); p2.setDescription("Second place"); prizeRepository.save(p2);
        Prize p3 = new Prize(); p3.setTitle("Third Place"); p3.setAmount(25000); p3.setRank(3); p3.setType("cash"); p3.setDescription("Third place"); prizeRepository.save(p3);
    }

    private void initSettings() {
        settingService.initDefault("team.minMembers", "3", "team", "Minimum Team Members", "number");
        settingService.initDefault("team.maxMembers", "5", "team", "Maximum Team Members", "number");
        settingService.initDefault("registration.open", "true", "registration", "Registration Open", "boolean");
        settingService.initDefault("registration.autoApprove", "true", "registration", "Auto Approve Registration", "boolean");
        settingService.initDefault("payment.amount", "249", "payment", "Registration Fee (INR)", "number");
        settingService.initDefault("payment.upiId", "admin@hackathon", "payment", "UPI ID", "text");
        settingService.initDefault("submission.maxSize", "25", "submission", "Max Submission Size (MB)", "number");
        settingService.initDefault("submission.deadline", "", "submission", "Submission Deadline", "datetime");
        settingService.initDefault("stage.minVotes", "3", "stage", "Minimum Votes for Promotion", "number");
        settingService.initDefault("platform.name", "Summer Hackathon 2026", "platform", "Platform Name", "text");
        settingService.initDefault("platform.contactEmail", "support@hackathon.com", "platform", "Contact Email", "email");
    }
}

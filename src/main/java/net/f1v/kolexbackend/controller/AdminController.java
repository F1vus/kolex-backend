package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.admin.TravelFormDto;
import net.f1v.kolexbackend.service.AdminService;
import net.f1v.kolexbackend.repository.StationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;
    private final StationRepository stationRepository;

    @GetMapping("/dashboard")
    public String dashboard() {
        return "admin/dashboard";
    }

    @GetMapping("/trains")
    public String trains(Model model) {
        model.addAttribute("travels", adminService.getAllTravels());
        model.addAttribute("stations", stationRepository.findAll());
        model.addAttribute("travelForm", new TravelFormDto());
        return "admin/trains";
    }

    @GetMapping("/trains/{id}/edit")
    public String editTrain(@PathVariable Long id, Model model) {
        model.addAttribute("travels", adminService.getAllTravels());
        model.addAttribute("stations", stationRepository.findAll());
        model.addAttribute("travelForm", adminService.getTravelForEdit(id));
        return "admin/trains";
    }

    @PostMapping("/trains/save")
    public String saveTrain(@ModelAttribute TravelFormDto form,
                            RedirectAttributes ra) {
        try {
            adminService.saveTravel(form);
            ra.addFlashAttribute("success", "Połączenie zapisane pomyślnie");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/trains";
    }

    @PostMapping("/trains/{id}/delete")
    public String deleteTrain(@PathVariable Long id, RedirectAttributes ra) {
        try {
            adminService.deleteTravel(id);
            ra.addFlashAttribute("success", "Połączenie usunięte");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/trains";
    }

    @GetMapping("/users")
    public String users(@RequestParam(required = false) String search,
                        Model model) {
        model.addAttribute("users", adminService.searchUsers(search));
        model.addAttribute("search", search);
        return "admin/users";
    }

    @PostMapping("/users/{id}/toggle")
    public String toggleUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            adminService.toggleUserEnabled(id);
            ra.addFlashAttribute("success", "Status konta zaktualizowany");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }

    @PostMapping("/users/{id}/delete")
    public String deleteUser(@PathVariable Long id, RedirectAttributes ra) {
        try {
            adminService.deleteUser(id);
            ra.addFlashAttribute("success", "Konto usunięte");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/users";
    }
}



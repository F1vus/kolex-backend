package net.f1v.kolexbackend.controller;

import lombok.RequiredArgsConstructor;
import net.f1v.kolexbackend.dto.StationResponse;
import net.f1v.kolexbackend.dto.admin.TravelFormDto;
import net.f1v.kolexbackend.entity.Station;
import net.f1v.kolexbackend.entity.states.UserRole;
import net.f1v.kolexbackend.service.AdminService;
import net.f1v.kolexbackend.repository.StationRepository;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;

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
        TravelFormDto form = new TravelFormDto();
        form.setStops(new ArrayList<>());
        form.setSeatCount(80);
        populateTrainsPage(model, form);
        return "admin/trains";
    }

    @GetMapping("/trains/{id}/edit")
    public String editTrain(@PathVariable Long id, Model model) {
        populateTrainsPage(model, adminService.getTravelForEdit(id));
        return "admin/trains";
    }

    @PostMapping("/trains/save")
    public String saveTrain(@ModelAttribute TravelFormDto form, RedirectAttributes ra, Model model) {
        if (form.getStops() == null) {
            form.setStops(new ArrayList<>());
        }
        try {
            adminService.saveTravel(form);
            ra.addFlashAttribute("success", "Połączenie zapisane!");
            return "redirect:/admin/trains";
        } catch (Exception e) {
            model.addAttribute("error", "Błąd: " + e.getMessage());
            populateTrainsPage(model, form);
            return "admin/trains";
        }
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
    public String users(@RequestParam(required = false) String search, Model model) {
        model.addAttribute("users", adminService.searchUsers(search));
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("search", search != null ? search : "");
        return "admin/users";
    }

    @GetMapping("/users/{id}/edit")
    public String editUser(@PathVariable Long id, Model model) {
        model.addAttribute("userEdit", adminService.getUserById(id));
        model.addAttribute("users", adminService.searchUsers(null));
        model.addAttribute("roles", UserRole.values());
        model.addAttribute("search", "");
        return "admin/users";
    }

    @PostMapping("/users/update")
    public String updateUser(@RequestParam Long id, @RequestParam String email,
                             @RequestParam UserRole role, RedirectAttributes ra) {
        try {
            adminService.updateUser(id, email, role);
            ra.addFlashAttribute("success", "Użytkownik zaktualizowany!");
        } catch (Exception e) {
            ra.addFlashAttribute("error", "Błąd: " + e.getMessage());
        }
        return "redirect:/admin/users";
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

    private void populateTrainsPage(Model model, TravelFormDto travelForm) {
        List<Station> stations = stationRepository.findAll();
        model.addAttribute("travels", adminService.getAllTravels());
        model.addAttribute("stations", stations);
        model.addAttribute("stationOptions", toStationOptions(stations));
        model.addAttribute("travelForm", travelForm);
    }

    private static List<StationResponse> toStationOptions(List<Station> stations) {
        return stations.stream()
                .map(s -> StationResponse.builder()
                        .id(s.getId())
                        .name(s.getName())
                        .city(s.getCity())
                        .build())
                .toList();
    }
}

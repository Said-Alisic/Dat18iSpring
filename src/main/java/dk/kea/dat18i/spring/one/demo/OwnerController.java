package dk.kea.dat18i.spring.one.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;


import java.util.ArrayList;
import java.util.List;

@Controller
public class OwnerController {

    @Autowired // Handle this field and create the object that needs to be created
    private JdbcTemplate jdbc;

    @Autowired
    private OwnerRepository ownerRepo;

    private static List<Owner> ownerList = new ArrayList<>();

    @GetMapping("/owners")
    public String owners(Model model) {
        ownerList = ownerRepo.findAllOwners();
        model.addAttribute("owners", ownerList);
        model.addAttribute("newOwner", new Owner());

        return "owners-page";
    }

    @GetMapping("/owners/edit/{index}")
    public String editOwner(@PathVariable int index, Model model) {
        Owner editOwner = ownerList.get(index);
        model.addAttribute("index", index);
        model.addAttribute("editOwner", editOwner);
        return "editowner-page";
    }

    // Edit owner from MySQL database and redirect back to /owners
    @PostMapping(value = "/owners/edit/{index}")
    public String handleEditOwner(@PathVariable int index, @ModelAttribute Owner owner) {
        ownerRepo.editOwner(owner, ownerList.get(index).getId());
        return "redirect:/owners";
    }

    // Delete owner from MySQL database  and redirect back to /owners
    @GetMapping(value = "/owners/delete/{index}")
    public String handleDeleteOwner(@PathVariable int index) {
        ownerRepo.deleteOwner(ownerList.get(index));
        return "redirect:/owners";
    }

    // Add owner to MySQL database
    // Can also use @PostMapping which is short for the @RequestMapping with RequestMethod.POST
    @PostMapping(value = "/owners")
    public String handleAddOwner(@ModelAttribute Owner owner) {
        ownerRepo.saveOwner(owner);

        return "redirect:/owners";
    }

    @PostMapping(value = "/saveowner")
    public String saveOwner(@ModelAttribute Owner owner) {
        owner = ownerRepo.insertOwner(owner);
        return "redirect:/owners";
    }







}

package paf.day27.d27workshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import paf.day27.d27workshop.model.Review;

@Controller
@RequestMapping
public class ReviewController {

    @GetMapping("/")
    public String writeReview(Model model){
        Review review = new Review();
        model.addAttribute("review", review);
        
        return "write-review";
    }


    
}

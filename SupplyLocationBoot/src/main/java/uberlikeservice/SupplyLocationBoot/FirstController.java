package uberlikeservice.SupplyLocationBoot;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("/supply_location")
public class FirstController {

   @GetMapping("/home")
   public String greeting() {
      return "Hello world";
   }
}

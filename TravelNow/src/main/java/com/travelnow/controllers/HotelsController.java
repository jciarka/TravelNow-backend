package com.travelnow.controllers;


import com.travelnow.models.BasicHotelInfo;
import com.travelnow.models.FilterInfo;
import com.travelnow.models.FullHotelInfo;
import com.travelnow.models.Room;
import com.travelnow.services.HotelsService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


import org.springframework.ui.ModelMap;

@Controller
public class HotelsController {

    HotelsService hotelsService;

    @Autowired
    public HotelsController(HotelsService hotelsService) {
        super();
        this.hotelsService = hotelsService;
    }

    
    @RequestMapping("/")
    public String home(ModelMap model) {
        model.addAttribute("loggedin", isAuthenticatated());
        model.addAttribute("cities", hotelsService.listCities());
       
        return "hotels/home";
    }

    @RequestMapping("/hotels/list")
    public String list(FilterInfo filterInfo, ModelMap model) {
        model.addAttribute("loggedin", isAuthenticatated());
        model.addAttribute("cities", hotelsService.listCities());
        model.addAttribute("filterInfo", filterInfo);

        if(filterInfo == null){
            filterInfo = new FilterInfo();
        }
        List<BasicHotelInfo> hotels = hotelsService.getHotelsByCityName(filterInfo.getCity());
        model.addAttribute("hotels", hotels);
       
        return "hotels/list";
    }

    @RequestMapping("/hotels/hotelDetails/{id}")
    public String hotelDetails(@PathVariable("id") int id, 
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateFrom, 
                               @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate dateTo,  
                               String returnurl, ModelMap model) {
        model.addAttribute("dateFrom", dateFrom);
        model.addAttribute("dateTo", dateTo);        
        model.addAttribute("loggedin", isAuthenticatated());
        model.addAttribute("returnurl", returnurl);

        FullHotelInfo hotel = hotelsService.getHotelById(id);
        model.addAttribute("hotel", hotel);

        if(dateFrom != null && dateTo != null) {
            model.addAttribute("freeRooms", hotelsService.getFreeRoomsByHotelsIdAtDates(id, dateFrom, dateTo));
        } else {
            model.addAttribute("freeRooms", new ArrayList<Room>());
        }

        return "hotels/hotel_details";
    }

    /*
    private String extractRelativeURLFromFullURL(String fullURL)
    {
        String relativeURL = fullURL.split("WEB-INF/view/")[1];
        return relativeURL.substring(0, relativeURL.length()-4);
    }
    */

    private boolean isAuthenticatated() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if(auth != null && !auth.getPrincipal().equals("anonymousUser")) {
            return true;
        }
        return false;
    }
}

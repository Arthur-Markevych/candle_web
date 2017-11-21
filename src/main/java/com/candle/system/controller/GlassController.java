package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.Glass;
import com.candle.system.repository.GlassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/glass")
public class GlassController {

    private String title = Constants.GLASS_TITLE;

    @Autowired
    GlassRepository repository;

    @RequestMapping(value = "/")
    public ModelAndView getAll(){
        Iterable<Glass> list = repository.findAll();
        Map<String, Object> model = new HashMap<>();
        if (list != null) model.put("list", list);
        model.put("title", title);
        return new ModelAndView("glass", model);
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(@RequestParam String name, Long weight, BigDecimal price){
        Glass glass = new Glass(name.trim(), price, weight);
        ModelAndView mv;
        try {
            repository.save(glass);
            mv = getAll();
            mv.addObject("report", Constants.SAVED);
            mv.addObject("message", "Единица " + "<span class=\"saved_product_color\">" + glass.getName() + "</span>" + " сохранена");
            return mv;
        } catch (Exception e) {
            mv = getAll();
            mv.addObject("report", Constants.ERROR);
            mv.addObject("message", "error: " + e.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = "/delete")
    public ModelAndView delete(@RequestParam Long id){
        try {
            repository.delete(id);
            return getAll();
        } catch (Exception e) {
            return getAll().addObject("report", Constants.ERROR)
                    .addObject("message", "Ошибка удаления!");
        }
    }
}

package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.Box;
import com.candle.system.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping(path = "/box")
public class BoxController {

    private String title = Constants.BOX_TITLE;

    private String saved = "Сохранено";

    @Autowired
    BoxRepository repository;

    @RequestMapping(value = "/")
    public ModelAndView getAll(){
        Iterable<Box> list = repository.findAll();
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        if (list != null) model.put("list", list);
        return new ModelAndView("box", model);
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public ModelAndView add(@RequestParam String name, BigDecimal price){
        ModelAndView mv;
        Box box = new Box(name.trim(), price);
        try {
            repository.save(box);
            mv = getAll();
            mv.addObject("report", Constants.SAVED);
            mv.addObject("message", "Упаковка " + "<span class=\"saved_product_color\">" + box.getName() + "</span>" + " сохранена");
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

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateBox(@RequestParam Long id, String name, BigDecimal price){
        ModelAndView mv;
        Box box = new Box();
        box.setId(id);
        box.setName(name);
        box.setPrice(price);

        try {
            repository.save(box);
            mv = getAll();
            mv.addObject("message", "упаковка " + "<span class=\"saved_product_color\">" + box.getName() + "</span>" + " обновлена");
            mv.addObject("report", Constants.SAVED);
            return mv;
        } catch (Exception e) {
            mv = getAll();
            mv.addObject("report", Constants.ERROR);
            mv.addObject("message", "Ошибка обновления: " + e.getMessage());
            return mv;
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.GET)
    public ModelAndView updateBox(@RequestParam Long id){
        ModelAndView mv = new ModelAndView("update/box");
        if (id != null){
            Box box = repository.findOne(id);
            mv.addObject("box", box);
            mv.addObject("title", "Изменение этикетки");
            return mv;
        }

        return getAll();
    }
}

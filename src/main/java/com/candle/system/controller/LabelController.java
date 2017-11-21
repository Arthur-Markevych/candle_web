package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.Label;
import com.candle.system.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@RestController
@RequestMapping(path = "/label")
public class LabelController {

    @Autowired
    LabelRepository repository;

    private String title = Constants.LABEL_TITLE;

    @RequestMapping(value = "/")
    public ModelAndView getAll(){
        ModelAndView mv = new ModelAndView("label");
        Iterable<Label> list = repository.findAll();
        if (list != null) mv.addObject("list", list);

        mv.addObject("title", title);
        return mv;
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(@RequestParam String name, BigDecimal price){
        Label label = new Label();
        label.setName(name.trim());
        label.setPrice(price);

        ModelAndView mv;

        try {
            repository.save(label);
            mv = getAll();
            mv.addObject("report", Constants.SAVED);
            mv.addObject("message", "Этикетка " + "<span class=\"saved_product_color\">" + label.getName() + "</span>" + " сохранена");

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

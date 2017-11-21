package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.Costs;
import com.candle.system.repository.CostsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

@RestController
@RequestMapping(value = "/costs")
public class CostsController {

    @Autowired
    CostsRepository repository;

    @RequestMapping("/")
    public ModelAndView getAll(){
        ModelAndView mv = new ModelAndView("costs");
        Iterable<Costs> list = repository.findAll();
        mv.addObject("title", Constants.COSTS_TITLE);
        mv.addObject("list", list);
        return mv;
    }

    @RequestMapping("/add")
    public ModelAndView add(@RequestParam String description, BigDecimal cash){
        Costs costs = new Costs();
        costs.setCash(cash);
        costs.setDescription(description.trim());
        costs.setCreationDate(Calendar.getInstance());

        try {
            repository.save(costs);
            return getAll().addObject("report", Constants.SAVED)
                    .addObject("message", "Сохранено!");
        } catch (Exception e) {
            return getAll().addObject("report", Constants.ERROR)
                    .addObject("message", "Произошла ошибка!");
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

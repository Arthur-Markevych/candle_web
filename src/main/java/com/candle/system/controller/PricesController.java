package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.Prices;
import com.candle.system.repository.PricesRepository;
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
@RequestMapping(value = "/prices")
public class PricesController {

    @Autowired
    PricesRepository repository;

    private String title = Constants.PRICES_TITLE;

    @RequestMapping(value = "/")
    public ModelAndView getAll(){
        Map<String, Object> model = new HashMap<>();
        Iterable<Prices> list = repository.findAll();
        model.put("title", title);
        if (list != null) model.put("list", list);
        return new ModelAndView("prices", model);
    }

    @RequestMapping(value = "/add")
    public ModelAndView add(@RequestParam String name, BigDecimal wax, BigDecimal wick, BigDecimal diy, BigDecimal aroma){
        Prices prices = new Prices(name.trim(), wax, wick, diy, aroma);
        ModelAndView mv;
        try {
            repository.save(prices);
            mv = getAll();
            mv.addObject("report", Constants.SAVED);
            mv.addObject("message", "Каталог цен " + "<span class=\"saved_product_color\">" + prices.getName() + "</span>" + " сохранен");
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
                    .addObject("message", "Ошибка: нельзя удалить расчетные цены для существующих единиц!");
        }
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ModelAndView updateBox(@RequestParam Long id, String name, BigDecimal wax, BigDecimal wick, BigDecimal diy, BigDecimal aroma){
        ModelAndView mv;
        Prices prices = new Prices();
        prices.setId(id);
        prices.setWax(wax);
        prices.setName(name);
        prices.setWick(wick);
        prices.setDiy(diy);
        prices.setAroma(aroma);
        try {
            repository.save(prices);
            mv = getAll();
            mv.addObject("message", "цены " + "<span class=\"saved_product_color\">" + prices.getName() + "</span>" + " обновлены");
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
        ModelAndView mv = new ModelAndView("update/prices");
        if (id != null){
            Prices prices = repository.findOne(id);
            mv.addObject("prices", prices);
            mv.addObject("title", "Изменение цен");
            return mv;
        }

        return getAll();
    }
}

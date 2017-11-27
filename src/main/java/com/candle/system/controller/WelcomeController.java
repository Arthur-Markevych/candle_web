package com.candle.system.controller;

import com.candle.system.configurations.Amount;
import com.candle.system.configurations.Constants;
import com.candle.system.model.*;
import com.candle.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
public class WelcomeController {

    @Autowired
    private GlassRepository glassRepository;

    @Autowired
    private CandleRepository candleRepository;

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private LabelRepository labelRepository;

    @Autowired
    private PricesRepository pricesRepository;




    private String title = Constants.INDEX_TITLE;


    @RequestMapping("/")
    public  ModelAndView getAll(){
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        Iterable<Candle> list = candleRepository.findAll();
        if (list != null) {
            try {
                for (Candle candle: list){
                    candle.setOutPrice(candle.getPrice().multiply(BigDecimal.valueOf(3L)));
                }
            } catch (Exception e) {
                model.put("list", list);
                model.put("report", Constants.ERROR);
                model.put("message", "error: " + e.getMessage());
            }
            model.put("list", list);
        }

        /*  SELECTS */
        Iterable<Label> labels = labelRepository.findAll();
        if (labels != null) model.put("labels", labels);

        Iterable<Box> boxes = boxRepository.findAll();
        if (boxes!= null) model.put("boxes", boxes);

        Iterable<Glass> glasses = glassRepository.findAll();
        if (glasses != null) model.put("glasses", glasses);

        Iterable<Prices> prices = pricesRepository.findAll();
        if (prices != null) model.put("prices", prices);
        /*  SELECTS - */

        return new ModelAndView("index", model);
    }


    @RequestMapping(path = "/candle/add")
    public ModelAndView add(
            @RequestParam String name, Long weight, Long label, Long box,
            Long glass, Long prices, Boolean diy, Boolean aroma, Long quantity){
        ModelAndView mv;

        if (prices != 0) {
            try {
                Candle candle = new Candle();
                candle.setName(name.trim());
                candle.setWeight(weight);
                candle.setLabel(labelRepository.findOne(label));
                candle.setBox(boxRepository.findOne(box));
                candle.setGlass(glassRepository.findOne(glass));
                candle.setPrices(pricesRepository.findOne(prices));
                candle.setDiy(diy==null?false:diy);
                candle.setAroma(aroma==null?false:aroma);
                candle.setQuantity(quantity>0L?quantity:0L);
                if (candle.getGlass() != null && candle.getWeight() <= candle.getGlass().getWeight())
                    throw new Exception("Неверный вес свечи, посуда должна весить меньше изделия!");
                candleRepository.save(candle);
                mv = getAll();
                mv.addObject("report", Constants.SAVED);
                mv.addObject("message", "свеча " + "<span class=\"saved_product_color\">" + candle.getName() + "</span>" + " сохранена");
                return mv;
            } catch (Exception e) {
                mv = getAll();
                mv.addObject("report", Constants.ERROR);
                mv.addObject("message", "error: " + e.getMessage());
                return mv;
            }
        } else {
            mv = getAll();
            mv.addObject("report", Constants.ERROR);
            mv.addObject("message", "error: цены не установленны");
            return mv;
        }
    }

    @RequestMapping(value = "/candle/delete")
    public ModelAndView delete(@RequestParam Long id){
        try {
            candleRepository.delete(id);
            return getAll();
        } catch (Exception e) {
            return getAll().addObject("report", Constants.ERROR)
                    .addObject("message", "Ошибка удаления!");
        }
    }

    @RequestMapping(value = "/candle/quantity", method = RequestMethod.POST)
    public ModelAndView update(@RequestParam Long id, Long quantity) {
        if (id != null && quantity != null) {
        try {
            candleRepository.updateQuantity(id, quantity);
        } catch (Exception e) {
            return getAll().addObject("report", Constants.ERROR)
                    .addObject("message", e.getMessage());
        }
        }
        return getAll();
    }
    @RequestMapping(value = "/update/candle", method = RequestMethod.POST)
    public ModelAndView updateCandle(@RequestParam Long id, String name, Long glass, Long label, Long box,
                                     Boolean diy, Boolean aroma, Long weight, Long prices, Long quantity){
        ModelAndView mv;
        Candle candle = new Candle();
        candle.setId(id);
        candle.setName(name);
        candle.setGlass(glassRepository.findOne(glass));
        candle.setLabel(labelRepository.findOne(label));
        candle.setBox(boxRepository.findOne(box));
        candle.setPrices(pricesRepository.findOne(prices));
        candle.setDiy(diy==null?false:diy);
        candle.setAroma(aroma==null?false:aroma);
        candle.setWeight(weight);
        candle.setQuantity(quantity);

        try {
            if (candle.getGlass() != null && candle.getWeight() <= candle.getGlass().getWeight())
                throw new Exception("Неверный вес свечи, посуда должна весить меньше изделия!");
            candleRepository.save(candle);
            mv = getAll();
            mv.addObject("report", Constants.SAVED);
            mv.addObject("message", "свеча " + "<span class=\"saved_product_color\">" + candle.getName() + "</span>" + " обновлена");
            return mv;
        } catch (Exception e){
            mv = getAll();
            mv.addObject("report", Constants.ERROR);
            mv.addObject("message", "Произошла ошибка при обновлении: " + e.getMessage());
            return mv;

        }
    }

    @RequestMapping(value = "/update/candle", method = RequestMethod.GET)
    public ModelAndView updateCandlePage(@RequestParam Long id){
        ModelAndView mv = getAll();
        mv.setViewName("update/candle");
        Candle candle = candleRepository.findOne(id);
        mv.addObject("candle", candle);
        mv.addObject("title", "Update page");

        return mv;
    }

    @RequestMapping(value = "/amount")
    public ModelAndView amount(){
        ModelAndView mv = new ModelAndView("amount");
        BigDecimal spendMoney = BigDecimal.ZERO;
        long count = 0L;

        try {
            Iterable<Candle> candles = candleRepository.findAll();
            for (Candle candle: candles){
              spendMoney = spendMoney.add(candle.getPrice().multiply(BigDecimal.valueOf(candle.getQuantity())));
              count = count + candle.getQuantity();
            }
        } catch (Exception e) {
            mv.addObject("title", Constants.AMOUNT_TITLE);
            mv.addObject("report", Constants.ERROR);
            mv.addObject("message", "Произошла ошибка: " + e.getMessage());
        }


        mv.addObject("countOfItems", count);
                mv.addObject("title", Constants.AMOUNT_TITLE);
                mv.addObject("spentMoney", spendMoney);
                mv.addObject("saleMoney", spendMoney.multiply(BigDecimal.valueOf(3L)));
                return mv;
    }

    @RequestMapping(value = "/candle")
    public ModelAndView home(){
                return getAll();
    }
}

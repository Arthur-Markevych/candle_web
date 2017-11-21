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
    public @ResponseBody ModelAndView getAll(){
        Map<String, Object> model = new HashMap<>();
        model.put("title", title);
        Iterable<Candle> list = candleRepository.findAll();
        if (list != null) {
            for (Candle candle: list){
                candle.setOutPrice(candle.getPrice().multiply(BigDecimal.valueOf(3L)));
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
                candle.setDiy(diy);
                candle.setAroma(aroma);
                candle.setPrice();
                candle.setQuantity(quantity>0L?quantity:0L);
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

    @RequestMapping(value = "/candle/update", method = RequestMethod.POST)
    public ModelAndView update(@RequestParam Long id, Long quantity) {
        try {
            candleRepository.updateQuantity(id, quantity);
        } catch (Exception e) {
            return getAll().addObject("report", Constants.ERROR)
                    .addObject("message", e.getMessage());
        }
        return getAll();
    }

    @RequestMapping(value = "/amount")
    public ModelAndView amount(){
        Amount amount = candleRepository.getAmount();

        return new ModelAndView("amount").addObject("countOfItems", amount.getCountOfItems())
                .addObject("title", Constants.AMOUNT_TITLE)
                .addObject("spentMoney", amount.getSpentMoney())
                .addObject("saleMoney", amount.getSaleMoney());
    }

    @RequestMapping(value = "/candle")
    public ModelAndView home(){
                return getAll();
    }
}

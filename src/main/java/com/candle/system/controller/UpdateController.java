package com.candle.system.controller;

import com.candle.system.configurations.Constants;
import com.candle.system.model.*;
import com.candle.system.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;

@RestController
public class UpdateController {

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





}

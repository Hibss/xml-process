package com.syz.xml.process.controller;

import com.syz.xml.process.service.CityService;
import com.syz.xml.process.service.CountryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class XmlController {

    @Autowired
    private CityService cityService;

    @Autowired
    private CountryService countryService;

    @GetMapping("cityXml")
    public void createCityXML() throws Exception {
        cityService.createXml();
    }

    @GetMapping("countryXml")
    public void createCountryXML(){
        countryService.createXml();
    }
}

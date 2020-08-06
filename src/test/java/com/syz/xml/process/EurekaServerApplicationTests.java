package com.syz.xml.process;

import com.syz.xml.process.service.CountryService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@SpringBootTest
@RunWith(SpringRunner.class)
public class EurekaServerApplicationTests {

	@Test
	public void contextLoads() {
	}

	@Autowired
	private CountryService countryService;

	@Test
	public void testXML(){
		countryService.createXml();
	}

}

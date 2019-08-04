package com.didispace;

import com.didispace.service.ComputeService;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;


@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = Application.class)
public class ApplicationTests {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	private ComputeService computeService;

	@Test
	public void testAdd() throws Exception {
		Integer result = computeService.add(1, 2);
		logger.info("computeService.add的计算结果为: {}", result);
		System.err.println("computeService.add的计算结果为: "+result);
		Assert.assertEquals("compute-service:add", new Integer(3), result);
	}

}

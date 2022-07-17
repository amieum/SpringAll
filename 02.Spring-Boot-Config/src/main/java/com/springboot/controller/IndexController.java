package com.springboot.controller;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.bean.BlogProperties;
import com.springboot.bean.ConfigBean;
import com.springboot.bean.TestConfigBean;

import java.util.HashMap;
import java.util.Map;


@RestController
public class IndexController implements ApplicationContextAware {
	@Autowired
	private BlogProperties blogProperties;
	@Autowired
	private ConfigBean configBean;
	@Autowired
	private TestConfigBean testConfigBean;

	private ApplicationContext app;
	
	@RequestMapping("/")
	Object index() {
		Map map = new HashMap();
		TestConfigBean testConfigBean = app.getBean("testConfigBean", TestConfigBean.class);
		System.out.println(testConfigBean);
		System.out.println(testConfigBean.object());
		System.out.println(testConfigBean.object());
		map.put("TestConfigBean","");
		map.put("TestConfigBean Type","");
		map.put("ConfigBean",configBean);
		map.put("BlogProperties",blogProperties);
		return map;
	}

	/**
	 * 获取ioc
	 * @param applicationContext
	 * @throws BeansException
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		this.app = applicationContext;
	}
}

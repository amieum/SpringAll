package com.springboot.service;

import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;

import com.springboot.bean.Student;

import java.util.List;

@CacheConfig(cacheNames = "student")
public interface StudentService {
	@Cacheable(key = "'students'")
	List<Student> queryAll();

	@CachePut(key = "#p0.sno")
	Student update(Student student);

	@CacheEvict(key = "#p0", allEntries = true)
	void deleteStudentBySno(String sno);
	
	@Cacheable(key = "#p0")
	Student queryStudentBySno(String sno);

	@CacheEvict(key = "#sno")
	void justClearCache(String sno);

	@CacheEvict(allEntries = true)// 删除student~keys保存的所有key
	void justClearCacheAll();
}

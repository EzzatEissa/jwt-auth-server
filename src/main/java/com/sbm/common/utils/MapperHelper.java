package com.sbm.common.utils;

import org.dozer.DozerBeanMapper;
import org.dozer.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class MapperHelper {

	
	@Autowired
	private DozerBeanMapper dozerBeanMapper;


	public <T> T transform(Object source, Class<T> clazz){
		if(source == null)
			return null;
		return dozerBeanMapper.map(source, clazz);
	}

	public <T> T transform(Object source, Class<T> clazz,String mapId){
		if(source == null)
			return null;
		return dozerBeanMapper.map(source,clazz,mapId);
	}
	
	public  <T, U> List<U> transform(List<T> source,  Class<U> destType) {

		if(source == null)
			return null;
		
        final List<U> dest = new ArrayList<U>();

        for (T element : source) {
        if (element == null) {
            continue;
        }
        dest.add(dozerBeanMapper.map(element, destType));
    }

    // finally remove all null values if any
    List s1 = new ArrayList();
    s1.add(null);
    dest.removeAll(s1);

    return dest;
}

	public <T> void map(Object source, Object destination) {
		if (source == null)
			return;
		dozerBeanMapper.map(source, destination);
	}


}
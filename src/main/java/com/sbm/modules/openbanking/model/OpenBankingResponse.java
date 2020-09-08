package com.sbm.modules.openbanking.model;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

public class OpenBankingResponse implements Serializable {
	private static final long serialVersionUID = 4949662200148334400L;

	@JsonIgnore
	private String listName;
	@JsonIgnore
	private List<? extends OpenBankingType> responseList;
	
	private OpenBankingResponse(String listName, List<? extends OpenBankingType> responseList) {
		this.listName = listName;
		this.responseList = responseList;
	}

	public static OpenBankingResponse instance(TYPE type, List<? extends OpenBankingType> responseList) {
		return new OpenBankingResponse(type.toString(),responseList);
	}

	@JsonAnyGetter
	private Map<String, Map<String,List<? extends OpenBankingType>>> responseList() {
		return Collections.singletonMap("Data", Collections.singletonMap(listName,responseList));
	}
	
	public enum TYPE {
		ACCOUNT {
	        public String toString() {
	            return "Account";
	        }
	    }

	};
}

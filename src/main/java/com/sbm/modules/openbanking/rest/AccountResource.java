package com.sbm.modules.openbanking.rest;

import com.sbm.modules.openbanking.model.OpenBankingResponse;
import com.sbm.modules.openbanking.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.HttpHeaders;

@Service
@Path("/accounts")
public class AccountResource {
	
	@Autowired
	private AccountService accountService;
	
	@GET
    @Produces("application/json")
    public OpenBankingResponse getAllAccounts(@Context HttpHeaders httpheaders) {
//		Account.AccountInfo info = new Account.AccountInfo();
//		info.setIdentification("SA1220000001150007129906");
//		info.setName("Mohamed");
//		info.setSchemeName("IBAN");
//		
//		Account.Servicer servicer = new Account.Servicer();
//		servicer.setIdentification("RIBLSARIXXX");
//		servicer.setSchemeName("BIC");
//		
//		Account acc = new Account();
//		acc.setAccountId("10040056677");
//		acc.setStatus("Enabled");
//		acc.setCurrency("SAR");
//		
//		acc.setAccount(info);
//		acc.setServicer(servicer);
//		
//		Account acc2 = new Account();
//		acc2.setAccountId("9999888888");
//		acc2.setStatus("Disabled");
//		acc2.setCurrency("USD");
//		
//		acc2.setAccount(info);
//		acc2.setServicer(servicer);
       
		String accountNo = null;
		return OpenBankingResponse.instance(OpenBankingResponse.TYPE.ACCOUNT,
				accountService.getAccounts(httpheaders.getHeaderString("x-rb-user-id"), accountNo));
    }
	
	
	@POST
	@Path("/portal")
    @Produces("application/json")
    public OpenBankingResponse portal(Object data) {
		System.out.println("portal--------------------------");
		return OpenBankingResponse.instance(OpenBankingResponse.TYPE.ACCOUNT,
				accountService.getAccounts("", ""));
    }

	@GET
	@Path("/portalget")
    @Produces("application/json")
    public OpenBankingResponse portalGet() {
		System.out.println("portal-GET-------------------------");
		return OpenBankingResponse.instance(OpenBankingResponse.TYPE.ACCOUNT,
				accountService.getAccounts("", ""));
    }
	
}

package com.orangecaraibe.enabler.crm.soa.provider;

import com.orangecaraibe.soa.v2.criteria.CriteriaComposer;

public class BaseManageProvider
{

	protected CriteriaComposer composeCriteria()
	{
		return CriteriaComposer.getInstance();
	}

}

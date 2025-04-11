package com.orangecaraibe.enabler.crm.webservice.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CustomerProblemHelper
{

	@Value( "${customer.problem.confirmation.htmlText1}" )
	private String customerProblemHTML1;

	@Value( "${customer.problem.confirmation.htmlText2}" )
	private String customerProblemHTML2;

	@Value( "${customer.problem.confirmation.htmlText3}" )
	private String customerProblemHTML3;

	@Value( "${customer.problem.confirmation.htmlText4}" )
	private String customerProblemHTML4;

	@Value( "${customer.problem.confirmation.mail.adresse}" )
	private String mailAdresse;

	public String getCustomerProblemHTML1()
	{
		return customerProblemHTML1;
	}

	public void setCustomerProblemHTML1( String customerProblemHTML1 )
	{
		this.customerProblemHTML1 = customerProblemHTML1;
	}

	public String getCustomerProblemHTML2()
	{
		return customerProblemHTML2;
	}

	public void setCustomerProblemHTML2( String customerProblemHTML2 )
	{
		this.customerProblemHTML2 = customerProblemHTML2;
	}

	public String getCustomerProblemHTML3()
	{
		return customerProblemHTML3;
	}

	public void setCustomerProblemHTML3( String customerProblemHTML3 )
	{
		this.customerProblemHTML3 = customerProblemHTML3;
	}

	public String getCustomerProblemHTML4()
	{
		return customerProblemHTML4;
	}

	public void setCustomerProblemHTML4( String customerProblemHTML4 )
	{
		this.customerProblemHTML4 = customerProblemHTML4;
	}

	public String getMailAdresse()
	{
		return mailAdresse;
	}

	public void setMailAdresse( String mailAdresse )
	{
		this.mailAdresse = mailAdresse;
	}
}

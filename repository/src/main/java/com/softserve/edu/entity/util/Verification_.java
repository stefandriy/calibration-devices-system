package com.softserve.edu.entity.util;

import javax.persistence.metamodel.SingularAttribute;

import com.softserve.edu.entity.Provider;
import com.softserve.edu.entity.Verification;

@javax.persistence.metamodel.StaticMetamodel(com.softserve.edu.entity.Verification.class)
public class Verification_ {
	
	
	public static volatile SingularAttribute<Verification,String> id;
	  public static volatile SingularAttribute<Verification,Provider> provider;
	  public static volatile SingularAttribute<Verification,Status> status;
}

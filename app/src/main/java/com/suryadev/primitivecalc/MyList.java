package com.suryadev.primitivecalc;

import java.util.ArrayList;
import java.util.Iterator;

class MyList extends ArrayList<String> {

 
	@Override
	public String toString() {
	
		 Iterator<String> iter = this.iterator();
		
          String str = "";
          
          while(iter.hasNext()) {
        	
        	 str += iter.next();
        	   
          }
		  
		 return str; 
	}

}
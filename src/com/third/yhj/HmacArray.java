package com.third.yhj;

public class HmacArray implements Comparable<String>{
	private String key;
	private Object[] items;

	public static HmacArray create(String key, Object[] items){
		return new HmacArray(key, items);
	}
	
	private HmacArray(String key, Object[] items){
		this.key = key;
		this.items = items;
	  }
	
	public String getKey() {
		return this.key;
	}
	
	public void setKey(String key) {
		this.key = key;
	}
	
	public Object[] getItems() {
		return this.items;
	}
	
	public void setItems(Object[] items) {
		this.items = items;
	}
	
	public int compareTo(String o) {
		int len1 = this.key.length();
	    int len2 = o.length();
	    int lim = Math.min(len1, len2);
	    char[] v1 = this.key.toCharArray();
	    char[] v2 = o.toCharArray();
	
	    int k = 0;
	    while (k < lim) {
	      char c1 = v1[k];
	      char c2 = v2[k];
	      if (c1 != c2) {
	        return c1 - c2;
	      }
	      k++;
	    }
	    return len1 - len2;
	}
}

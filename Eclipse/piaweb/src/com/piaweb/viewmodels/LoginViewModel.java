package com.piaweb.viewmodels;

public class LoginViewModel {
	private String username;
	private String contra;
	
	public LoginViewModel() {
		
	}
	
	public LoginViewModel(String username, String contra) {
		super();
		this.username = username;
		this.contra = contra;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getContra() {
		return contra;
	}
	public void setContra(String contra) {
		this.contra = contra;
	}
	
}

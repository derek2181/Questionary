package com.piaweb.viewmodels;

import com.piaweb.models.Answer;

public class AnswerCardViewModel {
	private Answer answer;
	private String nombreUsuario;
	private int likes;
	private int dislikes;
	private int liked;
	private int disliked;
	public AnswerCardViewModel() {
		answer = new Answer();
	}
	public Answer getAnswer() {
		return answer;
	}
	public void setAnswer(Answer answer) {
		this.answer = answer;
	}
	public String getNombreUsuario() {
		return nombreUsuario;
	}
	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}
	public int getLikes() {
		return likes;
	}
	public void setLikes(int likes) {
		this.likes = likes;
	}
	public int getDislikes() {
		return dislikes;
	}
	public void setDislikes(int dislikes) {
		this.dislikes = dislikes;
	}
	public int getLiked() {
		return liked;
	}
	public void setLiked(int liked) {
		this.liked = liked;
	}
	public int getDisliked() {
		return disliked;
	}
	public void setDisliked(int disliked) {
		this.disliked = disliked;
	}
	
	
	
}

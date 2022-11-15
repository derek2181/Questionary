package com.piaweb.viewmodels;

import java.io.InputStream;
import java.sql.Blob;

import com.piaweb.models.Question;

public class QuestionDetailsViewModel {
	private Question question;
	private boolean image;
	private String idUsuario;
	private String nombreUsuario;
	private String apellidoPUsuario;

	private int idCategoria;
	private String nombreCategoria;
	private int likes;
	private int dislikes;
	private int favs;
	private int liked;
	private int disliked;
	private int faved;
	public QuestionDetailsViewModel() {
		question = new Question();
	}

	public Question getQuestion() {
		return question;
	}

	public boolean isImage() {
		return image;
	}

	public int getDisliked() {
		return disliked;
	}

	public void setDisliked(int disliked) {
		this.disliked = disliked;
	}

	public void setImage(boolean image) {
		this.image = image;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}
	
	public String getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(String idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNombreUsuario() {
		return nombreUsuario;
	}

	public void setNombreUsuario(String nombreUsuario) {
		this.nombreUsuario = nombreUsuario;
	}

	public String getApellidoPUsuario() {
		return apellidoPUsuario;
	}

	public void setApellidoPUsuario(String apellidoPUsuario) {
		this.apellidoPUsuario = apellidoPUsuario;
	}
	public int getIdCategoria() {
		return idCategoria;
	}

	public void setIdCategoria(int idCategoria) {
		this.idCategoria = idCategoria;
	}

	public String getNombreCategoria() {
		return nombreCategoria;
	}

	public void setNombreCategoria(String nombreCategoria) {
		this.nombreCategoria = nombreCategoria;
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

	public int getFavs() {
		return favs;
	}

	public void setFavs(int favs) {
		this.favs = favs;
	}

	public int getLiked() {
		return liked;
	}

	public void setLiked(int liked) {
		this.liked = liked;
	}

	public int getFaved() {
		return faved;
	}

	public void setFaved(int faved) {
		this.faved = faved;
	}
}

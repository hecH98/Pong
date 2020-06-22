// Diego Arredondo A01633932
// Hector Herrera A01632115
// Proyecto final pong

import java.awt.Color;
import java.awt.Graphics;
import java.io.File;
import java.io.InputStream;
import java.util.Random;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;

public class Bola {
	private int x1,
				y1,
				diametro,
				velocidad;
	private PanelPong pp;
	private Random ran;
	private boolean direccionX,
					direccionY;
	
	private File wall = new File("pong_wall.wav");
	private File paddle = new File("pong_paddle.wav");
	private File fail = new File("pong_fail.wav");
	
	public Bola(PanelPong pp,int x1, int y1) {
		this.diametro=20;
		this.pp=pp;
		this.x1=x1;
		this.y1=y1;
		this.ran= new Random();
		this.direccionX=ran.nextBoolean();
		this.direccionY=ran.nextBoolean();
		this.velocidad = 10;
	}
	
	public void playWall(File sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playPaddle(File sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void playFail(File sound) {
		try {
			Clip clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(sound));
			clip.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void setX1(int x1) {
		this.x1=x1;
	}
	public void setY1(int y1) {
		this.y1=y1;
	}
	public void setDiametro(int diametro) {
		this.diametro=diametro;
	}
	public void moverBola(PaletaJugador pj1,PaletaJugador pj2) throws InterruptedException {
		
		if(this.y1+this.velocidad<0) {
			direccionY=false;
			playWall(this.wall);
		}
		if(this.y1+diametro-this.velocidad>this.pp.getAlto()) {
			direccionY=true;
			playWall(this.wall);
		}
		
		
		if(this.colision(pj1)==1) {
			this.direccionX=false;
			playPaddle(this.paddle);
		}

		if (this.colision(pj2)==1) {
			this.direccionX=true;
			playPaddle(this.paddle);
		}
		if (this.colision(pj1)==-1) {
			pj2.setPuntos(pj2.getPuntos()+1);
			playFail(this.fail);
			this.reiniciar();
		}
		if (this.colision(pj2)==-1) {
			pj1.setPuntos(pj1.getPuntos()+1);
			playFail(this.fail);
			this.reiniciar();
		}
		
		if (this.direccionX) {
			this.x1-=velocidad;
		}else {
			this.x1+=velocidad;
		}
		
		if(this.direccionY) {
			this.y1-=velocidad;
		}else {
			this.y1+=velocidad;
		}
		
	}
	
	public void reiniciar() throws InterruptedException {
		this.direccionY=ran.nextBoolean();
		this.x1=pp.getAncho()/2-38;
		this.y1=pp.getAlto()/2-20;
		Thread.sleep(500);
	}
	public int colision(PaletaJugador pj) {
		//1 Paleta
		//0 Nada
		//-1 Pared
		if(this.x1 <= pj.getX1()+pj.getAncho() &&  this.x1 + this.diametro > pj.getX1() && this.y1 < pj.getY1() 
				+ pj.getAlto() && this.y1 + this.diametro > pj.getY1()) {
			return 1;
		}
		else if((this.x1<0&&pj.getNumero()==1) || (this.x1>this.pp.getAncho()&&pj.getNumero()==2)){
			return-1;
		}
		return 0;
	}
	

	public void pintaBola(Graphics g) {
		g.setColor(Color.white);
		g.fillOval(x1, y1, diametro, diametro);
	}
	public void setVelocidad(int velocidad) {
		this.velocidad=velocidad;
	}
	
}

// Diego Arredondo A01633932
// Hector Herrera A01632115
// Proyecto final pong

import java.awt.Color;
import java.awt.Graphics;

public class PaletaJugador {
	private int x1,
				y1,
				ancho,
				alto,
				puntos,
				numero,
				velocidad;
	private PanelPong pp;
	public PaletaJugador(int x1,int y1,int ancho,int alto,PanelPong pp,int numero) {
		this.x1=x1;
		this.y1=y1;
		this.ancho=ancho;
		this.alto=alto;
		this.pp=pp;
		this.puntos=0;
		this.numero=numero;
		this.velocidad=30;
	}
	public void setY1(int y1) {
		this.y1=y1;
	}
	public int getY1() {
		return this.y1;
	}
	public int getX1() {
		return this.x1;
	}
	public void setPuntos(int puntos) {
		this.puntos=puntos;
	}
	public int getPuntos() {
		return puntos;
	}
	public int getNumero() {
		return this.numero;
	}
	public void setAlto(int alto) {
		this.alto=alto;
	}
	public void setVelociadad(int velocidad) {
		this.velocidad=velocidad;
	}
	public void pintaPaleta(Graphics g) {
		g.setColor(Color.white);
		g.fillRect(x1, y1, ancho, alto);
	}
	public void moverPaleta(boolean up) {
		if(up) {
			if(this.y1 + this.velocidad-20> 0  ) {
				this.y1-=this.velocidad;
			}
		}else {
			if(this.y1 + this.alto + this.velocidad-15< pp.getHeight()) {
				this.y1+=this.velocidad;
			}
		}
	}
	public int getAncho() {
		return ancho;
	}
	public int getAlto() {
		return alto;
	}
}

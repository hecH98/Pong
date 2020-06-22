// Diego Arredondo A01633932
// Hector Herrera A01632115
// Proyecto final pong

import javax.swing.JFrame;

public class VentanaPong extends JFrame {
	private PanelPong pp;
	public VentanaPong() {
		super();
		this.setTitle("Pong");
		this.pp = new PanelPong();
		this.add(pp);
		this.pack();
		this.setVisible(true);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setExtendedState(JFrame.MAXIMIZED_BOTH);
	}
	public static void main(String[] args) {
		new VentanaPong();
	}

}

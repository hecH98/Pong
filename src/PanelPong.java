// Diego Arredondo A01633932
// Hector Herrera A01632115
// Proyecto final pong

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class PanelPong extends JPanel implements KeyListener, Runnable{
	private PaletaJugador jugador1,
						  jugador2;
	private int ancho,
				alto,
				estado,
				puntosMaximos,
				ganador;
	private JButton btnIniciar,
					btnAjustes,
					btnPausa,
					btnRegresar,
					btnSalir,
					btnExit;
	private JSlider sPaletas,
					sVelocidad,
					sBola,
					sPuntos;
	private Bola bola1;
	private Thread hilo;
	private Toolkit toolkit;
	private boolean p1FlagUp,
					p1FlagDown,
					p2FlagUp,
					p2FlagDown,
					jugando;
	
	private File soundtrack = new File("pong_soundtrack.wav");
	private File win = new File("pong_win.wav");
	
	private Clip clip = null;
	private Clip winClip = null;
	
	public PanelPong() {
		super();
		
		this.estado=1;
		this.puntosMaximos=7;
		this.setLayout(null);
		this.setBackground(Color.BLACK);
		this.toolkit = Toolkit.getDefaultToolkit();
		this.addKeyListener(this);
		this.setFocusable(true);
		this.ancho=(int) toolkit.getScreenSize().getWidth();
		this.alto=(int) toolkit.getScreenSize().getHeight()-50;
		this.setPreferredSize(new Dimension(this.ancho,this.alto));
		this.jugador1= new PaletaJugador(10,alto/2-50,40,100,this,1);
		this.jugador2= new PaletaJugador(this.ancho-50,this.alto/2-50,40,100,this,2);
		this.bola1= new Bola(this,this.ancho/2-38,this.alto/2-20);
		try {
			clip = AudioSystem.getClip();
			clip.open(AudioSystem.getAudioInputStream(soundtrack));
			winClip = AudioSystem.getClip();
			winClip.open(AudioSystem.getAudioInputStream(win));
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		this.btnIniciar = new JButton("INICIAR");
		this.btnIniciar.setFont(new Font("Arial",Font.PLAIN,75));
		this.btnIniciar.setForeground(Color.white);
		this.btnIniciar.setBounds(this.ancho/2-210, 300 , 400, 100);
		this.btnIniciar.setBackground(null);
		this.btnIniciar.setBorderPainted(false);
		this.add(this.btnIniciar);
		this.btnIniciar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					estado=3;
					jugador1.setPuntos(0);
					jugador2.setPuntos(0);
					PanelPong.this.repaint();
					PanelPong.this.hilo = new Thread (PanelPong.this);
					hilo.start();
					try {
						bola1.reiniciar();
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
					jugando=true;
			}
		});
		
		this.btnAjustes= new JButton("AJUSTES");
		this.btnAjustes.setFont(new Font("Arial",Font.PLAIN,75));
		this.btnAjustes.setForeground(Color.white);
		this.btnAjustes.setBounds(this.ancho/2-210, 500 , 400, 100);
		this.btnAjustes.setBackground(null);
		this.btnAjustes.setBorderPainted(false);
		this.add(btnAjustes);
		this.btnAjustes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					estado=2;
					repaint();
			}
		});
		
		this.btnExit= new JButton("SALIR DEL JUEGO");
		this.btnExit.setFont(new Font("Arial",Font.PLAIN,75));
		this.btnExit.setForeground(Color.white);
		this.btnExit.setBounds(this.ancho/2-410, 700 , 800, 100);
		this.btnExit.setBackground(null);
		this.btnExit.setBorderPainted(false);
		this.btnExit.setOpaque(false);
		this.add(this.btnExit);
		this.btnExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					System.exit(0);
			}
		});
	
		this.sVelocidad = new JSlider(JSlider.HORIZONTAL, 10, 60, 30);
		this.sVelocidad.setBounds(this.ancho/2-210, 200 , 400, 100);
		this.sVelocidad.setMajorTickSpacing(10);
		this.sVelocidad.setMinorTickSpacing(5);
		this.sVelocidad.setPaintTicks(true);
		this.sVelocidad.setPaintLabels(true);
		this.sVelocidad.setForeground(Color.white);
		this.sVelocidad.setOpaque(false);
		this.add(this.sVelocidad);
		this.sVelocidad.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				jugador1.setVelociadad(sVelocidad.getValue());
				jugador2.setVelociadad(sVelocidad.getValue());
				bola1.setVelocidad(sVelocidad.getValue()-10);
			}
		});
		
		this.sPaletas = new JSlider(JSlider.HORIZONTAL, 10, 100, 50);
		this.sPaletas.setBounds(this.ancho/2-210, 300 , 400, 100);
		this.sPaletas.setMajorTickSpacing(10);
		this.sPaletas.setMinorTickSpacing(5);
		this.sPaletas.setPaintTicks(true);
		this.sPaletas.setPaintLabels(true);
		this.sPaletas.setForeground(Color.white);
		this.sPaletas.setOpaque(false);
		this.add(this.sPaletas);
		this.sPaletas.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				jugador1.setAlto(sPaletas.getValue()+50);
				jugador2.setAlto(sPaletas.getValue()+50);
			}
		});
		
		this.sBola = new JSlider(JSlider.HORIZONTAL, 10, 100, 20);
		this.sBola.setBounds(this.ancho/2-210, 400 , 400, 100);
		this.sBola.setMajorTickSpacing(10);
		this.sBola.setMinorTickSpacing(5);
		this.sBola.setPaintTicks(true);
		this.sBola.setPaintLabels(true);
		this.sBola.setForeground(Color.white);
		this.sBola.setOpaque(false);
		this.add(this.sBola);
		this.sBola.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				bola1.setDiametro(sBola.getValue());
			}
		});
		
		this.sPuntos = new JSlider(JSlider.HORIZONTAL, 1, 30, 7);
		this.sPuntos.setBounds(this.ancho/2-210, 500 , 400, 100);
		this.sPuntos.setMajorTickSpacing(2);
		this.sPuntos.setMinorTickSpacing(1);
		this.sPuntos.setPaintTicks(true);
		this.sPuntos.setPaintLabels(true);
		this.sPuntos.setForeground(Color.white);
		this.sPuntos.setOpaque(false);
		this.add(this.sPuntos);
		this.sPuntos.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				puntosMaximos=sPuntos.getValue();
			}
		});
		
		this.btnRegresar= new JButton("REGRESAR");
		this.btnRegresar.setFont(new Font("Arial",Font.PLAIN,50));
		this.btnRegresar.setForeground(Color.white);
		this.btnRegresar.setBounds(this.ancho/2-210, 600 , 400, 100);
		this.btnRegresar.setBackground(null);
		this.btnRegresar.setBorderPainted(false);
		this.add(this.btnRegresar);
		this.btnRegresar.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					estado=1;
					repaint();
			}
		});
		
		this.btnPausa= new JButton("PAUSA");
		this.btnPausa.setFont(new Font("Arial",Font.PLAIN,20));
		this.btnPausa.setForeground(Color.white);
		this.btnPausa.setBounds(this.ancho/2-90, 20 , 120, 50);
		this.btnPausa.setBackground(null);
		this.btnPausa.setBorderPainted(false);
		this.btnPausa.setOpaque(false);
		this.add(btnPausa);
		this.btnPausa.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					if(jugando) {
						estado=4;
						jugando=false;
					}else {
						estado=3;
						PanelPong.this.requestFocus();
						jugando=true;
					}
			}
		});
		
		this.btnSalir= new JButton("SALIR");
		this.btnSalir.setFont(new Font("Arial",Font.PLAIN,20));
		this.btnSalir.setForeground(Color.white);
		this.btnSalir.setBounds(this.ancho/2-90, 20 , 120, 150);
		this.btnSalir.setBackground(null);
		this.btnSalir.setBorderPainted(false);
		this.btnSalir.setOpaque(false);
		this.add(this.btnSalir);
		this.btnSalir.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
					estado=1;
					hilo.stop();
					repaint();
			}
		});}
	
	public int getAlto() {
		return this.alto;
	}
	public int getAncho() {
		return this.ancho;
	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
		if (estado==1) {
			g.setFont(new Font("Arial",Font.BOLD,150));
			g.setColor(Color.white);
			g.drawString("PONG", (int) ((this.toolkit.getScreenSize().getWidth()/2)-230), 150);
			clip.start();
			winClip.setMicrosecondPosition(0);
			this.btnIniciar.setVisible(true);
			this.btnAjustes.setVisible(true);
			this.btnExit.setVisible(true);
			
			this.sVelocidad.setVisible(false);
			this.sPaletas.setVisible(false);
			this.sBola.setVisible(false);
			this.sPuntos.setVisible(false);
			this.btnRegresar.setVisible(false);
			
			this.btnSalir.setVisible(false);
			this.btnPausa.setVisible(false);
		}else if(estado==2) {
			this.btnIniciar.setVisible(false);
			this.btnAjustes.setVisible(false);
			this.btnExit.setVisible(false);
			
			this.btnSalir.setVisible(false);
			this.btnPausa.setVisible(false);
			
			g.setColor(Color.white);
			g.setFont(new Font("Arial",Font.BOLD,30));
			g.drawString("VELOCIDAD", (int) ((this.toolkit.getScreenSize().getWidth()/2)-100), 210);
			this.sVelocidad.setVisible(true);
			g.drawString("TAMAÑO RAQUETAS", (int) ((this.toolkit.getScreenSize().getWidth()/2)-160), 310);
			this.sPaletas.setVisible(true);
			g.drawString("TAMAÑO BOLA", (int) ((this.toolkit.getScreenSize().getWidth()/2)-120), 410);
			this.sBola.setVisible(true);
			g.drawString("PUNTOS MAXIMOS", (int) ((this.toolkit.getScreenSize().getWidth()/2)-140), 510);
			this.sPuntos.setVisible(true);
			g.setFont(new Font("Arial",Font.BOLD,150));
			g.drawString("AJUSTES", (int) ((this.toolkit.getScreenSize().getWidth()/2)-340), 150);
			this.btnRegresar.setVisible(true);
			
		}
		else if(estado==3 || estado == 4) {
			clip.stop();
			clip.setMicrosecondPosition(0);
			this.btnIniciar.setVisible(false);
			this.btnAjustes.setVisible(false);
			this.btnExit.setVisible(false);
			
			this.sVelocidad.setVisible(false);
			this.sPaletas.setVisible(false);
			this.sBola.setVisible(false);
			this.sPuntos.setVisible(false);
			this.btnRegresar.setVisible(false);
			
			this.btnSalir.setVisible(true);
			this.btnPausa.setVisible(true);
			
			g.setFont(new Font("Arial",Font.BOLD,50));
			g.setColor(Color.white);
			g.drawString(""+jugador1.getPuntos(), (int) ((this.toolkit.getScreenSize().getWidth()/2)-150), 60);
			g.drawString(""+jugador2.getPuntos(), (int) ((this.toolkit.getScreenSize().getWidth()/2)+70), 60);
			this.jugador1.pintaPaleta(g);
			this.jugador2.pintaPaleta(g);
			
			int largo= this.toolkit.getScreenSize().width/2-30;
			int x=20;
			g.setColor(Color.DARK_GRAY);
			for (int i = 0; i < 19; i++) {
				g.fillRect(largo, x, 5, 50);
				x+=60;
			}
			this.bola1.pintaBola(g);
			
			if (this.jugador1.getPuntos()>=this.puntosMaximos || this.jugador2.getPuntos()>=this.puntosMaximos) {
				this.hilo.stop();
				this.estado=5;
				if(this.jugador1.getPuntos()>this.jugador2.getPuntos()) {
					this.ganador =1;
				}else {
					this.ganador=2;
				}
				this.repaint();
			}
		}else if(estado == 5) {
			winClip.start();
			this.btnIniciar.setVisible(false);
			this.btnAjustes.setVisible(false);
			this.btnExit.setVisible(false);
			
			this.sVelocidad.setVisible(false);
			this.sPaletas.setVisible(false);
			this.sBola.setVisible(false);
			this.sPuntos.setVisible(false);
			this.btnRegresar.setVisible(true);
			
			this.btnSalir.setVisible(false);
			this.btnPausa.setVisible(false);
			
			g.setFont(new Font("Arial",Font.BOLD,50));
			g.setColor(Color.white);
			g.drawString("El jugador "+ganador+ " ha ganado!", (int) ((this.toolkit.getScreenSize().getWidth()/2)-300), 210);
		}
		
	}
	
	@Override
	public void keyPressed(KeyEvent e) {
		switch(e.getKeyCode()) {
        case KeyEvent.VK_W: p1FlagUp = true; break;
        case KeyEvent.VK_S: p1FlagDown = true; break;
        case KeyEvent.VK_UP: p2FlagUp = true; break;
        case KeyEvent.VK_DOWN: p2FlagDown = true; break;
		}
	}
		
	@Override
	public void keyReleased(KeyEvent e) {
		switch(e.getKeyCode()) {
        case KeyEvent.VK_W: p1FlagUp = false; break;
        case KeyEvent.VK_S: p1FlagDown = false; break;
        case KeyEvent.VK_UP: p2FlagUp = false; break;
        case KeyEvent.VK_DOWN: p2FlagDown = false; break;
		}
	}
	
	@Override
	public void keyTyped(KeyEvent e) {}
	
	public void run() {
		try {
			while(true) {
				if(this.jugando) {
					if(p1FlagUp) {
						this.jugador1.moverPaleta(true);
						this.repaint();
					}
					else if(p1FlagDown) {
						this.jugador1.moverPaleta(false);
						this.repaint();
					}
					if(p2FlagUp) {
						this.jugador2.moverPaleta(true);
						this.repaint();
					}
					else if(p2FlagDown) {
						this.jugador2.moverPaleta(false);
						this.repaint();
					}
					this.bola1.moverBola(jugador1, jugador2);
					this.repaint();
				}
				Thread.sleep(45);
			}
		}
		catch(InterruptedException e) {
			System.out.println("Error en sleep "+e);
		}
		
	}

}

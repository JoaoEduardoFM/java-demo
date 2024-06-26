package jogo.personagens.jogador;

import java.awt.Color;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import jogo.combate.ControleAtaqueEmArea;
import jogo.combate.ControleEspada;
import jogo.combate.ControleTiros;
import jogo.personagens.npc.Mob;
import jogo.util.Ator;
import jplay.Keyboard;
import jplay.Scene;
import jplay.URL;
import jplay.Window;

public class Jogador extends Ator {

	public int vidaJogador;
	private boolean movendo = true;
	private long ultimoDisparo = 0;
	private long delayEntreTiros = 700;
	private long lastUpdateTime = System.currentTimeMillis();
	private long animationDelay = 1000; // Ajuste conforme necessário (em milissegundos)
	private String sprite;

	public Jogador(int x, int y, int vidaJogador) {
		super(URL.sprite("magoMarrom.png"), 20);
		this.x = x;
		this.y = y;
		this.sprite = getSprite();
		this.setTotalDuration(2000);
		Ator.vida = vidaJogador;
	}

	ControleTiros tiros = new ControleTiros();
	ControleEspada espada = new ControleEspada();
	ControleAtaqueEmArea atkArea = new ControleAtaqueEmArea();

	private int proximaSequencia = -1;
	private float interpolacao = 0.0f;
	private static final float VELOCIDADE_INTERPOLACAO = 0.05f;

	public void atualizarAnimacao() {
		if (proximaSequencia != -1) {
			interpolacao += VELOCIDADE_INTERPOLACAO;
			setSequence(proximaSequencia, proximaSequencia + 1);
			update();
			if (interpolacao >= 2.0f) {
				proximaSequencia = -1;
				interpolacao = 0.0f;
				if (direcao == 5) {
					setSequence(1, 4);
				} else if (direcao == 4) {
					setSequence(13, 16);
				} else if (direcao == 2) {
					setSequence(9, 12);
				} else if (direcao == 1) {
					setSequence(5, 8);
				}
			}

		}
	}
	
	public void ataqueEmAreaExplosao(Window janela, Scene cena, Keyboard teclado, Mob[] mobs) {
		if (direcao != 0 &&  teclado.keyDown(Keyboard.SPACE_KEY) && System.currentTimeMillis() - ultimoDisparo > delayEntreTiros) {
			atkArea.atacarEmArea(x - 80, y - 120, direcao, cena, "atakAreaExplosao.png", 5);
			ultimoDisparo = System.currentTimeMillis();
		}

		atkArea.run(mobs, janela, teclado , 5);
		atualizarAnimacao();
	}
	
	public void ataqueEmAreaAgua(Window janela, Scene cena, Keyboard teclado, Mob[] mobs) {
		if (direcao != 0 &&  teclado.keyDown(Keyboard.D_KEY) && System.currentTimeMillis() - ultimoDisparo > delayEntreTiros) {
			atkArea.atacarEmArea(x - 80, y - 80, direcao, cena, "atakAreaAgua.png", 5);
			ultimoDisparo = System.currentTimeMillis();
		}

		atkArea.run(mobs, janela, teclado , 5);
		atualizarAnimacao();
	}
	
	public void ataquePadrao(Window janela, Scene cena, Keyboard teclado, Mob[] mobs, String sprite) {
		if (direcao != 0 && teclado.keyDown(Keyboard.A_KEY) && System.currentTimeMillis() - ultimoDisparo > delayEntreTiros) {
			proximaSequencia = (direcao == 1) ? 17 : (direcao == 2) ? 18 : (direcao == 5) ? 16 : (direcao == 4) ? 19 : 1;
			tiros.adicionaTiro(x + 5, y + 12, direcao, cena, "tiro.png");
			ultimoDisparo = System.currentTimeMillis();
		}

		tiros.run(mobs, janela, teclado, sprite);
		atualizarAnimacao();
	}
	
	public void ataquePadraoGelo(Window janela, Scene cena, Keyboard teclado, Mob[] mobs, String sprite) {
		if (direcao != 0 && teclado.keyDown(Keyboard.S_KEY) && System.currentTimeMillis() - ultimoDisparo > delayEntreTiros) {
			proximaSequencia = (direcao == 1) ? 17 : (direcao == 2) ? 18 : (direcao == 5) ? 16 : (direcao == 4) ? 19 : 1;
			tiros.adicionaTiro(x + 5, y + 12, direcao, cena, "iceAtak.png");
			ultimoDisparo = System.currentTimeMillis();
		}

		tiros.run(mobs, janela, teclado, sprite);
		atualizarAnimacao();
	}
	
	public Boolean controle(Window janela, Keyboard teclado) {

		if (teclado.keyDown(Keyboard.UP_KEY) || teclado.keyDown(Keyboard.DOWN_KEY)
				|| teclado.keyDown(Keyboard.RIGHT_KEY) || teclado.keyDown(Keyboard.LEFT_KEY)) {
			correrLogica(janela, teclado);
		}

		// Diagonal Superior Direita
		if (teclado.keyDown(Keyboard.UP_KEY) && teclado.keyDown(Keyboard.RIGHT_KEY)) {
			moverDiagonalSuperiorDireita(janela);
		}

		// Diagonal superior esquerda
		if (teclado.keyDown(Keyboard.UP_KEY) && teclado.keyDown(Keyboard.LEFT_KEY)) {
			moverDiagonalSuperiorEsquerda(janela);
		}

		// Diagonal Inferior Direita
		if (teclado.keyDown(Keyboard.DOWN_KEY) && teclado.keyDown(Keyboard.RIGHT_KEY)) {
			moverDiagonalInferiorDireita(janela);
		}

		// Diagonal inferior esquerda
		if (teclado.keyDown(Keyboard.DOWN_KEY) && teclado.keyDown(Keyboard.LEFT_KEY)) {
			moverDiagonalInferiorEsquerda(janela);
		}

		// movendo para esquerda
		if (teclado.keyDown(Keyboard.LEFT_KEY) && !teclado.keyDown(Keyboard.RIGHT_KEY)) {
			if (this.x > 0) {
				this.x -= velocidade;// impede que não saia da jenela
			}
			if (direcao != 1) {
				setSequence(5, 8);// sprite 4 e 8 do personagem
				direcao = 1;

			}
			movendo = true;
			// movendo para direta
		} else if (teclado.keyDown(Keyboard.RIGHT_KEY) && !teclado.keyDown(Keyboard.LEFT_KEY)) {
			if (this.x < janela.getWidth() - 60) {
				this.x += velocidade;
			}
			if (direcao != 2) {
				setSequence(9, 12);
				direcao = 2;

			}
			movendo = true;
			// movendo para cima
		} else if (teclado.keyDown(Keyboard.UP_KEY) && !teclado.keyDown(Keyboard.DOWN_KEY)) {
			if (this.y > 0) {
				this.y -= velocidade;
			}
			if (direcao != 4) {
				setSequence(13, 16);
				direcao = 4;

			}
			movendo = true;

			// movendo para baixo
		} else if (teclado.keyDown(Keyboard.DOWN_KEY) && !teclado.keyDown(Keyboard.UP_KEY)) {
			if (this.y < janela.getHeight() - 60) {
				this.y += velocidade;
			}
			if (direcao != 5) {
				setSequence(1, 3);
				direcao = 5;

			}
			movendo = true;
		} else {
			// parado
			movendo = false;
			/*
			 * if (direcao != 125) { setSequence(1, 3); direcao = 125; }
			 */
		}

		if (!movendo) {
			long currentTime = System.currentTimeMillis();			
			if (currentTime - lastUpdateTime > animationDelay) {		
				update();
				lastUpdateTime = currentTime;
			}
			movendo = false;
		}

		if (movendo) {
			// atualiza o frame caso o movendo seja true
			update();
			movendo = false;
		}
		return movendo;
	}

	private void correrLogica(Window janela, Keyboard teclado) {
		janela.addKeyListener(new KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				// Lógica para tratar a tecla pressionada
				char keyChat = e.getKeyChar();
				if (keyChat == KeyEvent.VK_SPACE) {
					velocidade = 6;
				} else {
					velocidade = 3;
				}
			}
		});
	}

	private void moverDiagonalSuperiorDireita(Window janela) {
		if (this.x < janela.getWidth() - 60 && this.y > 0) {
			this.x += velocidade / Math.sqrt(3);
			this.y -= velocidade / Math.sqrt(3);
		}
		if (direcao != 7) {
			setSequence(8, 11);
			direcao = 7;
		}
		movendo = true;
	}

	private void moverDiagonalSuperiorEsquerda(Window janela) {
		if (this.x > 0 && this.y > 0) {
			this.x -= velocidade / Math.sqrt(7);
			this.y -= velocidade / Math.sqrt(7);
		}
		if (direcao != 8) {
			setSequence(4, 8);
			direcao = 8;
		}
		movendo = true;
	}

	private void moverDiagonalInferiorDireita(Window janela) {
		if (this.x < janela.getWidth() - 60 && this.y > 0) {
			this.x -= velocidade / Math.sqrt(7);
			this.y += velocidade / Math.sqrt(7);
		}
		if (direcao != 9) {
			setSequence(8, 11);
			direcao = 9;
		}
		movendo = true;
	}

	private void moverDiagonalInferiorEsquerda(Window janela) {
		if (this.x > 0 && this.y > 0) {
			this.x += velocidade / Math.sqrt(7);
			this.y += velocidade / Math.sqrt(7);
		}
		if (direcao != 6) {
			setSequence(4, 8);
			direcao = 6;
		}
		movendo = true;
	}

	public void pontoDeVida(Window janela) {
		janela.drawText("Vida: " + Ator.vida, 30, 30, Color.green);
	}
	
	public double pontoDeJogo(Window janela) {
		janela.drawText("Pontos: " + Mob.pontos, 30, 60, Color.RED);
		return Mob.pontos;
	}

	public String getSprite() {
		return sprite;
	}

	public void setSprite(String sprite) {
		this.sprite = sprite;
	}

}

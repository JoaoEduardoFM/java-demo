package jogo.cenario;

import java.awt.event.KeyEvent;
import java.util.Arrays;

import jogo.personagens.jogador.Jogador;
import jogo.personagens.npc.Mob;
import jogo.util.Ator;
import jogo.util.Menu;
import jplay.Keyboard;
import jplay.Scene;
import jplay.URL;
import jplay.Window;

public class Pantano extends Cenario {

	private Window janela;
	private Scene cena;
	private Jogador jogador;
	private Keyboard teclado;
	private Mob[] mobs;
	private String[] nomesCenarios = { "Cenario1.scn", "Cenario2.scn", "Cenario3.scn" };
	private int indiceCenarioAtual = 0;
	private long tempoInicialCenario = System.currentTimeMillis();
	private Boolean pause = true;
	private Boolean jogadorVivo = true;
	private int pontosAnteriores  = 0;

	public Pantano(Window window, Jogador backupJogador, Mob[] backupMobs, String[] backupNomesCenarios,
			long backupTempoInicialCenario, int vidaJogador, int pontos, boolean novoJogo) {
		if(novoJogo == true) {
		Mob.pontos = 0;
		}
		janela = window;
		cena = new Scene();
		if(backupJogador != null) {
		jogador = new Jogador(500, 350, Ator.vida);
		}else {
		jogador = new Jogador(500, 350, 1500);	
		}
		mobs = new Mob[] { new Mob(800, 350, "esqueleto.png", 0.5, 250.0) };
		cena.loadFromFile(URL.scenario("Cenario1.scn"));
		teclado = janela.getKeyboard();
		// som.play("musica1.mid");
		run(window, backupJogador, backupMobs, backupNomesCenarios, backupTempoInicialCenario, vidaJogador, pontos);
	}

	private void run(Window window, Jogador backupJogador, Mob[] backupMobs, String[] backupNomesCenarios,
			long backupTempoInicialCenario, int vidaJogador, int pontos) {
		while (getPause()) {
			jogadorLogica(backupJogador != null ? backupJogador : jogador);
			mobLogica(backupJogador != null ? backupJogador : jogador);
		
			spawnarMob(backupMobs);

			if (window != null) {
				setJanela(window);
			}

			if (backupNomesCenarios != null) {
				setNomesCenarios(backupNomesCenarios);
			}

			if (backupTempoInicialCenario != 0) {
				setTempoInicialCenario(backupTempoInicialCenario);
			}
			if (backupJogador != null) {
				setJogador(backupJogador);
			}
			if (backupMobs != null) {
	            setMobs(Arrays.copyOf(backupMobs, backupMobs.length));
	            backupMobs = null;
	        }
			pause();
			
			if(Ator.vida == 0) {
				return;
			}
			// mudarCenario();
			try {
				Thread.sleep(16); // 60 FPS
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

		if (getPause().equals(false)) {
			try {
				Menu.manuLogica(janela, teclado, getJogador(), getMobs(), getNomesCenarios(), 0, vidaJogador, pontos);	
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

	}

	public void pause() {
		if (teclado.keyDown(KeyEvent.VK_ESCAPE) && getPause().equals(true)) {
			setPause(false);
		}
	}

	private void adicionarNovoMob(String mob, Mob[] backupMobs, Double velocidade, Double vidaMob) {
		int randomEdge = (int) (Math.random() * 4);

		int randomX = 0;
		int randomY = 0;

		switch (randomEdge) {
		case 0: // Topo
			randomX = (int) (Math.random() * janela.getWidth());
			randomY = -50; // Altura do Mob é 50
			break;
		case 1: // Base
			randomX = (int) (Math.random() * janela.getWidth());
			randomY = janela.getHeight();
			break;
		case 2: // Esquerda
			randomX = -50; // Largura do Mob é 50
			randomY = (int) (Math.random() * janela.getHeight());
			break;
		case 3: // Direita
			randomX = janela.getWidth();
			randomY = (int) (Math.random() * janela.getHeight());
			break;
		}

		if (backupMobs != null) {
			Mob novoMob = new Mob(randomX, randomY, mob, velocidade, vidaMob);
			backupMobs = Arrays.copyOf(backupMobs, backupMobs.length + 1);
			backupMobs[backupMobs.length - 1] = novoMob;
		}

		if (mobs != null) {
			Mob novoMob = new Mob(randomX, randomY, mob, velocidade, vidaMob);
			mobs = Arrays.copyOf(mobs, mobs.length + 1);
			mobs[mobs.length - 1] = novoMob;
		}
	}

	private void jogadorLogica(Jogador personagem) {
		personagem.controle(janela, teclado);
		personagem.caminho(cena);
		cena.moveScene(personagem);
		personagem.x += cena.getXOffset();
		personagem.y += cena.getYOffset();
		personagem.ataquePadrao(janela, cena, teclado, mobs, "tiro.png");
		personagem.ataquePadraoGelo(janela, cena, teclado, mobs, "iceAtak.png");
		personagem.ataqueEmAreaExplosao(janela, cena, teclado, mobs);
		personagem.ataqueEmAreaAgua(janela, cena, teclado, mobs);
		personagem.draw();
		
	}

	private void mobLogica(Jogador player) {
		for (Mob mob : mobs) {
			mob.morrer();
			mob.caminho(cena);
			mob.perseguir(player.x, player.y);
			mob.x += cena.getXOffset();
			mob.y += cena.getYOffset();
			mob.morrer();
			mob.atacar(player, mob);
			mob.draw();
		}

		// Desenha o jogador por último
		player.x += cena.getXOffset();
		player.y += cena.getYOffset();
		player.ataquePadrao(janela, cena, teclado, mobs, "tiro.png");
		player.ataquePadraoGelo(janela, cena, teclado, mobs, "iceAtak.png");
		player.ataqueEmAreaExplosao(janela, cena, teclado, mobs);
		player.ataqueEmAreaAgua(janela, cena, teclado, mobs);
		player.pontoDeVida(janela);
		player.pontoDeJogo(janela);
		player.draw();
		

		janela.update();
	}

	private void mudarCenario() {
		long tempoAtual = System.currentTimeMillis();
		long tempoDecorrido = (tempoAtual - tempoInicialCenario) / 1000; // converta para segundos

		if (tempoDecorrido >= 4) {
			// Altere o cenário
			indiceCenarioAtual = (indiceCenarioAtual + 1) % nomesCenarios.length;
			cena.loadFromFile(URL.scenario(nomesCenarios[indiceCenarioAtual]));

			// Reinicia o tempo inicial para permitir futuras mudanças de cenário
			tempoInicialCenario = System.currentTimeMillis();
		}
	}

	private void spawnarMob(Mob[] backupMobs) {
	    int pontosAtuais = Mob.pontos;
	    int mobsASpawnar = 2 * (pontosAtuais - pontosAnteriores);
	    int tipoDeMob = 0;
	    for (int i = 0; i < mobsASpawnar; i++) {
	        if (tipoDeMob == 0) {
	            adicionarNovoMob("esqueleto.png", backupMobs, 0.5, 250.0);
	        } else if (tipoDeMob == 1) {
	            adicionarNovoMob("orcPequeno.png", backupMobs, 2.0, 250.0);
	        } else {
	            adicionarNovoMob("javali.png", backupMobs, 1.0, 750.0);
	        }
	        
	        tipoDeMob = (tipoDeMob + 1) % 3; 
	    }

	    pontosAnteriores = pontosAtuais;
	}
	
	

	public Boolean getJogadorVivo() {
		return jogadorVivo;
	}

	public void setJogadorVivo(Boolean jogadorVivo) {
		this.jogadorVivo = jogadorVivo;
	}

	public Boolean getPause() {
		return pause;
	}

	public void setPause(Boolean pause) {
		this.pause = pause;
	}

	public Window getJanela() {
		return janela;
	}

	public void setJanela(Window janela) {
		this.janela = janela;
	}

	public Scene getCena() {
		return cena;
	}

	public void setCena(Scene cena) {
		this.cena = cena;
	}

	public Jogador getJogador() {
		return jogador;
	}

	public void setJogador(Jogador jogador) {
		this.jogador = jogador;
	}

	public Keyboard getTeclado() {
		return teclado;
	}

	public void setTeclado(Keyboard teclado) {
		this.teclado = teclado;
	}

	public Mob[] getMobs() {
		return mobs;
	}

	public void setMobs(Mob[] mobs) {
		this.mobs = mobs;
	}

	public String[] getNomesCenarios() {
		return nomesCenarios;
	}

	public void setNomesCenarios(String[] nomesCenarios) {
		this.nomesCenarios = nomesCenarios;
	}

	public int getIndiceCenarioAtual() {
		return indiceCenarioAtual;
	}

	public void setIndiceCenarioAtual(int indiceCenarioAtual) {
		this.indiceCenarioAtual = indiceCenarioAtual;
	}

	public long getTempoInicialCenario() {
		return tempoInicialCenario;
	}

	public void setTempoInicialCenario(long tempoInicialCenario) {
		this.tempoInicialCenario = tempoInicialCenario;
	}

}

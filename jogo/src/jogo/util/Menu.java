package jogo.util;

import java.awt.event.KeyEvent;

import jogo.cenario.Pantano;
import jogo.personagens.jogador.Jogador;
import jogo.personagens.npc.Mob;
import jplay.GameImage;
import jplay.Keyboard;
import jplay.URL;
import jplay.Window;

public class Menu {

	static GameImage menuStart = menu("menu1Start.png");
	static GameImage menuOptions = menu("menu1Options.png");
	static GameImage menuExit = menu("menu1Exit.png");
	static GameImage options = menu("options.png");

	static GameImage currentMenu = menuStart;

	public static void manuLogica(Window janela, Keyboard teclado, Jogador backupJogador, Mob[] backupMobs, String[] backupNomesCenarios, long backupTempoInicialCenario, int vidaJogador, int pontos) throws InterruptedException {
		while (true) {
			currentMenu.draw();
			janela.update();

			if (teclado.keyDown(Keyboard.DOWN_KEY)) {
				if (currentMenu == menuStart) {
					currentMenu = menuOptions;
				} else if (currentMenu == menuOptions) {
					currentMenu = menuExit;
				} else if (currentMenu == menuExit) {
					currentMenu = menuStart;
				}

				while (teclado.keyDown(Keyboard.DOWN_KEY)) {
					Thread.sleep(10);
				}
			} else if (teclado.keyDown(Keyboard.UP_KEY)) {

				if (currentMenu == menuStart) {
					currentMenu = menuExit;
				} else if (currentMenu == menuOptions) {
					currentMenu = menuStart;
				} else if (currentMenu == menuExit) {
					currentMenu = menuOptions;
				}
				

				while (teclado.keyDown(Keyboard.UP_KEY)) {
					Thread.sleep(10);
				}
			}

			if (teclado.keyDown(KeyEvent.VK_ESCAPE)) {
				new Pantano(janela, backupJogador, backupMobs, backupNomesCenarios, 0, vidaJogador, pontos, false);
			}

			if (teclado.keyDown(Keyboard.ENTER_KEY)) {
				if (currentMenu == menuStart) {
					new Pantano(janela, null, null, null, 0, 1500, pontos, true);
				} else if (currentMenu == menuOptions) {
					currentMenu = options;
				} else if (currentMenu == menuExit) {
					System.exit(0);
				} else if(currentMenu == options) {
					currentMenu = menuStart;
				}

			}
		}
	}
	
	public static void manuLogicainicial(Window janela, Keyboard teclado) throws InterruptedException {
		while (true) {
			currentMenu.draw();
			janela.update();

			if (teclado.keyDown(Keyboard.DOWN_KEY)) {
				if (currentMenu == menuStart) {
					currentMenu = menuOptions;
				} else if (currentMenu == menuOptions) {
					currentMenu = menuExit;
				} else if (currentMenu == menuExit) {
					currentMenu = menuStart;
				} else if(currentMenu == options) {
					currentMenu = menuStart;
				}

				while (teclado.keyDown(Keyboard.DOWN_KEY)) {
					Thread.sleep(10);
				}
			} else if (teclado.keyDown(Keyboard.UP_KEY)) {

				if (currentMenu == menuStart) {
					currentMenu = menuExit;
				} else if (currentMenu == menuOptions) {
					currentMenu = menuStart;
				} else if (currentMenu == menuExit) {
					currentMenu = menuOptions;
				}

				while (teclado.keyDown(Keyboard.UP_KEY)) {
					Thread.sleep(10);
				}
			}

			if (teclado.keyDown(Keyboard.ENTER_KEY)) {
				if (currentMenu == menuStart) {
					new Pantano(janela, null, null, null, 0, 1500, 0, true);
				} else if (currentMenu == menuOptions) {
					currentMenu = options;
				} else if (currentMenu == menuExit) {
					System.exit(0);
				} else if(currentMenu == options) {
					currentMenu = menuStart;
				}

			}
		}
	}

	public static GameImage menu(String menuImage) {
		return new GameImage(URL.sprite(menuImage));
	}
}

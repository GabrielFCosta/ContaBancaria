/*
 * Classe Cliente estabelece um socket conectado ao servidor.
 * Existem seis opções de operações disponíveis ao cliente.
 * Dados a serem enviados ao servidor são concatenados na variável
 * output utilizando quebra de linha (\n) como delimitador.
 */
package cliente;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Scanner;

public class Cliente {

	public static void main(String[] args) throws ClassNotFoundException {
		System.out.println("[1] -> Criar Conta");
		System.out.println("[2] -> Realizar Depósito");
		System.out.println("[3] -> Realizar Saque");
		System.out.println("[4] -> Realizar Transferência");
		System.out.println("[5] -> Encerrar Servidor");
		System.out.println("[6] -> Encerrar Cliente");
		Scanner dados = new Scanner(System.in);
		while (true) {
			System.out.println("------------------");
			System.out.printf("Selecione opção: ");
			// Variável opcao armazena operação selecionada
			String opcao = dados.next();
			// Variável output armazena string contendo código da operação e dados
			// a serem lidos pelo servidor
			String output = opcao;
			switch (opcao) {
			// Operação 1, criar uma nova conta
			case "1":
				System.out.println("Criar uma nova conta.");
				System.out.printf("Digite o RG do cliente: ");
				output += "\n" + dados.next();
				System.out.printf("Digite o nome do cliente: ");
				output += "\n" + dados.next();
				break;
			// Operação 2, realizar depósito em uma conta
			case "2":
				System.out.println("Realizar depósito.");
				System.out.printf("Digite o RG da conta para depósito: ");
				output += "\n" + dados.next();
				System.out.printf("Digite o valor a ser depositado: ");
				output += "\n" + dados.next();
				break;
			// Operação 3, realizar saque de uma conta
			case "3":
				System.out.println("Realizar saque.");
				System.out.printf("Digite o RG da conta para saque: ");
				output += "\n" + dados.next();
				System.out.printf("Digite o valor a ser sacado: ");
				output += "\n" + dados.next();
				break;
			// Operação 4, realizar transferência entre duas contas
			case "4":
				System.out.println("Realizar transferência.");
				System.out.printf("Digite o RG da conta remetente: ");
				output += "\n" + dados.next();
				System.out.printf("Digite o RG da conta destinatária: ");
				output += "\n" + dados.next();
				System.out.printf("Digite o valor a ser transferido: ");
				output += "\n" + dados.next();
				break;
			// Operação 5, encerrar servidor
			case "5":
				break;
			case "6":
				System.out.println("Encerrando cliente.");
				dados.close();
				System.exit(0);
			default:
				System.out.println("Opção inválida.");
				break;
			}
			try {
				// Cria um socket cliente conectado ao servidor no localhost e porta 28580
				Socket cliente = new Socket(InetAddress.getLocalHost(), 28580);
				// Inicializa saída do cliente para o servidor no ObjectOutputStream
				ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
				// Inicializa entrada do servidor para o cliente no ObjectInputStream
				ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
				// Escreve saída contendo código da operação e dados concatenados, delimitados
				// por "\n"
				saida.writeObject(output);
				// Imprime na tela do cliente mensagens escritas pelo servidor
				System.out.println((String) entrada.readObject());
				entrada.close();
				saida.close();
				cliente.close();
			} catch (IOException e) {
				System.out.println("Cliente não conectado ao servidor.");
				System.out.println("Erro: " + e);
			}
		}
	}
}

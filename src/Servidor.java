/*
 * Classe Servidor estabelece um socket servidor na porta 28580
 * e mantém um ArrayList de objetos do tipo ContaBancaria.
 * Mensagens de clientes recebidas na variável "entrada" são convertidas
 * para string e desconcatenadas. Primeiro elemento da mensagem será o
 * código da operação a ser executada no servidor, demais elementos serão
 * os dados necessários para sua execução.
 */
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Servidor {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		// Contas mantém ArrayList de objetos do tipo ContaBancaria já criados.
		ArrayList<ContaBancaria> contas = new ArrayList<ContaBancaria>();
		// Socket servidor na escuta na porta 28580
		ServerSocket servidor = new ServerSocket(28580);
		System.out.println("Servidor ouvindo na porta 28580");
		while (true) {
			// O método accept() bloqueia a execução até que o servidor receba um pedido de
			// conexão de um cliente
			Socket cliente = servidor.accept();
			System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
			// Inicializa entrada de dados do cliente para o servidor no ObjectInputStream
			ObjectInputStream entrada = new ObjectInputStream(cliente.getInputStream());
			// Inicializa saída de dados do servidor para o cliente no ObjectOutputStream
			ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
			// Faz o cast da entrada do cliente pra string
			String auxdados = (String) entrada.readObject();
			// Desconcatena string nas quebras de linha
			String[] dados = auxdados.split("\n");
			// Primeiro elemento da entrada define tipo de operação a ser realizada
			System.out.printf("Código da operação: %s", dados[0]);
			// Variável que indica o resultado da operação
			boolean res = false;
			String saldos = null;
			switch (dados[0]) {
			// Operação 1, criar uma nova conta
			case "1":
				// Verifica antes se RG já está cadastrado em outra conta
				if (!existeRG(dados[1], contas)) {
					res = criaConta(dados[1], dados[2], contas);
					saldos = "\nSaldo: R$ " + retornaSaldo(dados[1], contas);
					if (res) {
						saida.writeObject("Conta criada com sucesso." + saldos);
					} else {
						saida.writeObject("Erro ao adicionar conta.");
					}
				} else {
					saida.writeObject("Conta não pode ser criada, RG já cadastrado.");
				}
				break;
			// Operação 2, realizar depósito em uma conta
			case "2":
				// Verifica se conta existe
				if (existeRG(dados[1], contas)) {
					// Realiza depósito
					res = depositaConta(dados[1], dados[2], contas);
					saldos = "\nSaldo: R$ " + retornaSaldo(dados[1], contas);
					if (res) {
						saida.writeObject("Depósito realizado." + saldos);
					} else {
						saida.writeObject("Depósito não realizado." + saldos);
					}
				} else {
					saida.writeObject("RG não cadastrado.");
				}
				break;
			// Operação 3, realizar saque de uma conta
			case "3":
				// Verifica se conta existe
				if (existeRG(dados[1], contas)) {
					// Realiza saque
					res = sacaConta(dados[1], dados[2], contas);
					saldos = "\nSaldo: R$ " + retornaSaldo(dados[1], contas);
					if (res) {
						saida.writeObject("Saque realizado." + saldos);
					} else {
						saida.writeObject("Saque não realizado." + saldos);
					}
				} else {
					saida.writeObject("RG não cadastrado.");
				}
				break;
			// Operação 4, realizar transferência entre duas contas
			case "4":
				// Verifica se conta remetente existe
				if (existeRG(dados[1], contas)) {
					// Verifica se conta destinatária existe
					if (existeRG(dados[2], contas)) {
						// Realiza transferência
						res = transfereConta(dados[1], dados[2], dados[3], contas);
						saldos = "\nSaldo conta remetente: R$ " + retornaSaldo(dados[1], contas);
						saldos += "\nSaldo conta destinatária: R$ " + retornaSaldo(dados[2], contas);
						if (res) {
							saida.writeObject("Transferência realizada." + saldos);
						} else {
							saida.writeObject("Transferência não realizada." + saldos);
						}
					} else {
						saida.writeObject("RG destinatário não cadastrado.");
					}
				} else {
					saida.writeObject("RG remetente não cadastrado.");
				}
				break;
			// Operação 5, encerrar servidor
			case "5":
				saida.writeObject("Encerrando servidor.");
				servidor.close();
				System.exit(0);
			default:
				saida.writeObject("Opção Inválida.");
				break;
			}
			// Imprime no terminal do servidor o resultado da operação
			System.out.println(" -> Resultado: " + res);
			entrada.close();
			saida.close();
			cliente.close();
		}
	}

	// Retorna true se RG encontrado no ArrayList de contas
	private static boolean existeRG(String rg, ArrayList<ContaBancaria> contas) {
		for (ContaBancaria obj : contas) {
			if (obj.getRg().equalsIgnoreCase(rg)) {
				return true;
			}
		}
		return false;
	}

	// Retorna conta correpondente ao RG passado por parâmetro
	private static ContaBancaria retornaConta(String rg, ArrayList<ContaBancaria> contas) {
		for (ContaBancaria obj : contas) {
			if (obj.getRg().equalsIgnoreCase(rg)) {
				return obj;
			}
		}
		return null;
	}

	private static boolean criaConta(String rg, String nome, ArrayList<ContaBancaria> contas) {
		return contas.add(new ContaBancaria(rg, nome));
	}

	private static boolean depositaConta(String rg, String valor, ArrayList<ContaBancaria> contas) {
		ContaBancaria conta = retornaConta(rg, contas);
		if (conta != null) {
			return conta.deposito(Double.parseDouble(valor));
		}
		return false;
	}

	private static boolean sacaConta(String rg, String valor, ArrayList<ContaBancaria> contas) {
		ContaBancaria conta = retornaConta(rg, contas);
		if (conta != null) {
			return conta.saque(Double.parseDouble(valor));
		}
		return false;
	}

	private static boolean transfereConta(String rg1, String rg2, String valor, ArrayList<ContaBancaria> contas) {
		if (sacaConta(rg1, valor, contas)) {
			return depositaConta(rg2, valor, contas);
		}
		return false;
	}

	private static double retornaSaldo(String rg, ArrayList<ContaBancaria> contas) {
		return retornaConta(rg, contas).getSaldo();
	}
}

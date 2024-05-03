/*
 * Classe ContaBancaria contém atributos e métodos
 * básicos de uma conta. Os atributos são: Saldo da conta,
 * que é iniciado em zero ao criar uma nova conta; RG do 
 * cliente, utilizado como chave única; Nome do cliente. 
 * Métodos especificos para depósito e saque também foram
 * incluídos nesta classe.  
 */
public class ContaBancaria {
	private double saldo;
	private String rg;
	private String nome;

	public ContaBancaria(String rg, String nome) {
		super();
		setRg(rg);
		setNome(nome);
		setSaldo(0);
	}

	public double getSaldo() {
		return saldo;
	}

	public String getRg() {
		return rg;
	}

	public String getNome() {
		return nome;
	}

	public void setSaldo(double saldo) {
		this.saldo = saldo;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	// Valor para depósito deve ser maior que zero
	public boolean deposito(double valor) {
		if (valor > 0) {
			this.saldo += valor;
			return true;
		}
		return false;
	}

	// Valor para saque deve ser menor ou igual ao saldo
	// da conta e maior que zero
	public boolean saque(double valor) {
		if (valor <= this.saldo && valor > 0) {
			this.saldo -= valor;
			return true;
		}
		return false;
	}
}

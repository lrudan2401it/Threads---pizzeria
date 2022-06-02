public class ProgramD{
	public static void main(String[]args){
		Porudzbina porudzbina = new Porudzbina();
		Picerija picerija = new Picerija();
		String[]radnici= {"Nikola", "Marko"};
		for(int i = 0; i < 2; i++){
			Radnik radnik = new Radnik(picerija, porudzbina);
			radnik.start();
			radnik.setName(radnici[i]);
			System.out.println("Kreiran radnik:" + radnici[i]);
		}
		String[]dobavljaci = {"Milica", "Luka", "Petar", "Miroslav", "Jelena", "Todor", "Aleksandra", "Djordje", "Nemanja", "Natasa"};
		for(int j = 0; j < 10; j++){
			Thread dobavljac = new Thread(new Dobavljac(picerija, porudzbina));
			dobavljac.start();
			dobavljac.setName(dobavljaci[j]);
			System.out.println("Kreiran dostavljac: " + dobavljaci[j]);
		}
	}
}

class Radnik extends Thread{
	private final Picerija picerija;
	private final Porudzbina porudzbina;
	
	public Radnik(Picerija picerija, Porudzbina porudzbina){
		this.picerija = picerija;
		this.porudzbina = porudzbina;
	}
	
	public void run(){
		Porudzbina porudzbina1 = new Porudzbina();
		try {
			picerija.spremno(porudzbina1);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		try {
			sleep(200);
		} catch (InterruptedException e){
			e.printStackTrace();
		}
		
	}
}

class Dobavljac implements Runnable{
	private final Picerija picerija;
	private final Porudzbina porudzbina;
	
	public Dobavljac(Picerija picerija, Porudzbina porudzbina){
		this.picerija = picerija;
		this.porudzbina = porudzbina;
	}
	
	@Override
	public void run(){
		int ukupnaZarada = 0;
		int zarada = 0;
		
		int udaljenost = 0;
		
		for(int i = 0; i < 25; i++){
			try{ 
				picerija.preuzmi();
				int vrednost1 = porudzbina.dostavi();
				ukupnaZarada += vrednost1;
				int vrednost2 = porudzbina.cena;
				int baksis = vrednost1 - vrednost2;
				System.out.println("===== dobavljacu je dato " + vrednost1 + ", od cega je baksis: " + baksis +  " =====" );
				zarada += vrednost2;
			}catch(Exception e){
				
			}
			udaljenost += porudzbina.udaljenost;
			System.out.println("---> udaljenost porudzbine: " + porudzbina.udaljenost);
		}
		System.out.println("Ukupna udaljenost koju je presao dostavljac " + Thread.currentThread().getName() + " je:"+ udaljenost);
		int prihodDobavljaca = ukupnaZarada - zarada;
		System.out.println(Thread.currentThread().getName() + " - ukupan dnevni PROFIT " + ukupnaZarada + ", od cega je " + zarada + " ZARADA, dok je BAKSIS: " + prihodDobavljaca);
		/*novac1(ukupnaZarada);
		novac2(zarada);
		novac3(prihodDobavljaca);*/
	}
	
	/*public void novac1(int ukupnaZarada){
		int novacUkupnaZarada = 0;
		
		novacUkupnaZarada += ukupnaZarada;
		
		System.out.println("Ukupna kolicina novca u danu: " + novacUkupnaZarada);
	}
	
	public int novac2(int zarada){
		int novacZarada = 0;
		
		novacZarada += zarada;
		
		return novacZarada;
	}
	
	public int novac3(int prihodDobavljaca){
		int novacPrihodDobavljaca = 0;
		
		novacPrihodDobavljaca += prihodDobavljaca;
		
		return novacPrihodDobavljaca;
	}*/
}

class Picerija {
	Porudzbina porudzbina = new Porudzbina();

	public synchronized Porudzbina preuzmi() throws InterruptedException{
		int brojPorudzbina = 25;
		int brojRadnika = 10;
		for(int i = 0; i < brojPorudzbina; i++){
			for(int j = 0; j < brojRadnika; j++){
				notifyAll();
			}
		}
		return null;
	}

	public synchronized void spremno(Porudzbina porudzbina) throws InterruptedException {
		System.out.println("sprema se");
		wait();

	}
}

class Porudzbina {

	public final String id;
	public final int cena;
	public final long udaljenost;
	public int brojac = 0;

	public Porudzbina() {
		System.out.printf("%s sprema porudzbinu...%n", Thread.currentThread().getName());
		this.id = String.format("#%H", System.identityHashCode(this));
		this.cena = (int) (40 + 160 * Math.random()) * 10;
		this.udaljenost = (long) (200 + 2800 * Math.random());
		System.out.printf("%s je spremio porudzbinu %s%n", Thread.currentThread().getName(), this.id);
	}

	public int dostavi() {
		System.out.printf("%s dostavlja porudzbinu %s%n", Thread.currentThread().getName(), this.id);
		boolean interrupted = Thread.interrupted();
		long timeout = 5 * udaljenost;
		long endTime = System.currentTimeMillis() + timeout;
		while ((timeout = endTime - System.currentTimeMillis()) > 0) {
			try {
				Thread.sleep(timeout);
			} catch (InterruptedException e) {
				interrupted = true;
			}
		}
		if (interrupted) {
			Thread.currentThread().interrupt();
		}
		System.out.println();
		System.out.printf("%s je dostavio/la porudzbinu %s%n", Thread.currentThread().getName(), this.id);
		brojac++;
		System.out.println("PORUDZBINA broj: " + brojac);
		System.out.println();
		
		
		return cena + 5 * (int) (0.04 * cena * Math.random());
	}
}
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;





public class Vaccines {
	
	public class Country{
		private int ID;
		private int vaccine_threshold;
		private int vaccine_to_receive;
		private ArrayList<Integer> allies_ID;
		private ArrayList<Integer> allies_vaccine; 
		public Country() {
			this.allies_ID = new ArrayList<Integer>();
			this.allies_vaccine = new ArrayList<Integer>();
			this.vaccine_threshold = 0;
			this.vaccine_to_receive = 0;
		}
		public int get_ID() {
			return this.ID;
		}
		public int get_vaccine_threshold() {
			return this.vaccine_threshold;
		}
		public ArrayList<Integer> get_all_allies_ID() {
			return allies_ID;
		}
		public ArrayList<Integer> get_all_allies_vaccine() {
			return allies_vaccine;
		}
		public int get_allies_ID(int index) {
			return allies_ID.get(index);
		}
		public int get_allies_vaccine(int index) {
			return allies_vaccine.get(index);
		}
		public int get_num_allies() {
			return allies_ID.size();
		}
		public int get_vaccines_to_receive() {
			return vaccine_to_receive;
		}
		public void set_allies_ID(int value) {
			allies_ID.add(value);
		}
		public void set_allies_vaccine(int value) {
			allies_vaccine.add(value);
		}
		public void set_ID(int value) {
			this.ID = value;
		}
		public void set_vaccine_threshold(int value) {
			this.vaccine_threshold = value;
		}
		public void set_vaccines_to_receive(int value) {
			this.vaccine_to_receive = value;
		}
		
	}
	
	
	public boolean isValid(Country c,boolean[] grey) {
		if(c.vaccine_to_receive>=c.vaccine_threshold && grey[c.ID-1]==false) {
			return true;
		}
		else return false;
	}
		
	public int vaccines(Country[] graph){
		
		Country first = graph[0];
		Queue<Country> queue = new LinkedList<>();
		queue.add(first);
		
		// parallel array indicate which country is gray
		boolean[] grey= new boolean[graph.length];
		Arrays.fill(grey, false);
		grey[0] = true; // first country is colored grey
		
		int count=0;

		while(!queue.isEmpty()) {
			Country co_1 = queue.remove();
			// check each allies of that country
			for(int i=0;i < co_1.get_all_allies_ID().size();i++) {
		
				// get country id of each allies
				int co_id = co_1.get_allies_ID(i);
				
				Country this_ally = graph[co_id-1];
				
				// get number of vaccines to share to each allies
				int vac = co_1.get_allies_vaccine(i);
				// get the number of vaccines received by each allies
				int receive = this_ally.get_vaccines_to_receive();
				
				// if country 1 is colored grey, its vally is not,update received vaccines
				if(grey[co_1.ID-1]==true && grey[co_id-1]==false) {
					
					this_ally.set_vaccines_to_receive(receive-vac);
					// if vaccines reveived< threshold
					if(isValid(this_ally,grey)==false) {
					grey[co_id-1] = true; // mark it grey
					queue.add(this_ally);
				
					}
				}	
				
			}
			
		}
		// the number of non-grey country is the answer
		for(int k=0;k<graph.length;k++) {
			if (grey[k]==false) {
				count++;
			}
		}
		

		return count;
	}

	public void test(String filename) throws FileNotFoundException {
		Vaccines hern = new Vaccines();
		Scanner sc = new Scanner(new File(filename));
		int num_countries = sc.nextInt();
		Country[] graph = new Country[num_countries];
		for (int i=0; i<num_countries; i++) {
			graph[i] = hern.new Country(); 
		}
		for (int i=0; i<num_countries; i++) {
			if (!sc.hasNext()) {
                sc.close();
                sc = new Scanner(new File(filename + ".2"));
            }
			int amount_vaccine = sc.nextInt();
			graph[i].set_ID(i+1);
			graph[i].set_vaccine_threshold(amount_vaccine);
			int other_countries = sc.nextInt();
			for (int j =0; j<other_countries; j++) {
				int neighbor = sc.nextInt();
				int vaccine = sc.nextInt();
				graph[neighbor -1].set_allies_ID(i+1);
				graph[neighbor -1].set_allies_vaccine(vaccine);
				graph[i].set_vaccines_to_receive(graph[i].get_vaccines_to_receive() + vaccine);
			}
		}
		sc.close();
		int answer = vaccines(graph);
		System.out.println(answer);
	}

	public static void main(String[] args) throws FileNotFoundException{
		Vaccines vaccines = new Vaccines();
		vaccines.test(args[0]); // run 'javac Vaccines.java' to compile, then run 'java Vaccines testfilename'
	}

}

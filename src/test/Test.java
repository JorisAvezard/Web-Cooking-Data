package test;


public class Test {
	
	public static int arrondirDizaine(int entry){
		int result = 0;
		result = (int) (Math.floor(entry / 10) * 10);

		return result;
	}
	
	public static int arrondirCentaine(int entry){
		int result = 0;
		result = (int) (Math.floor(entry / 500) * 500);

		return result;
	}

	public static void main(String[] args) {
		System.out.println(System.currentTimeMillis());
		int indice = 0;
		for(int i=0;i<1000;i++){
			indice++;
		}
		System.out.println(System.currentTimeMillis());
	}

}

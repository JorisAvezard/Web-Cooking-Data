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
		int result = arrondirCentaine(2638);
		System.out.println(result);
	}

}

//Done by Johnny Lai for job interview coding test

public class FibonacciByRecrusive {
	
	//recrusive method to fulfil the requirement
    public static long fibonacci(int n) {
        if (n <= 1) return n;
        else return fibonacci(n-1) + fibonacci(n-2);
    }

    public static void main(String[] args) {
		int n = 0;
		//control the input number in the int range
		//try to catch all exception caused by user input
		try	{
			n = Integer.parseInt(args[0]);
			if (n <= 0)	throw new NumberFormatException();
		}
		catch (NumberFormatException | ArrayIndexOutOfBoundsException e)	{
				System.out.println("Please try again and enter integer between 1 - 2147483647");
				System.exit(0);
		}	
		//since the fibonacci result always larger than the input, so use Long to cater the overflow issue
		long next = 0;
		
		//Since we need to stop to display the fibonacci by monitoring the result, so inevitably we need an for loop to control it
        for (int i = 1; i <= n; i++)	{
			next = fibonacci(i);
			if (next < n)	{
				System.out.println(i + " " + next); }	
			else	{	break;	}	
		}	
    }

}

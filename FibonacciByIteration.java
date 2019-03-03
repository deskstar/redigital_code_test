//Done by Johnny Lai for job interview coding test

public class FibonacciByIteration {

    public static void main(String[] args) {
		
		//control the input number in the int range
		//try to catch all exception caused by user input
		int n = 0;
		try	{
			n = Integer.parseInt(args[0]);
			if (n <= 0)	throw new NumberFormatException(); }
			
		catch (NumberFormatException | ArrayIndexOutOfBoundsException e)	{
				System.out.println("Please try again and enter integer between 1 - 2147483647");
				System.exit(0);	}

		//since the fibonacci result always larger than the input, so use Long to cater the overflow issue
        long t1 = 1, t2 = 1, last = 0;

        for (long i = 1; i <= n; ++i)
        {
			//cater the requirement of display the fibonacci number but 
			//ending at the largest value that is smaller than or equal to the input value
			if (t1 > n)	{
					t1 = last;
					break;
			}	
            System.out.println(i + " " + t1);
						
            long sum = t1 + t2;
			last = t1;
            t1 = t2;
            t2 = sum;
        }
    }
}

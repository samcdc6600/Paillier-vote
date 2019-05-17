import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class GetBigInts
{
	public Collection<BigInteger> getBigInts(Scanner in, int get)
	{
		Collection<String> ret = new ArrayList<>();
		for(int iter = get; iter > 0; --iter)
			{
			try
			{
				if(in.hasNextLine()) 
				{
					String num = "0";
					boolean badNum = true;
					while(badNum)
					{//Here the second argument 10 is the radix (base).
						if(isInteger(num = in.nextLine(), 10))
						{
						ret.add(num);
							badNum = false;
						}
						else
						{
							System.out.println("Error malformed input!\nYou must enter a number");
						}
					}
				}
				else
				{
					throw new Error("Error reading from input stream");
				}
			}
			catch(Error e)
			{
				System.out.println(e.getMessage());
				System.exit(-1);
			}
			}
		return strToBigInt(ret);
	}
	
	public Collection<BigInteger> strToBigInt(Collection<String> bIs)
	{
		Collection<BigInteger> ret = new ArrayList<BigInteger>();
		for(String s: bIs)
		{
			ret.add(new BigInteger(s));
		}
		return ret;
	}
	
	/*This function checks whether s is an integer number
	this function was taken from a stack overflow article at
	the following address:
	https://stackoverflow.com/questions/5439529/determine-if-a-string-is-an-integer-in-java#5439547
	*/
	public boolean isInteger(String s, int radix)
	{
		if(s.isEmpty())
			return false;
		for(int i = 0; i < s.length(); i++)
		{
			if(i == 0 && s.charAt(i) == '-')
			{
				if(s.length() == 1)
					return false;
				else continue;
			}
			if(Character.digit(s.charAt(i), radix) < 0)
				return false;
		}
		return true;
	}
}

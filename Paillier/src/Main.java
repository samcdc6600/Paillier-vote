import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Scanner;

public class Main 
{
	public static void main(String args[])
	{
		//GetBigInts gBI = new GetBigInts();
		Scanner in = new Scanner(System.in);
		ArrayList<BigInteger> cryptoNumbers = getCryptoNumbers(in);
		ArrayList<BigInteger> votes = getVotes(in);
		CalcPaillier cP = new CalcPaillier();	
		ArrayList<BigInteger> encryptedVotes = encryptVotes(cP, cryptoNumbers, votes);
		BigInteger encryptedTally = addVotes(cP, encryptedVotes);
		System.out.println("The votes have been securely tallied and the results are: " + cP.decrypt(encryptedTally, cP.getPhy(cryptoNumbers.get(0), cryptoNumbers.get(1)), cP.getN(cryptoNumbers.get(0), cryptoNumbers.get(1)), cryptoNumbers.get(2)));
		in.close();
	}
	
	private static ArrayList<BigInteger> getCryptoNumbers(Scanner in)
	{
		System.out.println("Enter p, q and g: ");
		GetBigInts gBI = new GetBigInts();
		return new ArrayList<BigInteger>(gBI.getBigInts(in, 3));
	}
	
	private static ArrayList<BigInteger> getVotes(Scanner in)
	{
		int nVotes = 0;
		boolean run = true;
		while(run)
		{
			System.out.println("Enter the number of votes to be made: ");
			if(in.hasNextLine())
			if((nVotes = Integer.parseInt(in.nextLine())) < 2)
			{
				System.out.println("You must make at least 2 votes");
			}
			else
			{
				run = false;
			}
		}
		System.out.println("Please enter each vote followed by it's corrisponding r value: ");
		GetBigInts gBI = new GetBigInts();
		return new ArrayList<BigInteger>(gBI.getBigInts(in, nVotes*2));
	}
	
	private static ArrayList<BigInteger> encryptVotes(CalcPaillier cP, ArrayList<BigInteger> cryptoNumbers, ArrayList<BigInteger> votes)
	{//skip every second index in votes because votes are stored at even indexes (the value of r for each vote is stored in the index following the vote.)
		//for example if there were 4 votes the array would look like this ->  [0]m, [1]r,   [2]m, [3]r,   [4]m, [5]r,   [6]m, [7]r
		ArrayList<BigInteger> ret = new ArrayList<BigInteger>();
		for(int iter = 0; iter < votes.size(); iter += 2)
		{//argument order for CalcPaillier.encrypt: p,                 q,                    g,            m,               r
			ret.add(cP.encrypt(cryptoNumbers.get(0), cryptoNumbers.get(1), cryptoNumbers.get(2), votes.get(iter), votes.get(iter + 1)));
		}
		return ret;
	}
	
	private static BigInteger addVotes(CalcPaillier cP, ArrayList<BigInteger> encryptedVotes)
	{
		BigInteger sum = new BigInteger("0");
		try
		{
			if(encryptedVotes.size() > 1)
			{
				for(int iter = 0; iter < encryptedVotes.size(); ++iter)
				{
					if(iter == 0)
					{
						sum = cP.homomorphicAdd(encryptedVotes.get(iter), encryptedVotes.get(iter + 1));
						++iter;//we must do this since we have already accessed the next index.
					}
					else
					{
						sum = cP.homomorphicAdd(sum, encryptedVotes.get(iter));
					}
				}
			}
			else
				throw new Error("Error less then two encrypted votes in encryptedVotes");
		}
		catch(Error e)
		{
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		return sum;
	}
}
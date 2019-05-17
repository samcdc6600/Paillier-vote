import java.math.BigInteger;

public class CalcPaillier 
{
	//---------------------------------------------------------------------------------------------------------------
	//---------------------------------------------Encryption--------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------
	public BigInteger encrypt(final BigInteger p, final BigInteger q, final BigInteger g, final BigInteger m, final BigInteger r)
	{
		BigInteger left = new BigInteger("0"), right = new BigInteger("0");
		left = left.add(getN(p, q));
		right = right.add(pow(left, new BigInteger("2")));
		left = getGR(g, m, r, left);
		left = left.mod(right);
		return left;
	}
	
	public BigInteger getN(final BigInteger p, final BigInteger q)
	{
		return p.multiply(q);
	}
	
	private BigInteger getGR(final BigInteger g, final BigInteger m, final BigInteger r, final BigInteger n)
	{
		return ((pow(g, m)).multiply(pow(r, n)));
	}
	
	private BigInteger pow(final BigInteger a, BigInteger b)
	{
		BigInteger ret = new BigInteger(a.toString());
		if(b.compareTo(new BigInteger("0")) == 0)
			return new BigInteger("1");//Special case zero, return 1.
		else
			if(b.compareTo(new BigInteger("1")) == 0)
			return a;//Special case one, return a.
			else
				try
				{
					if(b.compareTo(new BigInteger("0")) < 0)
						throw new Error("Error public BigInteger pow(BigInteger a, bigInteger b) does not handel negative powers!");
				}
		catch(Error e)
		{
			System.out.println(e.getMessage());
			System.exit(-1);
		}
		b = b.add(new BigInteger("-1"));//account for off by one error in for loop
		for(; b.compareTo(new BigInteger("0")) > 0; b = b.add(new BigInteger("-1")))
			{
				ret = ret.multiply(a);

			}
		return ret;
	}
	//---------------------------------------------------------------------------------------------------------------
	//---------------------------------------------Decryption--------------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------
	//m = (L((c^phy)mod(n^2)) * mu) mod n
	//where L(u) = (u -1) / n,	mu = (k^-1) mod n,	k = L((g^phy) mod (n^2))
	public BigInteger decrypt(final BigInteger c, final BigInteger phy, final BigInteger n, final BigInteger g) 
	{
		BigInteger ret = new BigInteger((pow(c, phy).mod(pow(n, new BigInteger("2"))).subtract(new BigInteger("1")).divide(n).toString()));
		return (ret.multiply(getMu(g, phy, n))).mod(n);
		//System.out.println((getL(c, phy, n).divide(getL(g, phy, n))).mod(n) + " decrypt\n");
		//return (getL(c, phy, n).divide(getL(g, phy, n))).mod(n);
	}
	
	/*private BigInteger getL(BigInteger cORg, BigInteger phy, BigInteger n)
	{
		System.out.println(((pow(cORg, phy).mod(pow(n, new BigInteger("2")))).subtract(new BigInteger("1"))).divide(n) + " getL\n");
		return ((pow(cORg, phy).mod(pow(n, new BigInteger("2")))).subtract(new BigInteger("1"))).divide(n);
	}*/
	public BigInteger getPhy(final BigInteger p, final BigInteger q)
	{
		return lcm(q.subtract(new BigInteger("1")), p.subtract(new BigInteger("1")));
	}
	
	private BigInteger getMu(final BigInteger g, final BigInteger phy, final BigInteger n)
	{
		return getK(g, phy, n).modInverse(n);
	}
	
	private BigInteger getK(final BigInteger g, final BigInteger phy, final BigInteger n)
	{
		BigInteger ret = new BigInteger(pow(g, phy).mod(pow(n, new BigInteger("2"))).toString());
		return (ret.subtract(new BigInteger("1"))).divide(n);
	}
	
	private BigInteger lcm(final BigInteger a, final BigInteger b)
	{
		return a.multiply(b.divide(a.gcd(b)));
	}
	//---------------------------------------------------------------------------------------------------------------
	//----------------------------------------Privacy Preserving comp------------------------------------------------
	//---------------------------------------------------------------------------------------------------------------
	public BigInteger homomorphicAdd(final BigInteger c1, final BigInteger c2)
	{
		return c1.multiply(c2);
	}
}
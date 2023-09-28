import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;



public class AccountDAOFake implements IAccountDAO {
	/*
	 * A full in-memory fake of AccountDAO.
	 * 
	 * IMPORTANT NOTE:
	 * If you take advantage of the Account class for simulating an in-memory database,
	 * make sure that your storage representation relies on fully cloned objects, not just Account references passed.
	 * Tests should not be able to distinguish this object from a real DAO object connected to a real DB. 
	 * In this sense, this class is a full fake that works with all inputs. 
	 */
	private final HashMap<String, Account> all = new HashMap<String, Account>();
		
	public boolean isFullFake() {  
		return true;
	}

	public void save(Account member) {
		if (member == null)
			return;
		all.computeIfAbsent(member.getUserName(), k -> member.clone());
	}

	public Account findByUserName(String userName) {
		if (userName.isEmpty() || !all.containsKey(userName))
			return null;
		return all.get(userName).clone();
	}
	
	public Set<Account> findAll()  {
		Set<Account> result = new HashSet<>();
		for (Account a : all.values()) {
			result.add(a.clone());
		}
		return result;
	}

	public void delete(Account member) {
		if (member == null)
			return;
		all.remove(member.getUserName());
	}

	public void update(Account member) {
		if (member == null)
			return;
		if (!all.containsKey(member.getUserName()))
			return;
		all.put(member.getUserName(), member.clone());
	}

}

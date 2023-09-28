import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;


public class TestSNWithMockDAO extends TestSNAbstractGeneric {

    @Override
    @Before
    public void setUp() throws Exception {
        // whatever you need to do here
        accountDAO = DAOFactory.getInstance().getAccountDAOMock();
        super.setUp();
    }

    /*
     * Generic tests are automatically inherited from abstract superclass - they should continue to work here!
     */

    /*
     * VERIFICATION TESTS
     *
     * These tests use a mock and verify that persistence operations are called.
     * They ONLY ensure that the right persistence operations of the mocked IAccountDAO implementation are called with
     * the right parameters. They need not and cannot verify that the underlying DB is actually updated.
     * They don't verify the state of the SocialNetwork either.
     *
     */

    @Test
    public void willAttemptToPersistANewAccount() throws UserExistsException {
        // make sure that when a new member account is created, it will be persisted
        verify(accountDAO, atLeast(1)).save(any(Account.class));
        assertNotNull(accountDAO.findByUserName("John"));
    }

    @Test
    public void willAttemptToPersistSendingAFriendRequest()
            throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
        // make sure that when a logged-in member issues a friend request, any changes to the affected accounts will be persisted
        sn.login(m1);
        sn.sendFriendshipTo(m2.getUserName());
        verify(accountDAO, times(7)).findByUserName(any(String.class));
        verify(accountDAO, times(2)).update(any(Account.class));
        assertTrue(accountDAO.findByUserName("John").getOutgoingRequests().contains("Hakan"));
        assertTrue(accountDAO.findByUserName("Hakan").getIncomingRequests().contains("John"));
    }

    @Test
    public void willAttemptToPersistAcceptanceOfFriendRequest()
            throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
        // make sure that when a logged-in member issues a friend request, any changes to the affected accounts will be persisted
        sn.login(m1);
        sn.sendFriendshipTo(m2.getUserName());
        sn.login(m2);
        sn.acceptFriendshipFrom(m1.getUserName());
        verify(accountDAO, times(9)).findByUserName(any(String.class));
        verify(accountDAO, times(4)).update(any(Account.class));
        assertFalse(accountDAO.findByUserName("John").getOutgoingRequests().contains("Hakan"));
        assertFalse(accountDAO.findByUserName("Hakan").getIncomingRequests().contains("John"));
        assertTrue(accountDAO.findByUserName("John").hasFriends());
        assertTrue(accountDAO.findByUserName("Hakan").hasFriends());
        assertTrue(accountDAO.findByUserName("John").hasFriend(m2));
        assertTrue(accountDAO.findByUserName("Hakan").hasFriend(m1));
    }

    @Test
    public void willAttemptToPersistRejectionOfFriendRequest()
            throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
        // make sure that when a logged-in member rejects a friend request, any changes to the affected accounts will be persisted
        sn.login(m1);
        sn.sendFriendshipTo(m2.getUserName());
        sn.login(m2);
        sn.rejectFriendshipFrom(m1.getUserName());
        verify(accountDAO, times(9)).findByUserName(any(String.class));
        verify(accountDAO, times(4)).update(any(Account.class));
        assertFalse(accountDAO.findByUserName("John").getOutgoingRequests().contains("Hakan"));
        assertFalse(accountDAO.findByUserName("Hakan").getIncomingRequests().contains("John"));
        assertFalse(accountDAO.findByUserName("John").getFriends().contains("Hakan"));
        assertFalse(accountDAO.findByUserName("Hakan").getFriends().contains("John"));
    }

    @Test
    public void willAttemptToPersistBlockingAMember()
            throws UserNotFoundException, UserExistsException, NoUserLoggedInException {
        // make sure that when a logged-in member blocks another member, any changes to the affected accounts will be persisted
        sn.login(m1);
        sn.block(m2.getUserName());
        verify(accountDAO, times(7)).findByUserName(any(String.class));
        verify(accountDAO, times(1)).update(any(Account.class));
        assertTrue(accountDAO.findByUserName("John").blockedMembers().contains("Hakan"));
    }

    @Test
    public void willAttemptToPersistLeavingSocialNetwork()
            throws UserExistsException, UserNotFoundException, NoUserLoggedInException {
        // make sure that when a logged-in member leaves the social network, his account will be permanenlty deleted and
        // any changes to the affected accounts will be persisted
        sn.login(m1);
        sn.sendFriendshipTo(m2.getUserName());
        sn.sendFriendshipTo(m3.getUserName());
        sn.login(m2);
        sn.acceptFriendshipFrom(m1.getUserName());
        sn.login(m4);
        sn.sendFriendshipTo(m1.getUserName());
        sn.login(m1);
        sn.leave();
        doReturn(null).when(accountDAO).findByUserName("John");
        verify(accountDAO, times(16)).findByUserName(any(String.class));
        verify(accountDAO, times(13)).update(any(Account.class));
        verify(accountDAO, times(1)).delete(any(Account.class));
        assertNull(accountDAO.findByUserName("John"));
        assertFalse(accountDAO.findByUserName("Hakan").getFriends().contains("John"));
        assertFalse(accountDAO.findByUserName("Serra").getIncomingRequests().contains("John"));
        assertFalse(accountDAO.findByUserName("Dean").getOutgoingRequests().contains("John"));
    }

}

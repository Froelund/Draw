package net.froelund.drawing.control;

import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

public class SessionServiceTest {

    SessionService sessionService;
    private int sessionCount = 0;

    @Before
    public void init(){
        sessionService = new SessionService();
    }

    @Test
    public void testSessionAdding(){
        sessionService.clientConnect(getMockedSession());
        Assert.assertThat("Session wasn't added", sessionService.getSessionCount(), CoreMatchers.is(1));
    }

    Session getMockedSession(){
        Session session = Mockito.mock(Session.class);
        Mockito.when(session.getId()).thenReturn(String.valueOf(sessionCount));
        sessionCount++;
        return session;
    }

    Session getMockedDelayedSession(){
        Session session = getMockedSession();
        Mockito.when(session.getBasicRemote()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                RemoteEndpoint.Basic basicRemoteMock = Mockito.mock(RemoteEndpoint.Basic.class);
                Thread.sleep(5000);
                return basicRemoteMock;
            }
        });
        try {
            session.getBasicRemote().sendObject("Nih!");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (EncodeException e) {
            e.printStackTrace();
        }
        return session;
    }
}
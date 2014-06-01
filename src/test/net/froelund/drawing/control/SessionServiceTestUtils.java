package net.froelund.drawing.control;

import org.mockito.Mockito;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import javax.websocket.EncodeException;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

/**
 * Created by froelund on 6/1/14.
 */
class SessionServiceTestUtils {

    private static int sessionCount = 0;

    static Session getMockedSession(){
        Session session = Mockito.mock(Session.class);
        Mockito.when(session.getId()).thenReturn(String.valueOf(sessionCount));
        sessionCount++;
        return session;
    }

    static Session getMockedDelayedSession(){
        Session session = getMockedSession();
        Mockito.when(session.getBasicRemote()).thenAnswer(new Answer<Object>() {
            @Override
            public Object answer(InvocationOnMock invocationOnMock) throws Throwable {
                RemoteEndpoint.Basic basicRemoteMock = Mockito.mock(RemoteEndpoint.Basic.class);
                Thread.sleep(100);
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

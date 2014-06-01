package net.froelund.drawing.control;

import net.froelund.Cache;
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

    @Before
    public void init(){
        sessionService = Mockito.mock(SessionService.class, Mockito.CALLS_REAL_METHODS);
        sessionService.cache = new Cache();
        sessionService.cache.initialize();
        sessionService.init();
    }

    @Test
    public void testSessionAdding(){
        sessionService.clientConnect(SessionServiceTestUtils.getMockedSession());
        Assert.assertThat("Session wasn't added", sessionService.getSessionCount(), CoreMatchers.is(1));
    }

}